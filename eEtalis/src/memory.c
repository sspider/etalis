/**
ETALIS Extended Memory Management Unit v1.0
@Author Ahmed Khalil Hafsi
Forschungszentrum Informatik Karlsruhe 2012

Build Instructrions:

- Include dir = %SWI_HOME_DIR/Include%
- Link with SwiLib.dll

Common errors:
SWI could not find resources.
Solution : link the saved state with plld or add the SWI_HOME_DIR into the envirement.
*/

#include <SWI-Prolog.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <assert.h>


#include "storage_conf.h"
#include "memory.h"

#ifdef EXTRA_PROLOG
#include "red_black_tree.h"

extern void termToString_(term_t,char*);
extern void StringToterm(term_t,char*);

rb_red_blk_tree* etalis_gt;

stk_stack* event_stack;


foreign_t
make_buffer(void){
event_stack = StackCreate();
return (event_stack) ? TRUE : FALSE;
}

foreign_t
push_b(term_t t)
{
    if (!event_stack) {printf("push_b : Error\n");return FALSE;};

    char* myGoal=(char*)malloc(256*sizeof(char));
    termToString_(t,myGoal);
    printf("push : %s\n",myGoal);
    StackPush(event_stack,(char*)myGoal);
    return TRUE;
}
foreign_t
pop_b(term_t t)
{
    /*TODO : memory check */
    term_t tmp = PL_new_term_ref();
    char* out = (char*)StackPop(event_stack);
    printf("pop : %s\n",out);
    GoalToterm(tmp,out);
    PL_unify(t,tmp);
    free(out);

    return TRUE;
}

int GoalCmp(const void* a,const void* b) {
    return PL_compare(*(term_t*)a,*(term_t*)b);

}
void GoalDel(void* a) {

    free((term_t*)a);
}
void GoalPrint(void* a) {
 char* myTerm=(char*)malloc(256*sizeof(char));
 termToString_(*(term_t*)a,myTerm);
 printf("%s",myTerm);

}

void InfoPrint(void* a) {
  ;
}

void InfoDest(void *a){
  ;
}

foreign_t
pl_lowercase(term_t u, term_t l)
{ char *copy;
  char *s, *q;
  int rval;

  if ( !PL_get_atom_chars(u, &s) )
    return PL_warning("lowercase/2: instantiation fault");
  copy = malloc(strlen(s)+1);

  for( q=copy; *s; q++, s++)
    *q = (isupper(*s) ? tolower(*s) : *s);
  *q = '\0';

  rval = PL_unify_atom_chars(l, copy);
  free(copy);

  return rval;
};

foreign_t
ins_dbf(term_t t, term_t goal)
{
    /*assert(etalis_global_tree);*/
    char* myGoal=(char*)malloc(256*sizeof(char));
    termToString_(t,myGoal);
    printf("%s\n",myGoal);
    free(myGoal);
    PL_succeed;
};
foreign_t
del_dbf(term_t label, term_t goal)
{
    int rval;
        return rval;


};
foreign_t
check_dbf(term_t label, term_t goal, term_t out_goal)
{

};
foreign_t
create_db(void)
{



};

#endif


