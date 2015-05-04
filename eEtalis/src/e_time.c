#include "e_time.h"

/*
 * Get Current Time Stamp
 */
void newTimeStamp(Timestamp* ts)
{
    struct timeval tv;
    gettimeofday(&tv,NULL);
    ts->time=tv.tv_sec;
    ts->parts=tv.tv_usec;
}


/* High Res functions */


/*
 * Get the timeStamp in millisecond, the timestamp should be already defined
 */
 /* TODO needs more work*/

double getHighResStamp(Timestamp* ts) /* only works on processors that support double accuracy */
{
    return ((double)ts->time); /*+(double)ts->parts*TIME_ACCURACY; */ /* TODO fix 29.12.2012 */
}
/* todo optimize */
double getTimeDiff(Timestamp* a, Timestamp *b)
{
    return fabs(getHighResStamp(b)-getHighResStamp(a));
}
void getTimeDiff_struct(Timestamp* a, Timestamp *b,Timestamp* DeltaT)
{
    DeltaT->time   = abs((a->time)-(b->time));
    DeltaT->parts = abs((a->parts)-(b->parts));
}


