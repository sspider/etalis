E:\_workspaces\_workspace_01_internal_SVN_based\etalis\examples\channels_01>"c:\Program Files\pl\bin\plcon.exe" -g "
_events,on), compile_event_file('test_01.event'), fire_event(sensor1,a(1)), findall(stored_event(event(b(S,X),T)),s
_,_,_,_,_,_),datime(_,_,_,_,_,_,_)])), stored_event(event(b(channel2,1),[datime(_,_,_,_,_,_,_),datime(_,_,_,_,_,_,_)
,halt."
% c:/documents and settings/pfodor/application data/swi-prolog/pl.ini compiled 0.00 sec, 1,844 bytes
%  binarizer.P compiled 0.00 sec, 10,256 bytes
%  compiler.P compiled 0.05 sec, 71,404 bytes
%  date_time.P compiled 0.00 sec, 18,020 bytes
%  event_utils.P compiled 0.00 sec, 3,796 bytes
%  executor.P compiled 0.02 sec, 12,668 bytes
%  flags.P compiled 0.00 sec, 2,616 bytes
%  garbage_collection.P compiled 0.00 sec, 3,764 bytes
%  java_interface.P compiled 0.00 sec, 2,004 bytes
%  justify_etalis.P compiled 0.00 sec, 21,280 bytes
%  labeled_event_rules.P compiled 0.00 sec, 2,352 bytes
%  logging.P compiled 0.00 sec, 1,948 bytes
%  parser.P compiled 0.02 sec, 14,528 bytes
%  storage.P compiled 0.00 sec, 13,676 bytes
%  utils.P compiled 0.00 sec, 7,804 bytes
% ../../src/etalis.P compiled 0.08 sec, 203,416 bytes
*Event: a(channel1, 1) @ [datime(2010, 5, 14, 2, 58, 57, 1), datime(2010, 5, 14, 2, 58, 57, 1)]
*Event: b(channel1, 1) @ [datime(2010, 5, 14, 2, 58, 57, 1), datime(2010, 5, 14, 2, 58, 57, 1)]
*Event: a(channel2, 1) @ [datime(2010, 5, 14, 2, 58, 57, 2), datime(2010, 5, 14, 2, 58, 57, 2)]
*Event: b(channel2, 1) @ [datime(2010, 5, 14, 2, 58, 57, 2), datime(2010, 5, 14, 2, 58, 57, 2)]

------------
Define event types:

event_type(a/1,[number]). % facts
event_type(b/1,[number]).

check_event_type(X):-
  X=..[EventName|Args],
  length(Args,Arity),
  event_type(EventName/Arity,ArgumentTypes),
  check_arg_types(Args,ArgumentTypes).

check_arg_types([],[]):-
  !.
check_arg_types([Head|Args],[Type|ArgumentTypes]):-
  Check=..[Type,Head],
  call(Check),
  check_arg_types(Args,ArgumentTypes),
  !.

Define channel's event types:

channel_types(channel1,[a/1,b/1]).

- check if an event matches the types of a channel:
check_channel_input(Channel,Event):-
  Event=..[EventName|Args],
  length(Args,Arity),
  channel_types(Channel,EventTypes),
  member(EventName/Arity,EventTypes),
  check_event_type(Event). % checks if the arguments are ok
  

