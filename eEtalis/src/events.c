#include "events.h"

/**
 * File : events.c
 * Author : Ahmed Khalil Hafsi ahmed@hafsi.org
 *
 */


/* Supported operators in eEtalis */
t_CoreCEP_LUT CEP_LUT_[] =
{

    { "withinop",2,_seq_win_cep_l,_seq_win_cep_r,parse_within_op_},
    { "seqf"    ,2,NULL ,NULL,parse_seq_op_ }
     /*operator_tag, operator_arity,operator_left_function, operator_right_function, operator parsing handler*/


};

int LUT_Size=2;


extern EtalisExecTree*  _exec_tree;


void parse_seq_op_ (EtalisExecNode* operatorNode,term_t t)
{

    assert(operatorNode != NULL);

    fid_t fid = PL_open_foreign_frame();


    /*get components of the operator*/

    term_t _level_1 = PL_new_term_refs(2);
    term_t _left_event=_level_1;
    term_t _right_event=_level_1+1;
    atom_t _left_event_name,_right_event_name;

    int temp_arity,i=0;

    /* TODO check for embedded operators, if operator -> parse right function ; else get atomic events */
    PL_get_arg(1,t,_left_event);
    PL_get_name_arity(_left_event,&_left_event_name,&temp_arity);

    char * tt = PL_atom_chars(_left_event_name);
    i=0;
    while(strcmp(CEP_LUT_[i].CEP_name,PL_atom_chars(_left_event_name)) != 0 && i<LUT_Size) i++; /* #CONT */

     if(i != LUT_Size) /*The operator is found in the CEP_LUT_*/ /* create a temp event */
    {
        EtalisEventNode* TempEvent = (EtalisEventNode*)calloc(1,sizeof(EtalisEventNode)); /* this temp event is the complex event of the embedded operation */
        TempEvent->event.arity = 0;

        strcpy(TempEvent->event.name,"temp_"); /* TODO Temp Events */ /* Temp Events have no arguments */
        TempEvent->parentNode = operatorNode;
        strcpy(TempEvent->parentNode->name,tt);
        TempEvent->is_temp = ETALIS_TRUE;
        operatorNode->leftChild = TempEvent;

        EtalisExecNode* NewNodeRule = (EtalisExecNode*)calloc(1,sizeof(EtalisExecNode)); /* binarization and allocation of the embedded operator */
        TempEvent->childNode  = NewNodeRule;
        NewNodeRule->parentEvent = TempEvent;
        TempEvent->trigger = _seq_win_cep_l;
        switch (CEP_LUT_[i].CEP_arity)
        {
        case 1:
            break;
        case 2:
            /* found embedded */
            CEP_LUT_[i].parser_func(NewNodeRule,_left_event);
            break;
        default:
            printf("error compiling the rules\n");
        }
    }
    else /* an atomic event has been found */
    {
        operatorNode->leftChild = (EtalisEventNode*) calloc(1,sizeof(EtalisEventNode));
        PL_get_name_arity(_left_event,&_left_event_name,(int *)&((operatorNode->leftChild)->event.arity));
        strcpy(operatorNode->leftChild->event.name,PL_atom_chars(_left_event_name));
        operatorNode->leftChild->childNode = NULL;
        operatorNode->leftChild->parentNode=operatorNode;
        operatorNode->leftChild->is_temp = ETALIS_FALSE;

        if (operatorNode->parentEvent->is_temp)
                strcat(operatorNode->parentEvent->event.name,operatorNode->leftChild->event.name);

        operatorNode->leftChild->trigger = _seq_win_cep_l;

    }

    PL_get_arg(2,t,_right_event);

    operatorNode->rightChild = (EtalisEventNode*) calloc(1,sizeof(EtalisEventNode));
    PL_get_name_arity(_right_event,&_right_event_name,(int *)&(operatorNode->rightChild->event.arity));
    strcpy(operatorNode->rightChild->event.name,PL_atom_chars(_right_event_name));
    operatorNode->rightChild->childNode = NULL;
    operatorNode->rightChild->parentNode = operatorNode;
    operatorNode->rightChild->is_temp = ETALIS_FALSE;
    PL_discard_foreign_frame(fid);

    if (operatorNode->parentEvent->is_temp)
    {
            strcat(operatorNode->parentEvent->event.name,operatorNode->rightChild->event.name);
            operatorNode->rightChild->trigger = _seq_win_cep_l;
    }
    else /* event is latest event in the call list */
    {
        operatorNode->rightChild->trigger = _seq_batch_r;
    }




    ;
}

/*
 * parse a where clause and add the information into the ExecNode.
 * TODO #hafsi#5#
 * TODO implement a non binary, rule wide WHERE clause parser and interpreter.
 *
 */
