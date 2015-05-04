#ifndef MEMORY_H_INCLUDED
#define MEMORY_H_INCLUDED


#ifdef EXTRA_PROLOG
extern foreign_t pl_lowercase(term, term);

/*General Event Database : Tree based*/
extern foreign_t create_db(void);
extern foreign_t ins_dbf(term,term);
extern foreign_t del_dbf(term,term);
extern foreign_t check_dbf(term,term,term);

/*Event buffers*/
extern foreign_t make_buffer(void);
extern foreign_t push_b(term);
extern foreign_t pop_b(term);
#endif

/****  Utility Functions */

extern foreign_t docs(void);             /* documentation of the provided predicates*/
extern foreign_t pl_display(term_t t);   /* write a term into standard output stream*/
extern int consultPrologFile(char* cmd); /* consult a prolog file*/
extern int str_alp(char *s1,char *s2);   /* compare 2 strings, 0 if equal, 1 if s1 is bigger*/
extern void print_banner();              /* print ETALIS banner */
extern void padding ( char ch, int n );  /* print padding */
extern void viewEventHashTable(void);    /* print EventHash */
extern int findInStringArray(size_t frm,size_t to,char** arr,char*str_to_be_found); /* find a string in a string array */

#endif
