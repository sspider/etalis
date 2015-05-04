#ifndef EVENTS_H_INCLUDED
#define EVENTS_H_INCLUDED

#include <SWI-Prolog.h>
#include <time.h>
#include "lib/uthash.h"
#ifdef USE_RB_TREES
    #include "red_black_tree.h"
#endif
#include "lib/stack.h"
#include "ETALIS_conf.h"
#include "cep_core.h"
#include "e_time.h"



#include <assert.h>

typedef struct eventType
{

    unsigned int arity;
    char name[MAX_EVENT_NAME_SIZE];

} eventType;


typedef struct trClause
{
    char label[MAX_STRING_SIZE];
    eventType event;
    union exec{
    void (*exec_1) (void* arg);
    void (*exec_2)(void* arg1, void* arg2);
    void (*exec_3)(void* arg1, void* arg2, void* arg3);
    } exec;
} trClause;


/* Argument link definitions */

/* defined in Etalis_conf.h */
/*
    UNBOUND_ARGUMENT          -0x33
    BOUND_ARGUMENT_TO_WHERE   -0x2
    BOUND_ARGUMENT_TO_DO      -0x1
    BOUND_ARGUMENT_TO_CPLX     0x0
*/

/*
 * Event argument interrelations basic struct
 */
typedef struct argument_link
{
    int event_;
    int argument_number;

} argument_link;


/*
 * Event arguments must be one of these types
 */
typedef enum argument_types
{
    EA_INT,     /* a 32 bit signed integer */
    EA_U_INT,   /* a 32 bit unsigned integer */
    EA_STRING,  /* a string */ /* todo static or dynamic ? */ /* profile */
    EA_FLOAT,   /* a 32 bit IEEE 754 if available on the platform (C99 Standard)*/
    EA_VAR      /* an unbound variable */

} argument_types;

/*
 * An Etalis WHERE Node Model
 */
typedef struct EtalisWhereNode
{
    int size;                  /* how many conditions there are in this node */
    char* conditions;          /* conditions in a verbatim format */
    argument_link* arg_links;  /* links to the relevant events in the rule */


} EtalisWhereNode;

/*
 * An Etalis Event Root Model
 */
typedef struct EtalisEventNode
{
    eventType event;                    /* event name, arity */
    struct EtalisExecNode * parentNode; /* the operator that should be executed */
    struct EtalisExecNode * childNode;  /* the operator that may produce this event */
    stk_stack* eventStack;              /* an event buffer that contains event instances */ /* todo: should be a circular buffer to enable auto garbage collection */
    void (*trigger) (void*);            /* a function to execute upon triggering this event */
    argument_link*    arg_links;        /* event argument relation data */
    EtalisWhereNode* whereNode;         /* a priori conditions for this event */
    ETALIS_BOOL      is_temp;           /* a flag to hold whether this event is temporary */

} EtalisEventNode;

/*
 * an ETALIS binary operator model
 */
typedef struct EtalisExecNode
{
    char label[MAX_STRING_SIZE]; /* operator label*/
    char  name[MAX_STRING_SIZE]; /* operator name */
    enum op_type{binary,unary} op_type;  /* operator type*/
    struct EtalisEventNode* leftChild;   /* Left Event*/
    struct EtalisEventNode* rightChild;  /* Right Event */
    struct EtalisEventNode* parentEvent; /* Complex Event*/
    union left_exec{                     /* The Exec of the left Event*/
    void (*exec_1) (void* arg);
    void (*exec_2)(void* arg1, void* arg2);
    void (*exec_3)(void* arg1, void* arg2, void* arg3);
    } left_exec;
    union right_exec{                    /* The Exec of the right Event*/
    void (*exec_1) (void* arg);
    void (*exec_2)(void* arg1, void* arg2);
    void (*exec_3)(void* arg1, void* arg2, void* arg3);
    } right_exec;
    ETALIS_BOOL has_condition;
    char* condition;
    EtalisWhereNode* whereNode;           /* all information about conditions in the WHERE part of the rule */
    WINDOW_SIZE_T window_size;            /* rule window size */

} EtalisExecNode;


/* ETALIS rule */
typedef struct EtalisBatch
{
    char label[MAX_STRING_SIZE];          /* rule label*/
    int batchSize;                        /* number of nodes the rule has */
    EtalisExecNode* nodes;                /* an array of all nodes starting from the least significant node/operator */
    WINDOW_SIZE_T window_size;            /* rule window size */


}EtalisBatch;


/*
 * An Etalis Event
 */

typedef struct EtalisEvent
{
    EtalisEventNode* RootModel;     /* Event Root Model in the EtalisExecTree */
    void*            args;          /* A list of Event Arguments with size RootModel->event.arity*/
    Timestamp        timestamps[2]; /* Event timestamps */
} EtalisEvent;

/*
 * Etalis rules are represented in this tree.
 */
