#ifndef CEP_CORE_H_INCLUDED
#define CEP_CORE_H_INCLUDED


#include <stdlib.h>
#include "dbg.h"
#include "events.h"
#include "Etalis_conf.h"


/*Exec functions*/
void _win_cep(void* exec);
void _seq_win_cep_l(void* exec);
void _seq_win_cep_r(void* exec);

void _seqf_l(void* exec);
void _seq_batch_r(void* exec);

void _cep_identity(void* exec);

void _cep_print_event(void* exec);

#endif
