#include <SWI-Prolog.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>


#include "ETALIS_conf.h"
#include "memory.h"

foreign_t
docs(void)
{
    printf("Storage : \n");
    printf("create_db/0\n");
    printf("ins_dbf/2\n");
    printf("del_dbf/2\n");
    printf("check_dbf/3\n");

    PL_succeed;
}
int
consultPrologFile(char* cmd)
{

    static predicate_t p;
    int rval;
    if ( !p )
        p = PL_predicate("consult", 1, NULL);
     term_t a0 = PL_new_term_refs(1);


    PL_put_atom_chars(a0,cmd);
    rval = PL_call_predicate(NULL, PL_Q_NORMAL, p, a0);



    return (rval ? 0 : 1);
}

foreign_t
pl_display(term_t t)
{
  int len, n;
  char *s;

  switch( PL_term_type(t) )
  { case PL_VARIABLE:
    case PL_ATOM:
    case PL_INTEGER:
    case PL_FLOAT:
      PL_get_chars(t, &s, CVT_ALL);
      Sprintf("%s", s);
      break;
    case PL_STRING:
      PL_get_string_chars(t, &s, &len);
      Sprintf("\"%s\"", s);
      break;
    case PL_TERM:
    { term_t a = PL_new_term_ref();
        atom_t name;
        int arity;
      PL_get_name_arity(t, &name, &arity);
      Sprintf("%s(", PL_atom_chars(name));
      for(n=1; n<=arity; n++)
      { PL_get_arg(n, t, a);
        if ( n > 1 )
          Sprintf(", ");
        pl_display(a);
      }
      Sprintf(")");
      break;
    default:
      PL_fail;                          /* should not happen */
    }
  }

  PL_succeed;
}

/*Utility function : compare 2 strings, 0 if equal, 1 if s1 is bigger*/
int
str_alp(char *s1,char *s2) {
    if(strcmp(s1,s2)==0)return(0);   /*if they are equal*/
        int i=0;
        for(i=0;s1[i]!=0;i++)
        {
              if(s1[i] > s2[i])return(1);
              else if(s1[i] < s2[i])return(-1);
        }

    return (-1);
}

void
print_banner() {
     printf("\n");
     printf("#######  #######     #     #         #####    #####  \n");
     printf("#           #        #     #           #     #     # \n");
     printf("#           #       ###    #           #     #       \n");
     printf("#####       #       # #    #           #      #####  \n");
     printf("#           #      #####   #           #           # \n");
     printf("#           #      #   #   #           #     #     # \n");
     printf("#######     #     ##   ##  ######    #####    #####  \n");
     printf("\n");


     printf("Welcome to embedded ETALIS (Single Threaded, 32 bits, Version ");printf(VERSION_ee);printf(")\n");
     printf("Copyright (c) 2008-2012 Forschungszentrum Informatik, Karlsruhe Germany.\n");
     printf("Copyright (c) 2008-2012 Ahmed Khalil Hafsi, Darko Anicic.\n");

     printf("ETALIS comes with ABSOLUTELY NO WARRANTY. This is free software,\nand you are welcome to redistribute it under certain conditions.\n");
     printf("Please visit http://www.etalis.org for details.\n\n");

}

void padding ( char ch, int n )
{
  int i;

  for ( i = 0; i < n; i++ )
    putchar ( ch );
}

void viewEventHashTable(void)
{

}
/* size_t to : must be inside the array */
int findInStringArray(size_t frm,size_t to,char** arr,char*str_to_be_found)
{
    size_t i=frm;
    while (i < to)
        {
            if (!strcmp(arr[i],str_to_be_found))
                return i;
            else
                i++;
        }
        return -1;
}