void parse_where_op_(EtalisExecNode* operatorNode, term_t t)
{
    /* find out whether a where clause is used */
    term_t constraints = PL_new_term_refs(3);
    atom_t wheref;
    int arr;
    ETALIS_BOOL where_available=0;


    PL_get_arg(1,t,constraints);
    PL_get_name_arity(constraints,&wheref,&arr);


    char* gg = (char*)malloc(sizeof(char)*256);
    memset(gg,0,256);
    gg = PL_atom_chars(wheref);

    if(!strcmp(gg,"wheref")) where_available = ETALIS_TRUE;

    if(where_available)
    {


        /* process where clause */

        /*

        A = eventClause(unlabeled, e2(_G321, _G322, _G323, _G324), withinop(wheref(seqf(a(_G321, _G322), d(_G323, _G324)), conditions), 2.0))

        */

        operatorNode->whereNode =(EtalisWhereNode*)malloc(sizeof(EtalisWhereNode));
        memset(operatorNode->whereNode,0,sizeof(EtalisWhereNode));

        term_t _where_level_1 = PL_new_term_refs(2);
        term_t  rule_gut_term = _where_level_1+1;
        term_t  constraints_term = _where_level_1+2;

        PL_get_arg(1,t,_where_level_1);

        PL_get_arg(1,_where_level_1,rule_gut_term);
        PL_get_arg(2,_where_level_1,constraints_term);

#ifdef DEBUG
        char* testing = (char*)malloc(sizeof(char)*256);
        char* args = (char*)malloc(sizeof(char)*256);
        *args="\0";
        memset(testing,0,256);

        int size_contraints,idx=0;

        PL_get_name_arity(constraints_term,NULL,&size_contraints);
        termToStringVerbatim(_where_level_1,testing,args);

        /*
       wheref(seqf(seqf(a(_G1776),b(_G1778)),c(_G1780)),,(>(_G1776,1),<(_G1778,2)))
        */
        int j=strlen(testing);
        int num_=0;
        for (j=strlen(testing)-2;j>0;j--)
        {

            if (testing[j] == ')') num_++;
            if (testing[j] == '(') num_--;
            /*printf("%c : %d : %d\n",testing[j],j,num_);*/
            if(num_ == 0 ) break;

        }

        char* real_constr = testing + j ;


        printf("--- WHERE Block detected | Constraints: %s \n",real_constr);

#endif



        operatorNode->has_condition=ETALIS_TRUE;
        }
    else /* no constraints are detected */
    {
        /*get atomic events of the operator*/

        /*

        A = eventClause(unlabeled, e2(_G321, _G322, _G323, _G324), withinop(seqf(a(_G321, _G322), d(_G323, _G324)), 2.0)).

        */

    operatorNode->has_condition=ETALIS_FALSE;
    }


}


/* parse the time window constraints */
void parse_within_op_(EtalisExecNode* operatorNode,term_t t)
{

    assert(operatorNode != NULL);

    fid_t fid = PL_open_foreign_frame();


    EtalisEventNode* opt_t = (EtalisEventNode*)malloc(2*sizeof(EtalisEventNode)); /* memory alignement for L1 cache optimization */
    operatorNode->leftChild=opt_t;
    memset(operatorNode->leftChild,0,sizeof(EtalisEventNode));
    operatorNode->rightChild=opt_t+1;
    operatorNode->condition=NULL;

    /* get window size */

    term_t winsize = PL_new_term_ref();
    PL_get_arg(2,t,winsize);

    WINDOW_SIZE_T i; /*get window size*/ /* depending on the target processor, this might be an int, a double or a structure. */ /* defined in WINDOW_SIZE_T : e_time.h */

#if PROCESSOR_SUPPORTS_DOUBLE ==1
    if (PL_term_type(winsize) == PL_FLOAT)
        PL_get_float(winsize,&i);
        else
        printf("ERROR: window Size must be a floating number ! \n");
#else /* we don't support double accuracy, fall back to int */
        PL_get_integer(winsize, &i);
#endif
    operatorNode->window_size=i;

#ifdef DEBUG
    printf("--- WITHIN Block detected | Window size: %f\n",i);
#endif










/*

    term_t _level_1 = PL_new_term_refs(3);
    term_t _left_event=_level_1+1;
    term_t _right_event=_level_1+2;
    atom_t _left_event_name,_right_event_name;




    PL_get_arg(1,t,_level_1);
    PL_get_arg(1,_level_1,_left_event);
    PL_get_arg(2,_level_1,_right_event);
    PL_get_name_arity(_left_event,&_left_event_name,(int*)&((operatorNode->leftChild)->event.arity));
    PL_get_name_arity(_right_event,&_right_event_name,(int*)&(operatorNode->rightChild->event.arity));

    PL_discard_foreign_frame(fid);
    strcpy(operatorNode->leftChild->event.name,PL_atom_chars(_left_event_name));
    strcpy(operatorNode->rightChild->event.name,PL_atom_chars(_right_event_name));
    }



*/



}
void default_op_parser(EtalisExecNode* operatorNode,term_t t)
{
    assert(operatorNode != NULL);

    PL_open_foreign_frame();

    operatorNode->leftChild=(EtalisEventNode*)malloc(sizeof(EtalisEventNode));
    operatorNode->rightChild=NULL;
    operatorNode->condition=NULL;
    operatorNode->window_size=0;


    term_t _left_event=t;
    atom_t _left_event_name;
    PL_get_name_arity(_left_event,&_left_event_name,&((operatorNode->leftChild)->event.arity));
    strcpy(operatorNode->leftChild->event.name,PL_atom_chars(_left_event_name));

}



