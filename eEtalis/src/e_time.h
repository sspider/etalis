#ifndef TIME_H_INCLUDED
#define TIME_H_INCLUDED

#include <math.h>
#include <stdlib.h>

#ifdef __MINGW32__ || __GNUC__ || __APPLE__
    #include <sys/time.h>
    #define MILLISECOND_supported
#elif _WIN32
    #include <windows.h>
#endif

#define TIME_ACCURACY 1e-6
#define PROCESSOR_SUPPORTS_DOUBLE 1
#define WINDOW_SIZE_T double /* some processors do not support double (64bit) accuracy or is too costly to compute, so we might restrict our selfes by either using a Timestamp struct or an unsigned int */

typedef struct Timestamp
{
    long time; /* seconds */
    long parts; /* depending on the system, parts is milliseconds or better */ /* the accuracy is defined in TIME_ACCURACY */
} Timestamp;


double getHighResStamp(Timestamp* ts); /* only works on processors that support double accuracy (should be 64bit, 32bit is not enough to represent the timestamp) */ /* todo make alternatives */
double getTimeDiff(Timestamp* a, Timestamp *b); /* b - a */ /* result is a double */
void getTimeDiff_struct(Timestamp* a, Timestamp *b,Timestamp* DeltaT); /* result is a Timestamp */
void newTimeStamp(Timestamp* ts);
void checkWindow(double window, Timestamp *a,Timestamp *b); /* an optimized version to check if the timestamps are in a valid window */



#endif
