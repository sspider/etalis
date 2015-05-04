#include "term.h"
#include <stdio.h>

/**
term.c
Term based manipulations
@Author : Ahmed Khalil Hafsi

*/


void
GoalToterm(term_t t, char* myGoal)
{

    char** pch = (char*)malloc(256*sizeof(char*));
    pch[0] = strtok(myGoal,"(),.");
    int i=0;
    while(pch[i] != NULL)
    {

        printf("Token : %s\n",pch[i]);
        i++;
        pch[i] = strtok(NULL,"(),.");

    };

    functor_t f = PL_new_functor(PL_new_atom("goal"), 3);
    term_t a1 = PL_new_term_refs(3);
    PL_put_atom_chars(a1,pch[1]);
    PL_put_atom_chars(a1+1,pch[2]);
    PL_put_atom_chars(a1+2,pch[3]);
    PL_cons_functor_v(t,f,a1);
}

/*myGoal must be allocated before this function is called.*/
void
termToString(term_t t, char* myGoal,char* args)
{
  *myGoal='\0';

  int len, n;
  char *s=NULL;
  char*myVar=calloc(256,1);

  switch( PL_term_type(t) )
  { case PL_VARIABLE:
            *myVar='\0'; PL_get_chars(t,&myVar,CVT_VARIABLE); strcat(args,myVar);break;
    case PL_ATOM:
    case PL_INTEGER:
    case PL_FLOAT:
      PL_get_chars(t, &s, CVT_ALL);
      strcat(myGoal,s);
      break;
    case PL_STRING:
      PL_get_string_chars(t, &s, &len);
      sprintf(myGoal,"%s\"%s\"",myGoal, s);
      break;
    case PL_TERM:
    {
     term_t a = PL_new_term_ref();
        atom_t name;
        int arity;
      PL_get_name_arity(t, &name, &arity);

      sprintf(myGoal,"%s(",PL_atom_chars(name));
      for(n=1; n<=arity; n++)
      { PL_get_arg(n, t, a);

        if ( n > 1 )
            strcat(myGoal,",");

        char* temp = (char*)malloc(256*sizeof(char));
        *temp='\0';
        termToString(a,temp,args);

        strcat(myGoal,temp);
        free(myVar);
        free(temp);





      }
      sprintf(myGoal,"%s)",myGoal);

      break;
    default:
        return;
  }

  }

  return;

}

/* a Verbatim copy of the term */
void termToStringVerbatim(term_t t, char* myGoal,char* args)
{
          *myGoal='\0';

  int len, n;
  char *s=NULL;
  char*myVar=(char*)malloc(sizeof(char)*256);

  switch( PL_term_type(t) )
  { case PL_VARIABLE:
            *myVar='\0'; PL_get_chars(t,&myVar,CVT_VARIABLE); strcat(args,myVar);sprintf(myGoal,"%s",myVar);break;
    case PL_ATOM:
    case PL_INTEGER:
    case PL_FLOAT:
      PL_get_chars(t, &s, CVT_ALL);
      strcat(myGoal,s);
      break;
    case PL_STRING:
      PL_get_string_chars(t, &s, &len);
      sprintf(myGoal,"%s\"%s\"",myGoal, s);
      break;
    case PL_TERM:
    {
     term_t a = PL_new_term_ref();
        atom_t name;
        int arity;
      PL_get_name_arity(t, &name, &arity);

      sprintf(myGoal,"%s(",PL_atom_chars(name));

      char* temp = (char*)malloc(256*sizeof(char));

      for(n=1; n<=arity; n++)
      { PL_get_arg(n, t, a);

        if ( n > 1 )
            strcat(myGoal,",");

        *temp='\0';
        termToStringVerbatim(a,temp,args);

        strcat(myGoal,temp);
        /*free(myVar);*/
        /*free(temp);*/
      }
      sprintf(myGoal,"%s)",myGoal);

      break;
    default:
        return;
  }

  }

  return;
}

/* a term2String that ignores variables */
void termToString_(term_t t, char* myGoal)
{

  *myGoal='\0';
  functor_t functor;
  int arity, len, n;
  char *s=NULL;

  switch( PL_term_type(t) )
  { case PL_VARIABLE:
    break;
    case PL_ATOM:
    case PL_INTEGER:
    case PL_FLOAT:
      PL_get_chars(t, &s, CVT_ALL);
      strcat(myGoal,s);
      break;
    case PL_STRING:
      PL_get_string_chars(t, &s, &len);
      sprintf(myGoal,"%s\"%s\"",myGoal, s);
      break;
    case PL_TERM:
    {
     term_t a = PL_new_term_ref();
        atom_t name;
        int arity;
      PL_get_name_arity(t, &name, &arity);

      sprintf(myGoal,"%s(",PL_atom_chars(name));
      for(n=1; n<=arity; n++)
      { PL_get_arg(n, t, a);

        if ( n > 1 )
            strcat(myGoal,",");

        char* temp = (char*)malloc(256*sizeof(char));
        *temp='\0';
        termToString_(a,temp);

        strcat(myGoal,temp);

        free(temp);





      }


      break;
    default:
        return;
  }

  }

  return;

}