/** Execution tree */
/* General structure of a rule :

[Rule_Label] ComplexEvent <- CEP_Clause [WHERE_Clause] [WITHIN_Clause]

*/
void construct_rule(EtalisBatch* batch,term_t term)
{

    EtalisExecNode* NodeRule = batch->nodes;
    assert(NodeRule != NULL);

    atom_t cep_name;
    int temp_arity;
    int i=0;
    int LUT_Size=2;



#ifdef DEBUG
    printf("--- Constructing rule: \n");
#endif




    /* WITHIN Clause */

    parse_within_op_(NodeRule,term); /*TODO Add a check that a within is explicitely stated in the rule */

    /* WHERE Clause */

    parse_where_op_(NodeRule,term);



    /* CEP Clause */

    term_t cep_term = PL_new_term_refs(2);
    if(NodeRule->has_condition == ETALIS_TRUE)
    {
        term_t first_level_term = PL_new_term_refs(2);
        PL_get_arg(1,term,first_level_term);
        PL_get_arg(1,first_level_term,cep_term);
    }
    else
        PL_get_arg(1,term,cep_term);
    PL_get_name_arity(cep_term, &cep_name, &temp_arity);
    char* aaa = PL_atom_chars(cep_name);

    /* find the right CEP operator */
    while(strcmp(CEP_LUT_[i].CEP_name,PL_atom_chars(cep_name)) != 0 && i<LUT_Size) i++;
    NodeRule->left_exec.exec_1=CEP_LUT_[i].exec1.exec_1;
    NodeRule->right_exec.exec_1=CEP_LUT_[i].exec2.exec_1;

    if(i != LUT_Size) /*The operator is found in the CEP_LUT_*/
    {
        switch (CEP_LUT_[i].CEP_arity)
        {
        case 1:
            NodeRule->op_type=unary;
            strcpy(NodeRule->name,CEP_LUT_[i].CEP_name);
            NodeRule->left_exec.exec_1=CEP_LUT_[i].exec1.exec_1;

            break;
        case 2:
            NodeRule->op_type=binary;
            strcpy(NodeRule->name,CEP_LUT_[i].CEP_name);
            NodeRule->left_exec.exec_1=CEP_LUT_[i].exec1.exec_1;
            NodeRule->right_exec.exec_1=CEP_LUT_[i].exec2.exec_1;
            CEP_LUT_[i].parser_func(NodeRule,cep_term);
            break;
        default:
            printf("error compiling the rules\n");
        }
    }
    else  /*no operator is found : identity operator is assumed*/
    {
        NodeRule->op_type=unary;
        strcpy(NodeRule->name,"identity");
        NodeRule->left_exec.exec_1=_cep_identity;
        default_op_parser(NodeRule,cep_term);
    }


    /* triggering an event should execute the correct function*/ /* deprecated */
    /*
    NodeRule->leftChild->trigger=NodeRule->left_exec.exec_1;
    if(NodeRule->op_type == binary)
        NodeRule->rightChild->trigger=NodeRule->right_exec.exec_1;
    */

    /* setting the tree connections between the events and the CEP operator */
    NodeRule->leftChild->parentNode=NodeRule;
    if(NodeRule->op_type == binary)
        NodeRule->rightChild->parentNode=NodeRule;


    /** Propagate the WHERE Clauses */
    where_binarization(NodeRule);


}

