#include "io.h"


void load_fire_stream_RAM(char* path, void (*parser_func)(char*,EtalisEvent**))
{
    char * buffer = 0;
    long length;
    FILE * stream = fopen (path, "r");

    char event[256]; /* optimize */
    EtalisEvent* e_struct=NULL;

    while(fgets(event,sizeof event,stream) != NULL) /* read file line by line */
    {
        parser_func(event,&e_struct);

        triggerEvent_intern_no_hash(e_struct);
    }
    fclose(stream);

}

/*

 */
void parse_cEvent(char* event,EtalisEvent** c_event_p)
{
    int i=0;
    eventType ev = {0,NULL};
    EtalisEvent* e_struct = NULL;

    while(event[i] != '.')
    {

        if (event[i] == ',')
            ev.arity++;
        i++;
    }
    ev.arity++;



    char * pch = strtok (event,",()"); /* dels used in ETALIS Format */
    strcpy(ev.name,pch);


    EtalisEventNode* myNode= find_event_model_in_hash(&ev);


    if(myNode != NULL);
    {
        i=0;
        e_struct = (EtalisEvent*)malloc(sizeof(EtalisEvent)); /* allocate event     */
        *c_event_p = e_struct;
        e_struct->RootModel=myNode;
        e_struct->args = (int*)malloc(sizeof(int)*ev.arity);  /* allocate arguments */

        newTimeStamp(&e_struct->timestamps[0]);               /* get a timestamp */
        e_struct->timestamps[1] = e_struct->timestamps[0];

        while (pch != NULL && i < ev.arity)

        {

            pch = strtok(NULL,",().");
            *((int*)e_struct->args + i) = (int)atoi(pch);
            i++;

        }

    }
}
#ifdef BENCHMARKING
/* generate benchmarking streams and feed them to the engine */
void generateStream_size(int size)
{
    srand ( time(NULL) );
    char name_pool[]="ad";
    int i=0;
    EtalisEvent* e_struct;
    int len=strlen(name_pool);
    char* event;

    for(i=0;i<size;i++)
    {
        char n=name_pool[rand() % len];
        strcat(event,(char*)n);
        strcat(event,"(1,2).");
        parse_cEvent(event,&e_struct);
        triggerEvent_intern_no_hash(e_struct);
    }


}
#endif

#ifdef NETWORKING
/* remote streams */
/* high level API */
void BindInput_TCP(int port, void (*encoding_func)(char* In,void* Out))
{

}
void BindOutput_TCP(char* ADDR,int port,void (*encoding_func)(char* In,void* Out))
{

}
void closeRemote_TCP(void)
{

}
/* low level Remote  IO API */
#ifdef _WIN32
int tcpConnect(SOCKET sock,char* IP, int port)
{
    struct sockaddr_in server;
    struct hostent *host_info;
    unsigned long addr;

    WORD wVersionRequested;
    WSADATA wsaData;
    wVersionRequested = MAKEWORD (1, 1);
    if (WSAStartup (wVersionRequested, &wsaData) != 0)
    {
        log_err("Cannot initialize the tcp client.");
        return ETALIS_ERR;
    }

    else
        log_info("Winsock initialized\n");

    sock = socket( AF_INET, SOCK_STREAM, 0 );
    if (sock < 0)
        {
        log_err("Cannot create socket !");
        return ETALIS_ERR;
    }

     memset( &server, 0, sizeof (server));
    if ((addr = inet_addr(IP)) != INADDR_NONE) {
        memcpy( (char *)&server.sin_addr, &addr, sizeof(addr));
    }
    else {
        host_info = gethostbyname(argv[1]);
        if (NULL == host_info)
            {
                log_err("Cannot find server !");
                return ETALIS_ERR;
            }
        /* Server IP Adress */
        memcpy( (char *)&server.sin_addr,
                host_info->h_addr, host_info->h_length );
    }
    server.sin_family = AF_INET;
    server.sin_port = htons( port );

    if(connect(sock,(struct sockaddr*)&server,sizeof(server)) <0)
        return ETALIS_ERR;

}
int tcpListen(SOCKET sock,int port)
{

}
#else
int tcpConnect(int sock,char* IP, int port){}
int tcpListen(int sock,int port){}
#endif
#endif
