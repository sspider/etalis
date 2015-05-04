/**

 * embedded ETALIS 0.1 (eEtalis 0.1)
 * embedded Event Driven Transaction Logic Inference System
 * \date   01.Sep.2012
 * \author Ahmed Khalil Hafsi ahmed@hafsi.org
 * Copyright 2012 (c) Ahmed Khalil Hafsi Forschungszentrum Informatik Karlsruhe
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 *
 * \file main.c
 * \author Ahmed Khalil Hafsi ahmed@hafsi.org
 * This version of eEtalis needs SWI Prolog to be able to run.
 * You must link the eEtalis binary with the SWI dll to resolve the undefined symbols.

 * Build Instructrions:

 - Include dir = %SWI_HOME_DIR/Include%
 - Link with SwiLib.dll

 Common errors:
 SWI could not find resources.
 Solution : link the saved state with plld or add the SWI_HOME_DIR into the envirement.

*/




#include <stdlib.h>
#include <stdio.h>
#include <uthash.h>
#include <malloc.h>

#include <SWI-Prolog.h>


#include "ETALIS_conf.h"

#include "dbg.h"
#include "storage_conf.h"
#include "memory.h"
#include "events.h"
#include "io.h"




/* A definition of all the predicates written in c*/
PL_extension pl_predicates[] =
{
    /*{ "name",      arity,  function,      PL_FA_<flags> },*/
#ifdef EXTRA_PROLOG
    { "lowercase", 2,       pl_lowercase,  0 },
    { "_create_db", 0,       create_db,     0 },
    { "_ins_dbf",2,       ins_dbf,       0 },
    { "_del_dbf",2,       del_dbf,       0 },
    { "_check_dbf",3,       check_dbf,       0 },
    {
        "make_buffer",0,make_buffer,0
    },
    {
        "push_buffer",1,push_b,0
    },
    {
        "pop_buffer",1,pop_b,0
    },
#endif
    {
        "cevent",2,triggerEvent,0
    },
    {
        "cevent",1,triggerEvent_u,0
    },

    { "docs",0,       docs,       0 },
    { NULL,        0,       NULL,          0 }     /* terminating line */
};

/* load etalis main Prolog files */
int load_etalis(char* path)
{
    printf("--- Loading ETALIS Kernel ... ");
    consultPrologFile("Statics.P"); /*A preprocessor file*/
    consultPrologFile(path);
    printf("done!\n");

    return TRUE;
};
/* load an event stream from path */
int load_stream(char* path)
{
    printf("--- Loading Stream 500k ... ");
    /* Load a stream into eETALIS.*/

    /*
    FILE* Stream = fopen(path."rb");
    */

    printf("done!\n");
    return TRUE;
}
/* load the prolog runtime */
void load_prolog(char**argv)
{
    char *av[10];
    int ac = 0;
    av[ac++] = argv[0];
    av[ac++] = "-q";
    av[ac]   = NULL;

    printf("--- Loading Prolog Kernel ... ");
    if ( !PL_initialise(ac, av) )
        PL_halt(1);
    printf("done!\n");
}
/* compile an event file from c and integrate it into prolog */
int compile_event_file(char* path)
{
    static predicate_t p;
    int rval;
    if ( !p )
        p = PL_predicate("compile_events", 1, NULL);
    term_t a0 = PL_new_term_refs(1);
    PL_put_atom_chars(a0,(path!=NULL ? path : DEFAULT_EVENT_FILE)); /*if NULL compile the default event file*/
    printf("--- Compiling ");
    printf((path ? path : DEFAULT_EVENT_FILE));
    printf(" ... ");
    rval = PL_call_predicate(NULL, PL_Q_NORMAL, p, a0);
    printf("done!\n");
    return (rval ? TRUE : FALSE);
}

/* _globals */
etalis_main_conf _conf;      /* eEtalis general configuration data*/
EtalisExecTree*  _exec_tree; /* CEP execution tree */
eventHash*      _event_hash; /* hashtable for quick access to the CEP execution tree nodes based on eventname/arity */
#ifdef NETWORK /* a global / platform independent socket handler for in/out bound communications */
#ifdef _WIN32
SOCKET
#else
int
#endif
TCPsock;
#endif

/* __main__ */
int main(int argc, char **argv)
{
    PL_register_extensions(pl_predicates);
    putenv("SWI_HOME_DIR=C:\\hafsi\\software\\swi-32\\pl"); /* for production mode, this should be changed to use the script that comes with SWI : plld*/

    print_banner();
    _conf.policy=recent;                /* Default CEP policy: recent */

    load_prolog(argv);                  /* Load the SWI Prolog kernel. */
    load_etalis(DEFAULT_ETALIS_KERNEL); /* Load the ETALIS kernel modules. */
    compile_event_file(NULL);           /* NULL means compile the default event file. */


    initEventHash(_event_hash);         /* Intitialize the eventHash. */

    _exec_tree = buildExecTree();       /* Execution tree. */





#ifdef DEBUG     /* dbg _exec_tree */
/*
    EtalisEventNode *myEv = _exec_tree->complexEvents;
    EtalisExecNode  *myNode = myEv->childNode;
    EtalisEventNode *lc = myNode->leftChild;
    EtalisEventNode *rc = myNode->rightChild;
    EtalisExecNode  *upper1 = lc->parentNode;
    EtalisExecNode  *upper2 = rc->parentNode;

    eventHash tempEventHash;
    eventHash* foundEventNode = NULL;
    memset(&tempEventHash,0,sizeof(eventHash));
    tempEventHash.ev.arity=0;
    strcpy(tempEventHash.ev.name,"a");


    HASH_FIND(hh,_event_hash,&tempEventHash.ev,sizeof(eventType),foundEventNode);

    EtalisEvent a;

    clock_t uptime = clock() / (CLOCKS_PER_SEC / 1000);
    clock_t uptime2 = clock() / (CLOCKS_PER_SEC / 1000);
*/
#endif /* f dbg*/

    printf("\nFor help, use ?- docs.\n");


#ifdef BENCHMARKING
    clock_t start = clock();
    /*load_fire_stream_RAM("benchmarks/a_2_d_2_10.P",parse_cEvent);*/
    load_fire_stream_RAM("benchmarks/a_2_d_2_2m.P",parse_cEvent);
    /*load_fire_stream_RAM("benchmarks/a_2_d_2_10.P",parse_cEvent);*/
    printf ( "Time : %f\n", ( (double)clock() - start ) / CLOCKS_PER_SEC );
#endif


    PL_halt(PL_toplevel() ? 0 : 1);       /* Prolog Event Loop. */
    return 0;
}


