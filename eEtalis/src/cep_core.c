#include "cep_core.h"


extern etalis_main_conf _conf;
/*
* a general function to handle sliding windows
*/
void _win_cep(void* exec)
{
    printf("hello _win_cep\n");
}

/*
* Zstream implementation for seq (left event)
*/
void _seq_win_cep_l(void* exec)
{
    EtalisEvent* Event = (EtalisEvent*)exec;

    assert(Event != NULL);

    /* push the event on the stack */ /* a stack is not the most optimal datastructure because we might need to delete a whole chunk of events which o(n) for a stack */
    StackPush(Event->RootModel->eventStack,Event);


}

/*
* Zstream implementation for seq (right event)
*/
void _seq_win_cep_r(void* exec)
{
    EtalisEvent* Event_b  = (EtalisEvent*)exec;
    EtalisExecNode* rule_ = Event_b->RootModel->parentNode;
    assert(Event_b != NULL && rule_ != NULL);


    EtalisEvent* Event_a = StackPop(rule_->leftChild->eventStack);

    if(Event_a == NULL) /* stack is empty*/
    {
        free(Event_b);
        return;
    }
    /* Event a is in the stack */
    /* a complex event will be generated if the conditions are met */
    /* testing if the conditions could be proven correct */





    if(getTimeDiff(&(Event_b->timestamps[0]),&(Event_a->timestamps[0])) > rule_->window_size) /* out of the window*/
    {
        /* printf(" --- Time difference is %d > window size of %d\n",Event_b->timestamps[0]-Event_a->timestamps[0],rule_->window_size);
        */
        free(Event_a); /* check this */
        free(Event_b);
        return;
    }


    if (Event_b->RootModel->event.arity != 0 )
    {
        if(aprio_validation(Event_b) != ETALIS_OK) /* argument a priori conditions must be true*/
        {
#ifdef DEBUG
            log_err("Event could be validated !");
#endif
            free(Event_a); /* check*/
            free(Event_b);
            return;
        }

        /* event arguments must be valide - implicite condition */
        if(validate_arguments(Event_b,Event_a) != ETALIS_OK)
        {
#ifdef DEBUG
            printf("Event arguments could not be validated ! \n");
#endif
            free(Event_a); /* check*/
            free(Event_b);
            return;
        }
    }


    EtalisEventNode* cplxEvModel=NULL; /* Complex Event Root Model */

    /* the rest depends on the CEP policy */
    if (_conf.policy==recent) /* recent policy */
    {
        cplxEvModel = rule_->parentEvent;

        /* triggering the complex event */
        EtalisEvent* cplxEvent = (EtalisEvent*)malloc(sizeof(EtalisEvent));
        cplxEvent->RootModel = rule_->parentEvent;
        /* bind the arguments of the complex event */ /* todo validate */
        cplxEvent->args = (int*)malloc(sizeof(int)*cplxEvModel->event.arity);
        size_t arg_iterator=0;
        for(arg_iterator=0; arg_iterator<cplxEvModel->event.arity; arg_iterator++)
        {
            switch (cplxEvModel->arg_links[arg_iterator].event_)
            {
            case 1 :
                *((int*)cplxEvent->args+arg_iterator)=*((int*)Event_a->args+(cplxEvModel->arg_links[arg_iterator].argument_number));
                break;

            case 2 :
                *((int*)cplxEvent->args+arg_iterator)=*((int*)Event_b->args+(cplxEvModel->arg_links[arg_iterator].argument_number));
                break;
            }
        }


        cplxEvent->timestamps[0] = Event_a->timestamps[0]; /* timestamp structure as suggested by the ETALIS paper */
        cplxEvent->timestamps[1] = Event_b->timestamps[1];
        triggerEvent_intern_no_hash(cplxEvent);            /* trigger the complex event (we already know the EventModel, so we don't need to search for it in the _event_hash) */

        /* garbage collect : we don't need these events anymore */
        free(Event_a);
        free(Event_b);
        return;
    }
    else if (_conf.policy==unrestricted)
    {
        /* TODO */
        return ;
    }

}
void _seqf_l(void* exec)
{
    ;
}

/** The batch for the seq operator as defined in http://db.csail.mit.edu/pubs/xstream-sigmod09.pdf Section 4.4.1 */

