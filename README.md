# etalis
Exported from code.google.com/p/etalis

ETALIS is an open source system for Complex Event Processing with two accompanied languages called: ETALIS Language for Events (ELE) and Event Processing SPARQL (EP-SPARQL). ETALIS is based on a declarative semantics, grounded in Logic Programming. Complex events are derived from simpler events by means of deductive rules. Due to its root in logic, ETALIS also supports reasoning about events, context, and real-time complex situations (i.e., Knowledge-based Event Processing). ETALIS stands for Event TrAnsaction Logic Inference System.

ETALIS is implemented in Prolog. The engine runs on many Prolog systems: YAP, SWI, SICStus, XSB, tuProlog and LPA Prolog. Download ETALIS from here. We installed ETALIS and EP-SPARQL on several operating systems including: Windows XP, Vista and 7, Mac OS, Android OS (with tuProlog) and Linux-based systems (Ubuntu, RedHat?, SUSE).

Features:

* declarative rule-based language for event processing;
* detection of complex events and reasoning over states (with logic rules);
* classic event operators (e.g., sequence, concurrent conjunction, disjunction, negation etc.). The language supports all operators from Allen's interval algebra (e.g., during, meets, starts, finishes etc.);
* count-based sliding windows;
* event aggregation for count, avg, sum, min, max etc.;
* event filtering, enrichment, projection, translation, and multiplication are supported;
* alarm events to trigger events after certain durations of time or at absolute datimes (working only under SWI Prolog);
* processing of out-of-order events (i.e. events that are delayed due to different circumstances e.g. network anomalies etc.);
* event retraction (revision);
* shared computation plan for evaluation of complex event rules;
* support for Event Processing SPARQL (EP-SPARQL) language.

See the ETALIS Manual for more information about ETALIS.

To evaluate ETALIS we have implemented the Fast Flower Delivery use case created by Opher Etzion and Peter Niblett in their book: Event Processing in Action. Description of the use case can be found in the aforementioned book or from the Event Processing Technical Society website. The use case implementation in ETALIS can be found in examples/flower_delivery or on a corresponding Fast_Flower_Delivery_Use_Case wiki page.

Contact:
Darko Anicic <darko.anicic@fzi.de>: for questions related to ETALIS design, architecture and underlying algorithms;
Paul Fodor <pfodor@cs.stonybrook.edu>: for questions related to implementation of the ETALIS engine or any problems with the execution of the ETALIS programs.