typedef struct EtalisExecTree
{
    struct EtalisEventNode* complexEvents;
    int size;
    void (*exec) (void* arg);

} EtalisExecTree;


/*
 * The EventHash maps an event into its EtalisEventNode
 */
typedef struct eventHash
{
    eventType ev;            /*hash key*/
    EtalisEventNode* myNode; /*data*/
    UT_hash_handle hh;       /*makes this struct hashable*/
} eventHash;

extern eventHash*      _event_hash;


/*
 * EtalisExecTree related functions
 */
EtalisBatch*    constructBatches();
EtalisExecTree* buildExecTree();
void destroyExecTree(EtalisExecTree*);
void construct_rule(EtalisBatch * batch, term_t term);
void print_rule(const EtalisEventNode* cplxEvent);

/*
 * EventHash related functions
 */
void initEventHash(eventHash* );
void addToEventHash(EtalisExecNode* Rule);
EtalisEventNode* getEventFromHash(eventType* event);


/*
 * every CEP operator that is supported, is represented as this structure.
 */
typedef struct CoreCEP_LUT
{
    char CEP_name[256];
    unsigned int CEP_arity;
    union {
    void (*exec_1) (void* arg);
    void (*exec_2)(void* arg1, void* arg2);
    void (*exec_3)(void* arg1, void* arg2, void* arg3);
    } exec1;
    union {
    void (*exec_1) (void* arg);
    void (*exec_2)(void* arg1, void* arg2);
    void (*exec_3)(void* arg1, void* arg2, void* arg3);
    } exec2;
    void (*parser_func) (EtalisExecNode*,term_t t);
} t_CoreCEP_LUT;


/*
 * Parsers : transform a Prolog term into an Etalis Exec Node.
 * These Parsers will be finally included in the final eEtalis IDE. It could also be included in the embedded version depending on the embedded target platform
 */

void parse_within_op_(EtalisExecNode *OperatorNode  ,term_t t);
void parse_seq_op_   (EtalisExecNode *operatorNode  ,term_t t);


void where_binarization(EtalisExecNode* OperatorNode);
ETALIS_BOOL constraint_fullfilled(char *constraint,EtalisEventNode * Ev);
ETALIS_BOOL constraint_par_fullfilled(char *constraint,EtalisEventNode * Ev);
void assign_relevant_constraints(EtalisExecNode* OperatorNode, EtalisEventNode* CurrentEvent);
void delete_constraint(EtalisWhereNode* WH_node, int pos);

/* Trigger an Event*/

/** General procedure on Event Triggering :
0) get the event key (name/arity) from the term_t
1) get the EtalisEventNode from the EventHash
2) execute the EtalisEventNodes's trigger() function.
*/

/* event triggering from the external prolog environment (ETALIS Event Format)*/

foreign_t triggerEvent(term_t args,term_t time); /* trigger an event with explicit timestamp*/
foreign_t triggerEvent_u(term_t args);           /* trigger an with an implicit timestamp */

/* event triggering from C (internal eEtalis Format) */
void triggerEvent_intern_no_hash(EtalisEvent* event); /* trigger an event that we know its EventModel (no search in the _event_hash is performed) */
void triggerEvent_intern_hash(eventType* key /* key to find in the hash */, EtalisEvent* event /* event arguments */);    /*search for an event in the hash and trigger its model */
EtalisEventNode* find_event_model_in_hash(eventType* key);

/* event arguments processing functions */

void build_args_map(EtalisExecNode* Node,term_t t1,term_t t2);
void process_event_args(EtalisEvent* Cplx);
void parse_validate_args(term_t args, EtalisEvent* event);
int validate_arguments(EtalisEvent* /* event_to_validate*/, EtalisEvent* /*event_left*/);
int aprio_validation(EtalisEvent* Event_b);


/* utility event functions */
void print_event(char* event);
eventType* getEventType(term_t event_term); /*Get Event Type (name and arity) from the Prolog term*/

#ifdef USE_RB_TREES
/** Event Map :
an event map is location where every Event Type/Class has a key we call EV_ID

a/0 : 1
b/0 : 2

etc ...

*/
rb_red_blk_tree* buildEventMap();
rb_red_blk_tree* getEventMap();
int getEventKey(eventType et);


/** Execution Tree :

every node of this tree is a function pointer that points to the action that would be made in case the event is triggered.
the key to those nodes are either EV_ID (from the event map) or the event signature (a/0, etc ..)

*/
void buildExecutionTree();

/* Every rule has a Rule ID*/
void buildRuleMap();
/*utility functions for the RB TRees*/
int Compare_EventType    (const void* a, const void* b);
void DestroyEventType    (void* a);
void DestroyInfoEventKey (void* a);
void PrintEventKey       (const void* a);
void PrintInfoEventKey   (void* a);

#endif /* end def USE_RB_TREES */

#endif