void _seq_batch_r(void* exec)
{



    EtalisEvent* Event_b  = (EtalisEvent*)exec;
    if (Event_b == NULL) return;
    EtalisExecNode* rule_ = Event_b->RootModel->parentNode;

    EtalisEvent* Event_a=NULL;

    /* calculate EAT */

    float EAT = getHighResStamp(&Event_b->timestamps[1]) - rule_->window_size; /* TODO fix */

    if (rule_->leftChild->is_temp)
    {
        _seq_batch_r(StackPop(rule_->leftChild->childNode->rightChild->eventStack));
    }


    do
    {
        /*if (getHighResStamp(&Event_b->timestamps[0]) < EAT ) { free(Event_b); continue;} /* problem : all atomic right side events does not fulfill this condition */
        Event_a = StackPop(rule_->leftChild->eventStack);
        while( Event_a != NULL && getTimeDiff(&(Event_b->timestamps[0]),&(Event_a->timestamps[1])) >= 0 ) /* problem : recursion */
        {
            /*if (getHighResStamp(&Event_a->timestamps[0]) < EAT )*/ /* EAT is not compatible with timestamps */
            if (Event_b->timestamps[1].time - Event_a->timestamps[0].time  > rule_->window_size)
            {
                free (Event_a);
                Event_a = StackPop(rule_->leftChild->eventStack);
                continue; /* normally you don't have to continue here, because events are already choronogically ordered in the stack */ /* fix this */
            }
            if(validate_arguments(Event_b,Event_a) != ETALIS_OK)
            {
#ifdef DEBUG
                printf("Event arguments could not be validated ! \n");
#endif
                free(Event_a); /* check*/
                Event_a = StackPop(rule_->leftChild->eventStack);
                continue;
            }

            EtalisEventNode* cplxEvModel=NULL; /* Complex Event Root Model */

            /* the rest depends on the CEP policy */
            if (_conf.policy==recent) /* recent policy */
            {
                cplxEvModel = rule_->parentEvent;

                /* triggering the complex event */
                EtalisEvent* cplxEvent = (EtalisEvent*)malloc(sizeof(EtalisEvent));
                cplxEvent->RootModel = rule_->parentEvent;
                /* bind the arguments of the complex event */ /* todo validate */
                cplxEvent->args = (int*)malloc(sizeof(int)*cplxEvModel->event.arity);
                size_t arg_iterator=0;
                for(arg_iterator=0; arg_iterator<cplxEvModel->event.arity; arg_iterator++)
                {
                    switch (cplxEvModel->arg_links[arg_iterator].event_)
                    {
                    case 1 :
                        *((int*)cplxEvent->args+arg_iterator)=*((int*)Event_a->args+(cplxEvModel->arg_links[arg_iterator].argument_number));
                        break;

                    case 2 :
                        *((int*)cplxEvent->args+arg_iterator)=*((int*)Event_b->args+(cplxEvModel->arg_links[arg_iterator].argument_number));
                        break;
                    }
                }


                cplxEvent->timestamps[0] = Event_a->timestamps[0]; /* timestamp structure as suggested by the ETALIS paper */
                cplxEvent->timestamps[1] = Event_b->timestamps[1];
                triggerEvent_intern_no_hash(cplxEvent);            /* trigger the complex event (we already know the EventModel, so we don't need to search for it in the _event_hash) */

                /* garbage collect : we don't need these events anymore */
                free(Event_a);
                free(Event_b);
                return;
            }
            else if (_conf.policy==unrestricted)
            {
                /* TODO */
                return ;
            }

            Event_a = StackPop(rule_->leftChild->eventStack);
        }


    }
    while (Event_b = StackPop(Event_b->RootModel->eventStack));









}

void _cep_print_event(void* exec)
{
#ifndef BENCHMARKING /* we are not interested in printing the complex events if benchmarking mode is on */
    EtalisEvent* event  = (EtalisEvent*)exec;
    if(event->RootModel->event.arity == 0)
        log_info("\n--- Fired ! %s/0 @ [%ld.%06ld,%ld.%06ld]\n",event->RootModel->event.name,event->timestamps[0].time,event->timestamps[0].parts,event->timestamps[1].time,event->timestamps[1].parts);
    else
    {


        printf("--- Fired ! %s(",event->RootModel->event.name);
        int arg_i;
        for (arg_i=0; arg_i<event->RootModel->event.arity-1; arg_i++)
            printf("%d,",*((int*)event->args+arg_i));
        printf("%d",*((int*)event->args+arg_i));
        printf(") @ [%ld.%06ld,%ld.%06ld]\n",event->timestamps[0].time,event->timestamps[0].parts,event->timestamps[1].time,event->timestamps[1].parts);

        ;
    }

    free(event);
#endif
}

/*in case of an identity, just execute the complex event*/
void _cep_identity(void* exec)
{
    EtalisExecNode* myNode = (EtalisExecNode*)exec;

    printf("testing node: %s\n",myNode->name);

    /* if (myNode->parentNode != NULL)
         myNode->parentNode->parentEvent->trigger(NULL);
    */
}