/* compile all rules */
EtalisExecTree* buildExecTree()
{
    printf("--- Generating Rule Tree ...\n");
    EtalisExecTree* tree = calloc(1,sizeof(EtalisExecTree));
    tree->size=3; /* TODO (hafsi#4#): fixme */ /*if more than one rule, find out how many complex events*/
    tree->exec=print_event;
    tree->complexEvents = (EtalisExecNode*)calloc(tree->size,sizeof(EtalisExecNode));

    EtalisBatch* temp_batch = (EtalisBatch*)malloc(sizeof(EtalisBatch));

    int i=0;

    static predicate_t p;
    term_t _args_binary_event_rule = PL_new_term_refs(3);
    atom_t name;
    int temp_arity;

    if ( !p )
        p = PL_predicate("binary_event_rule", 3, NULL);

    qid_t qid = PL_open_query(NULL, PL_Q_NORMAL, p, _args_binary_event_rule);

    while(PL_next_solution(qid) != FALSE)
    {

        EtalisEventNode* temp_event = tree->complexEvents+i; /* next complex event */
        EtalisExecNode* temp_operator =(EtalisExecNode*)malloc(sizeof(EtalisExecNode));
        memset(temp_operator,0,sizeof(EtalisExecNode));

        assert( temp_event != NULL && temp_operator != NULL);

        temp_event->parentNode=NULL;          /*a complex event does not have a parent*/
        temp_event->childNode=temp_operator;
        temp_event->trigger=_cep_print_event; /* by default, triggering a complx event would print it */

        temp_operator->parentEvent=temp_event;

        temp_batch->batchSize=1;
        temp_batch->nodes=temp_operator;


        /*get label*/
        PL_get_name_arity(_args_binary_event_rule, &name, &temp_arity);
        strcpy(temp_batch->label,PL_atom_chars(name));
        /*get complex event*/
        PL_get_name_arity(_args_binary_event_rule+1, &name, &temp_arity);
        strcpy(temp_event->event.name,PL_atom_chars(name));
        temp_event->event.arity = temp_arity;

        /*get rule*/
        construct_rule(temp_batch,_args_binary_event_rule+2);

        /* init a stack for each event*/

        /* query the tree in the depth */
        EtalisEventNode* temp_event_index = temp_operator->leftChild;
        for (temp_event_index = temp_operator->leftChild;temp_event_index->childNode != NULL;temp_event_index = temp_event_index->childNode->leftChild)
        {
            temp_event_index->eventStack = StackCreate();
            if(temp_event_index->parentNode->op_type == binary)
                temp_event_index->parentNode->rightChild->eventStack = StackCreate();
        }
        /* Create stack for leaf nodes*/
        temp_event_index->eventStack = StackCreate();
            if(temp_event_index->parentNode->op_type == binary)
                temp_event_index->parentNode->rightChild->eventStack = StackCreate();



        /* build argument logical models */

/*
        if(temp_operator->has_condition == ETALIS_TRUE)
            {
                    ;
            build_args_map(temp_operator,_args_binary_event_rule+1,_args_binary_event_rule+2);

            }
            else
            {

            build_args_map(temp_operator,_args_binary_event_rule+1,_args_binary_event_rule+2);
            }

*/

        /*print the rule*/ /* only if debugging */

        #ifdef DEBUG
        /*print_rule(temp_event);*/
        #endif

        /*add to event hash*/
        addToEventHash(temp_operator);


        i++; /*next rule*/
    };

    PL_close_query(qid);

    /*from the rules build the tree*/


    printf("--- Done!\n");
    return tree;
}

void exec_tr_make_new_left_child(EtalisExecNode* subNode)
{
    subNode->leftChild = (EtalisExecNode*)malloc(sizeof(EtalisExecNode));
}


void exec_tr_make_new_right_child(EtalisExecNode* subNode)
{
    subNode->rightChild = (EtalisExecNode*)malloc(sizeof(EtalisExecNode));
}

void print_rule(const EtalisEventNode* temp_event)
{
    padding(' ',4) ;
    printf("New Rule ->\n");
    padding(' ',14);
    printf("+Label: %s\n",temp_event->childNode->label);
    padding(' ',14);
    printf("+Complex Event: %s/%d\n",temp_event->event.name,temp_event->event.arity);
    padding(' ',14);
    printf("+Final Operator: %s\n",temp_event->childNode->name);
    if(temp_event->childNode->condition != NULL)
    {
        padding(' ',14);
        printf("+Node Condition: %s\n",temp_event->childNode->condition);
    }
    if(temp_event->childNode->window_size != 0)
    {
        padding(' ',14);
        printf("+Sliding Window Size: %f sec\n",temp_event->childNode->window_size);
    }
    else
    {
        padding(' ',14);
        printf("+Sliding Window Size: Unlimited\n");
    }
    padding(' ',14);
    printf("++@l-Event: %s/%d\n",temp_event->childNode->leftChild->event.name,temp_event->childNode->leftChild->event.arity);
    if(temp_event->childNode->op_type == binary)
    {
        padding(' ',14);
        printf("++@r-Event: %s/%d\n",temp_event->childNode->rightChild->event.name,temp_event->childNode->rightChild->event.arity);
    }

}

void print_event(char* event)
{
    printf("-> %s\n",event);
}


void initEventHash(eventHash* events)
{
    events=NULL;
}

/*
 * addToEventHash
 * Update the _event_hash when new events are found in a rule.
 * Arguments : rule_
 * an EtalisExecNode that represents an Etalis binary rule.
 */
/* TODO (hafsi#2#)
 *   - when more than one event is associated with a rule, add them all into hash.
 *   - when a complex event has the same signature as an atomic event, this should be
 *   resolved.
 *  Note : memset only once when creating the Nodes ! memsetting the struct again would destroy the associated data !
 */
