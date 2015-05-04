dynamic updates work without compiling any file:
> swipl

1 ?- ['../../src/etalis.P'].
%  binarizer.P compiled 0.00 sec, 10,804 bytes
%  compiler.P compiled 0.02 sec, 75,616 bytes
%  date_time.P compiled 0.00 sec, 19,592 bytes
%  event_utils.P compiled 0.00 sec, 3,796 bytes
%  executor.P compiled 0.00 sec, 15,896 bytes
%  flags.P compiled 0.00 sec, 3,400 bytes
%  garbage_collection.P compiled 0.00 sec, 4,132 bytes
%  java_interface.P compiled 0.00 sec, 2,032 bytes
%  justify_etalis.P compiled 0.03 sec, 21,504 bytes
%  labeled_event_rules.P compiled 0.00 sec, 2,352 bytes
%  logging.P compiled 0.02 sec, 3,124 bytes
%  parser.P compiled 0.00 sec, 17,704 bytes
%  storage.P compiled 0.00 sec, 20,092 bytes
%  string.P compiled 0.02 sec, 6,292 bytes
%  utils.P compiled 0.02 sec, 12,912 bytes
%  java_communication.P compiled 0.02 sec, 2,716 bytes
% ../../src/etalis.P compiled 0.11 sec, 245,312 bytes
true.

2 ?- assert(print_trigger(_/_)).
true.

3 ?- ins_event_rule(r1 'rule:' c(Y) <- a(Y)).
true.

4 ?- event(a(1)).
*Event: a(1) @ [datime(2011,8,5,14,10,11,1),datime(2011,8,5,14,10,11,1)]
*Event: c(1) @ [datime(2011,8,5,14,10,11,1),datime(2011,8,5,14,10,11,1)]
true .

5 ?- ins_event_rule(r2 'rule:' d(Y) <- a(Y) seq b(Y)).
true.

6 ?- event(a(1)), event(b(1)).
*Event: a(1) @ [datime(2011,8,5,14,11,10,1),datime(2011,8,5,14,11,10,1)]
*Event: c(1) @ [datime(2011,8,5,14,11,10,1),datime(2011,8,5,14,11,10,1)]
*Event: b(1) @ [datime(2011,8,5,14,11,10,2),datime(2011,8,5,14,11,10,2)]
*Event: temp_e_1(a(1),b(1)) @
[datime(2011,8,5,14,11,10,1),datime(2011,8,5,14,11,10,2)]
*Event: d(1) @ [datime(2011,8,5,14,11,10,1),datime(2011,8,5,14,11,10,2)]
true .

7 ?-
