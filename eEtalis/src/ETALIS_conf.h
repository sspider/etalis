#ifndef ETALIS_CONF_H_INCLUDED
#define ETALIS_CONF_H_INCLUDED

#define DEBUG /* define if debugging session */

/* #define BENCHMARKING  /* define BENCHMARKING for benchmarking mode */


/**** General eETALIS Configuration Parameters */

#define VERSION_ee          "0.15"

#define DEFAULT_EVENT_FILE "pl/v0_binarized/testPattern.event"
#define DEFAULT_ETALIS_KERNEL "pl/v0_binarized/src/etalis.P"

#define MAX_STRING_SIZE     256
#define MAX_EVENT_NAME_SIZE 256
#define MAX_EVENT_ARITY     256

/* TODO (hafsi#6#): remove SWI_Prolog dependency */
#define PL_BACKEND    /* using SWI Prolog */

#define NETWORK /* enable tcp ip communications */

/* Error handling */
#define ETALIS_OK            0x1
#define ETALIS_ERR           0x0

/* Argument processing */

#define UNBOUND_ARGUMENT          -0x33
#define BOUND_ARGUMENT_TO_WHERE   -0x2
#define BOUND_ARGUMENT_TO_DO      -0x1
#define BOUND_ARGUMENT_TO_CPLX     0x0

/* ETALIS internal booleans */
#define ETALIS_BOOL          int
#define ETALIS_TRUE          0x1
#define ETALIS_FALSE         0x0

/* */
/* #define USE_RB_TREES */ /* define USE_RB_TREES to use them instead of the hashtable, note the code may need further changes. */
/* #define EXTRA_PROLOG */ /* define EXTRA_PROLOG for extra prolog functionality implemented in C */

#ifdef BENCHMARKING
    #undef DEBUG
#endif

#ifdef NETWORK
    #define RCVBUFSIZE 1024
#endif

/** main eEtalis Configuration */

typedef struct etalis_main_conf{
enum policy_type{recent=1,unrestricted} policy; /* CEP Policy*/
}etalis_main_conf;



/*** error and logging */

#define _err(str,L)  printf("---Error: (str)\n");
#define _warn(str,L) printf("---Warning: (str)\n");

#endif