void addToEventHash(EtalisExecNode* rule_)
{
    assert(rule_ != NULL);

    /* add complex event to hash*/
    if(rule_->parentEvent != NULL)
    {
        eventHash* complex_event=NULL;
        complex_event = (eventHash*)malloc(sizeof(eventHash));
        memset(complex_event,0,sizeof(eventHash));
        complex_event->ev.arity=rule_->parentEvent->event.arity;
        strcpy(complex_event->ev.name,rule_->parentEvent->event.name);
        complex_event->myNode=rule_->parentEvent;
        HASH_ADD(hh,_event_hash,ev,sizeof(eventType),complex_event);
        printf("--- EventHash: Adding complex Event %s/%d\n",complex_event->ev.name,complex_event->ev.arity);

    }

    if (rule_->leftChild->is_temp) /* Follow the operator chaining of the rule */
    {
        addToEventHash(rule_->leftChild->childNode);

    }
    else /* add left event to hash*/
    {

        eventHash* temp_event_left=NULL;

        temp_event_left = (eventHash*)malloc(sizeof(eventHash));
        memset(temp_event_left,0,sizeof(eventHash));

        temp_event_left->ev.arity = rule_->leftChild->event.arity;
        strcpy(temp_event_left->ev.name,rule_->leftChild->event.name);
        temp_event_left->myNode = rule_->leftChild;

        HASH_ADD(hh,_event_hash,ev,sizeof(eventType),temp_event_left);
        printf("--- EventHash: Adding %s/%d\n",temp_event_left->ev.name,temp_event_left->ev.arity);
    }
    /* add right event to hash*/
    if(rule_->op_type == binary)
    {
        eventHash* temp_event_right=NULL;
        temp_event_right = (eventHash*)malloc(sizeof(eventHash));
        memset(temp_event_right,0,sizeof(eventHash));

        temp_event_right->ev.arity=rule_->rightChild->event.arity;
        strcpy(temp_event_right->ev.name,rule_->rightChild->event.name);
        temp_event_right->myNode=rule_->rightChild;

        HASH_ADD(hh,_event_hash,ev,sizeof(eventType),temp_event_right);
        printf("--- EventHash: Adding %s/%d\n",temp_event_right->ev.name,temp_event_right->ev.arity);
    }


}

/*
 * get the name and arity of an SWI Event.
 */
eventType* getEventType(term_t event_term)
{
    atom_t name;
    int arity;
    char* eventName = (char*)malloc(256*sizeof(char));
    eventType* ev = (eventType*)malloc(sizeof(eventType));
    PL_get_name_arity(event_term,&name,&arity);
    eventName = PL_atom_chars(name);

    strcpy(ev->name,eventName);
    ev->arity=arity;
    return ev;
}
/*
 * Trigger an Event with explicit timestamp TODO hafsi#6#
 */
foreign_t triggerEvent(term_t args,term_t time)
{
    PL_succeed;
}
/*
 * Trigger an event without explicitely defining timestamps
 */
foreign_t triggerEvent_u(term_t args)
{
    atom_t name;
    int arity=0;
    PL_get_name_arity(args,&name,&arity);

    /* find from _event_hash the EventModel to follow */

    eventHash tempEventHash;
    eventHash* foundEventNode = NULL;
    memset(&tempEventHash,0,sizeof(eventHash));
    tempEventHash.ev.arity=arity;
    strcpy(tempEventHash.ev.name,PL_atom_chars(name));

    HASH_FIND(hh,_event_hash,&tempEventHash.ev,sizeof(eventType),foundEventNode);



    /* if EventModel is in the Hash */
    if(foundEventNode != NULL)
    {

        EtalisEvent *myEvent=(EtalisEvent*)malloc(sizeof(EtalisEvent));
        myEvent->RootModel=foundEventNode->myNode;

        if (arity != 0)
            parse_validate_args(args,myEvent);

        newTimeStamp(&myEvent->timestamps[0]);
        myEvent->timestamps[1] = myEvent->timestamps[0];

        /* debg models */
        #ifdef DEBUG
        EtalisEventNode *myModel = myEvent->RootModel;
        EtalisExecNode  *myExec  = myModel->parentNode;
        #endif
        /* f dbg */


        debug("--- Triggering event ... %s/%d @ [%ld.%06ld,%ld.%06ld]\n",myEvent->RootModel->event.name,myEvent->RootModel->event.arity,myEvent->timestamps[0].time,myEvent->timestamps[0].parts,myEvent->timestamps[1].time,myEvent->timestamps[1].parts);

        foundEventNode->myNode->trigger(myEvent);
    }

    PL_succeed;
}

void triggerEvent_intern_no_hash(EtalisEvent* event)
{
    if (event != NULL)
        {
            debug("--- Triggering event ... %s/%d @ [%ld.%06ld,%ld.%06ld]\n",event->RootModel->event.name,event->RootModel->event.arity,event->timestamps[0].time,event->timestamps[0].parts,event->timestamps[1].time,event->timestamps[1].parts);
            event->RootModel->trigger(event);
        }
        else
            log_err("An Error has occured !");
}


void triggerEvent_intern_hash(eventType* key, EtalisEvent* event)
{
    /* find from _event_hash the EventModel to follow */

    eventHash tempEventHash;
    eventHash* foundEventNode = NULL;

    memset(&tempEventHash,0,sizeof(eventHash));
    tempEventHash.ev.arity=key->arity;
    strcpy(tempEventHash.ev.name,key->name);

    HASH_FIND(hh,_event_hash,&tempEventHash.ev,sizeof(eventType),foundEventNode);

    /* if EventModel is in the Hash */
    if(foundEventNode != NULL)
    {

        event->RootModel=foundEventNode->myNode;


        newTimeStamp(&event->timestamps[0]);
        event->timestamps[1] = event->timestamps[0];




        debug("--- Triggering event ... %s/%d @ [%ld.%06ld,%ld.%06ld]\n",event->RootModel->event.name,event->RootModel->event.arity,event->timestamps[0].time,event->timestamps[0].parts,event->timestamps[1].time,event->timestamps[1].parts);

        foundEventNode->myNode->trigger(event);
    }


}
EtalisEventNode* find_event_model_in_hash(eventType* key)
{
    /* find from _event_hash the EventModel to follow */

    eventHash tempEventHash;
    eventHash* foundEventNode = NULL;

    memset(&tempEventHash,0,sizeof(eventHash));
    tempEventHash.ev.arity=key->arity;
    strcpy(tempEventHash.ev.name,key->name);

    HASH_FIND(hh,_event_hash,&tempEventHash.ev,sizeof(eventType),foundEventNode);
    return (foundEventNode != NULL ? foundEventNode->myNode : NULL);  /* return NULL if node is not found */

}






