#ifndef IO_H_INCLUDED
#define IO_H_INCLUDED

/**
 * io.h General IO routines for eETALIS
 * @author Ahmed Hafsi | ahmed@hafsi.org
 *
*/

#include "ETALIS_conf.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <time.h>

#ifdef NETWORK
#ifdef _WIN32
/* Headerfiles für Windows */
#include <winsock.h>
#include <io.h>

#else
/* Headerfiles für UNIX/Linux */
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <unistd.h>
#endif
#endif

#include "events.h"

/** stream related functions */

/* local streams */

void load_fire_stream_RAM(char* path, void (*parser_func)(char*,EtalisEvent**)); /* streams are read line by line, lines are then passed into parser_func */
void load_fire_stream_InPlace(char* path,void (*parser_func)(char*,EtalisEvent**)); /* best performance for extremely huge streams that are too big to fit in memory, files are read in place and every event is triggered as the file is read.*/

#ifdef NETWORK /* if networking is enabled */
/* remote streams */
/* high level API */
void BindInput_TCP(int port, void (*encoding_func)(char* In,void* Out));
void BindOutput_TCP(char* ADDR,int port,void (*encoding_func)(char* In,void* Out));
void closeRemote_TCP(void);
/* low level Remote  IO API */
#ifdef _WIN32
int tcpConnect(SOCKET sock,char* IP, int port);
int tcpListen(SOCKET sock,int port);
#else
int tcpConnect(int sock,char* IP, int port);
int tcpListen(int sock,int port);
#endif

#endif

#ifdef BENCHMARKING
void generateStream_size(int size);
void generateStream_time(int t);
void runForEver(void);
#endif
/** parser functions available */

/* parse an event written in the ETALIS format */
void parse_cEvent(char* event,EtalisEvent** Event);

/* parse an event written in json */ /* needs json lib*/
void parse_cEvent_json(char* json_event,EtalisEvent* Event);

/* parse an event written in xml */ /* needs libxml */
void parse_cEvent_xml(char* json_event,EtalisEvent* Event);


#endif // IO_H_INCLUDED
