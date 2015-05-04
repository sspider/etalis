Fast Flowers Delivery Use Case

This directory contains an implementation of an example created by Opher Etzion and Peter Niblett used as an motivating example in their event processing book "Event Processing in Action" ( http://www.manning.com/etzion ). 

The description of this example can be obtained from the aforementioned book and from the following blog: http://epthinking.blogspot.com/2009/06/on-methodic-use-case-used-in-epia-book.html

--------------------------------------------------------------------------------
Prerequisites: install SWI prolog from: http://www.swi-prolog.org/download/stable (Note: SWI has the alarm predicates used by us for the alarm events only starting with version 5.8.0, while older version do not support these calls. If your Linux system has a preinstalled version of swipl, please check if it's 5.8.0 or newer).

--------------------------------------------------------------------------------
There are two ways to run the Flower Delivery use case: 
    - test with a synthetic stream (i.e., flower_stream_test.bat), and 
    - user input events (i.e., flower_interface.bat).

Note: Almost all the code is self explanatory and one can see a direct correspondence from the specification to the code (the language specification is in the "doc" directory in the Etalis home directory).

The only implementation pattern that might not be intuitive to the user is the implementation of alert events (i.e. events scheduled by the system to be generated in the future).
For instance: "A delivery_alert is reported if a delivery_confirmation has not been reported within ten minutes of the committed delivery time". 
In this case, the pattern is as follows:

1. a system event is scheduled to be generated at a specified date-time in the future.
check_delivery/4 is a rule that implements the system event:

exceptionAlarmAbsoluteDatime(check_delivery(DeliveryRequestId,StoreId,DriverId,DeliveryTime),CheckTimeDelivery):-
	assignment(DeliveryRequestId,StoreId,ToCoordinates,DeliveryTime,DriverId,_ScheduledPickupTime)
	where (check_delivery_time(WaitDuration),addSec_Datime(WaitDuration,DeliveryTime,CheckTimeDelivery)). 
	
The system event time is a configurable parameter and can be changed.

2. after the specific check is detected, the condition of the constraint for the alarm is checked out (in this case, 
if the delivery for DeliveryRequestId was not detected, the system will generate delivery_alert/4:

delivery_alert(DeliveryRequestId,StoreId,DriverId,DeliveryTime):- 
	check_delivery(DeliveryRequestId,StoreId,DriverId,DeliveryTime) fnot 
	delivery_confirmation(DeliveryRequestId,DriverId,_RealDeliveryTime).