/*
    build arguments' internal interrelations
*/
void build_args_map(EtalisExecNode* Node,term_t lhs,term_t rhs)
{
    /* assuming that all arguments are int. Further Datatypes will be supported in the future. */

    /* Arity of all the involved events */
    /* Note : need to test if this is more efficient than directly calling the struct's members*/



    int arg_count = Node->leftChild->event.arity+Node->rightChild->event.arity+Node->parentEvent->event.arity;
    int arg_iterator=0;

    char* arguments=calloc(arg_count*256,sizeof(char));

    char Goal[256];
    char Goal2[256];
    char WhereGoal[256];

    termToString(rhs,Goal2,arguments);
    termToString(lhs,Goal,arguments);

    char* arguments_cond=calloc(arg_count*256,sizeof(char));
    termToStringVerbatim(rhs,WhereGoal,arguments_cond);

    int extra_args=0; /* used if a where clause is available */

    char* orig_args=arguments;

    if(Node->has_condition == ETALIS_TRUE)
    {

        while(*arguments)
        {
            if(*arguments == '_')
                extra_args++;
            arguments++;
        }

        extra_args = extra_args - arg_count;


    arguments=orig_args;

    char * whereRightBound;
    char * whereLeftBound;

    whereRightBound = WhereGoal + strlen(WhereGoal);
    while(*whereRightBound != ',')
        whereRightBound--;

    whereRightBound--;

    whereLeftBound =  strstr(WhereGoal,"wheref");
    whereLeftBound = whereLeftBound+7;

    int leftParenth=0, rightParenth=0;

    while(*whereLeftBound != '\0')
    {
        if(*whereLeftBound == ')')
            rightParenth++;
        if(*whereLeftBound == '(')
            leftParenth++;
        if(leftParenth == rightParenth && leftParenth != 0)
                break;
        whereLeftBound++;
    }
    whereLeftBound=whereLeftBound+2;

    char* constraints_verbatim = malloc(256*sizeof(char));

    strncpy(constraints_verbatim,whereLeftBound,strlen(whereLeftBound)-strlen(whereRightBound));


    Node->whereNode->conditions=constraints_verbatim;

    printf("CONSTRAINTS: %s\n",constraints_verbatim);



    }


    #ifdef DEBUG
    printf("rule args : %s\n",arguments);
    #endif

    char** argument_array = malloc((arg_count+extra_args)*sizeof(char*));

    char* pch;
    pch = strtok(arguments,"_");
    while(pch != NULL)
    {


        argument_array[arg_iterator] = malloc(sizeof(char)*strlen(pch));
        strcpy(argument_array[arg_iterator],pch);
        arg_iterator++;
        pch = strtok(NULL,"_");
    }

    #ifdef DEBUG
    for(arg_iterator=0;arg_iterator<(arg_count+extra_args);arg_iterator++)
        printf("arg[%d] : %s\n",arg_iterator,argument_array[arg_iterator]);
    #endif

    argument_link* temp_arg =NULL;
    size_t left_s = Node->leftChild->event.arity;
    size_t right_s = Node->rightChild->event.arity;
    size_t cplx_s = Node->parentEvent->event.arity;

    /* handling of left event args : Usually these are unbound */

    Node->leftChild->arg_links = calloc(Node->leftChild->event.arity,sizeof(argument_link));
    temp_arg = Node->leftChild->arg_links;
    for(arg_iterator=0;arg_iterator<(Node->leftChild->event.arity);arg_iterator++)
        {
            (temp_arg+arg_iterator)->event_= UNBOUND_ARGUMENT;
            (temp_arg+arg_iterator)->argument_number = UNBOUND_ARGUMENT;
        }


    /* handling of right event args */ /* todo check for the unbound event case */

    Node->rightChild->arg_links = calloc(Node->rightChild->event.arity,sizeof(argument_link));
    temp_arg = Node->rightChild->arg_links;
    for(arg_iterator=0;arg_iterator<(Node->rightChild->event.arity);arg_iterator++)
        {
            int pos=-1;
            pos = findInStringArray(0,left_s,argument_array,argument_array[left_s+arg_iterator]);

            (temp_arg+arg_iterator)->event_= (pos == -1 ? UNBOUND_ARGUMENT : 1 );
            (temp_arg+arg_iterator)->argument_number = (pos == -1 ? UNBOUND_ARGUMENT : pos );
        }

    #ifdef DEBUG
     for(arg_iterator=0;arg_iterator<right_s;arg_iterator++)
            printf("ARG[%d] of right event is bound to Event %d : arg [%d]\n",arg_iterator,Node->rightChild->arg_links[arg_iterator].event_,Node->rightChild->arg_links[arg_iterator].argument_number);
    #endif

    /* handling of additional clauses : where, etc ..*/

    if(Node->has_condition == ETALIS_TRUE)
    {
        Node->whereNode->arg_links=(argument_link*)malloc(sizeof(argument_link)*extra_args);
        /* search for the conditions in the events */
        temp_arg = Node->whereNode->arg_links;
        for(arg_iterator=0;arg_iterator<extra_args;arg_iterator++)
        {
            int pos=-1;
            pos= findInStringArray(0,left_s,argument_array,argument_array[left_s+right_s+arg_iterator]);

            if (pos == -1) /* Unbound event */
            {
                (temp_arg+arg_iterator)->event_= UNBOUND_ARGUMENT;
                (temp_arg+arg_iterator)->argument_number = UNBOUND_ARGUMENT;
                continue;
            }
            else
            {
                (temp_arg+arg_iterator)->event_= 1;
                (temp_arg+arg_iterator)->argument_number = pos;
                continue;
            }
            /* this should be fixed */
            pos= findInStringArray(left_s,right_s+left_s,argument_array,argument_array[left_s+right_s+arg_iterator]);

        }


    }





    /* handling of complex event args */

    Node->parentEvent->arg_links = (argument_link*)malloc(Node->parentEvent->event.arity*sizeof(argument_link));
    temp_arg = Node->parentEvent->arg_links;
    for(arg_iterator=0;arg_iterator<(Node->parentEvent->event.arity);arg_iterator++)
        {
            int pos=-1;
            pos = findInStringArray(0,left_s+right_s,argument_array,argument_array[extra_args+left_s+right_s+arg_iterator]);

            if (pos == -1) /* Unbound event */
            {
                (temp_arg+arg_iterator)->event_= UNBOUND_ARGUMENT;
                (temp_arg+arg_iterator)->argument_number = UNBOUND_ARGUMENT;
                continue;
            }
            else if (pos <left_s)
                {
                        (temp_arg+arg_iterator)->event_=1;
                        (temp_arg+arg_iterator)->argument_number = pos;
                        continue;
                }
                else if (pos-left_s < right_s)
                    {
                        (temp_arg+arg_iterator)->event_=2;
                        (temp_arg+arg_iterator)->argument_number = pos-left_s; /* todo correct offset */
                        continue;
                    }
        }

    #ifdef DEBUG
     for(arg_iterator=0;arg_iterator<cplx_s;arg_iterator++)
            printf("ARG[%d] of complex event is bound to Event %d : arg [%d]\n",arg_iterator,Node->parentEvent->arg_links[arg_iterator].event_,Node->parentEvent->arg_links[arg_iterator].argument_number);
    #endif




    /* free temporary data */

    /* todo fixme win7 Heap FTH : look here : http://stackoverflow.com/questions/1621059/breakpoints-out-of-nowhere-when-debugging-with-gdb-inside-ntdll */

/*
    for(arg_iterator=0;arg_iterator<arg_count;arg_iterator++)
        free(argument_array[arg_iterator]);
*/

    free(argument_array);
    free(arguments);



    return;

}
/* parse event arguments of an ETALIS Prolog Event : A priori conditions are validated while parsing */
/* todo */
void parse_validate_args(term_t args, EtalisEvent* event)
{
    event->args = malloc(sizeof(int)*event->RootModel->event.arity);
    int arity;
    PL_get_name_arity(args, NULL, &arity);
    term_t arg_terms = PL_new_term_refs(arity);
    /* assuming that all arguments are ints */ /* todo implement for other types */


    size_t arg_iterator;
    for(arg_iterator=0;arg_iterator<event->RootModel->event.arity;arg_iterator++)
    {
        PL_get_arg(arg_iterator+1,args,arg_terms+arg_iterator);
        PL_get_integer(arg_terms+arg_iterator,(int*)event->args+arg_iterator);

    }

}

