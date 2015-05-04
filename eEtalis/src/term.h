#ifndef TERM_H_INCLUDED
#define TERM_H_INCLUDED

#include <SWI-Prolog.h>


void termToString(term_t t, char* myGoal,char* args);
void termToString_(term_t,char*);
void termToStringVerbatim(term_t t, char* myGoal,char* args);
void GoalToterm(term_t t, char* myGoal);




#endif