ETALIS_BOOL constraint_fullfilled(char *constraint,EtalisEventNode * Ev)
{
    return ETALIS_TRUE;
}


ETALIS_BOOL constraint_par_fullfilled(char *constraint,EtalisEventNode * Ev)
{
    return ETALIS_TRUE;
}

void assign_relevant_constraints(EtalisExecNode* OperatorNode, EtalisEventNode* CurrentEvent)
{

}
void delete_constraint(EtalisWhereNode* WH_node, int pos)
{

}


void where_binarization(EtalisExecNode* OperatorNode)
{
        EtalisEventNode* tempEvent = OperatorNode->leftChild;
        EtalisExecNode*  tempOperator = OperatorNode;


        /* find left most event in the tree */
        for (tempEvent=OperatorNode->leftChild;tempEvent->childNode != NULL; tempEvent = tempEvent->childNode->leftChild);

        do
        {
            int i=0;
            for (i = 0; i < OperatorNode->whereNode->size; i++) /* for all constraints */
            {
                if (constraint_fullfilled(OperatorNode->whereNode->conditions[i],tempEvent)) {tempEvent->whereNode = OperatorNode->whereNode; continue;}
                else if (constraint_par_fullfilled(OperatorNode->whereNode->conditions[i], tempEvent))
                {
                    assign_relevant_constraints(OperatorNode,tempEvent); /* 1) assign left || right constraints 2) propagate relevant constraints to the upper level */

                }
                delete_constraint(OperatorNode->whereNode,i);
            }
            if (tempEvent == tempEvent->parentNode->leftChild) /* if event is left event ; navigate to right event*/
                tempEvent = tempEvent->parentNode->rightChild;
                else
                tempEvent = tempEvent->parentNode->parentEvent;


        }while (tempEvent->parentNode != NULL);


}


/* validate arguemnts of general ETALIS events (not originating from prolog)
 * goal: optimize speed
 */
int validate_arguments(EtalisEvent* a, EtalisEvent* b) /* todo unit test / validate */
{
    return ETALIS_OK; /* TODO fix */
    EtalisEventNode Model_a = *(a->RootModel);
    EtalisEventNode Model_b = *(b->RootModel);
    size_t size_b = Model_b.event.arity;

    size_t arg_iter = 0;

    /* validate with other events on implicit data */
    for (arg_iter=0;arg_iter<size_b;arg_iter++)
    {
        int* arg_b = (int*)(b->args)+arg_iter;
        int* arg_a = (int*)(a->args);

        int arg_a_to_validate_against = Model_b.arg_links[arg_iter].argument_number;

        if(Model_b.arg_links[arg_iter].event_ == UNBOUND_ARGUMENT)
            continue;

        if(*(arg_b) != *(arg_a+arg_a_to_validate_against))
            return ETALIS_ERR;
    }
    /* validate on any where clauses */
    if(Model_a.parentNode->has_condition == ETALIS_TRUE)
    {
         char* conds = strcat(Model_a.parentNode->whereNode->conditions,"\n\0\0");
         printf("current conditions: %s \n",conds);
         printf("current links: %d \n",Model_a.parentNode->whereNode->arg_links->event_);
         yy_scan_string(conds);
         yyparse();

    }

    return ETALIS_OK;
}

int aprio_validation(EtalisEvent* Event_b)
{

    int arg_i = Event_b->RootModel->event.arity;
    int* arg = (int*)Event_b->args;
    /* a priori validation is removed after adding general conditions */
    return ETALIS_OK;

}

/* Bind the arguments of the complex event
 * */
void process_event_args(EtalisEvent* Cplx)
{
    /* 1) we might inherit arguments from child events.
     * 2) we might inherit arguments from a 'do' block.
     */






    return;
}

#ifdef USE_RB_TREES
/*
 * An old implementation for event input distribution that is replaced with a hashtable.
 * this macro is usually not defined, and this code is never compiled.
 */
extern rb_red_blk_tree* _event_map;

rb_red_blk_tree* buildEventMap()
{
    printf("--- Generating Event Rule Map ... \n");
    rb_red_blk_tree* EventTree = RBTreeCreate(Compare_EventType,DestroyEventType,DestroyInfoEventKey,PrintEventKey,PrintInfoEventKey);

    if(!EventTree)
    {
        printf("Error Building the Event Rule Map.\n");
        return NULL;
    }

    int i=0;
    term_t a0 = PL_new_term_refs(3);
    term_t b0 = PL_new_term_refs(2);

    static predicate_t p;
    static functor_t event_functor;

    char myEvents[256][256];
    int  arity;
    eventType* temp=NULL;

    if ( !event_functor )
        event_functor = PL_new_functor(PL_new_atom("event"), 2);
    PL_cons_functor(a0+1,event_functor,b0,b0+1);

    if ( !p )
        p = PL_predicate("trClause", 3, NULL);

    qid_t qid = PL_open_query(NULL, PL_Q_NORMAL, p, a0);
    while(PL_next_solution(qid) != FALSE)
    {
        //termToString(b0,myEvents[i]);
        atom_t name;
        PL_get_name_arity(b0, &name, &arity);
        sprintf(myEvents[i],"%s",PL_atom_chars(name));
        temp=(eventType*)calloc(1,sizeof(eventType));
        trClause* trc=(trClause*)calloc(1,sizeof(trClause));

        strcpy(temp->name,PL_atom_chars(name));
        temp->arity = arity;
        RBTreeInsert(EventTree,temp,trc);
        temp=NULL;
        trc=NULL;
        padding(' ',4);
        printf("+New Event Signature : %s/%d\n",myEvents[i],arity);
        i++;
    }
    PL_close_query(qid);
#if DEBUG
    RBTreePrint(EventTree);
#endif
    printf("--- Done!\n");


    return EventTree;
}
int Compare_EventType    (const void* a, const void* b)
{
    return str_alp(((eventType*)a)->name,((eventType*)b)->name);
}
void DestroyEventType    (void* a)
{
    free((eventType*)a);
}
void DestroyInfoEventKey (void* a)
{
    ;
}
void PrintEventKey       (const void* a)
{
    printf(" %s/%d\n",((eventType*)a)->name,((eventType*)a)->arity);
};
void PrintInfoEventKey   (void* a)
{
    ;
}
#endif /* end def USE_RB_TREES*/
