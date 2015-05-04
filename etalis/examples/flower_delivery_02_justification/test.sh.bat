rm output.jpg

swipl -g "open('../results.txt',append,FH), ['../../src/etalis.P'], set_etalis_flag(output_temporary_files,on), set_etalis_flag(logging_to_file,on), set_etalis_flag(store_fired_events,on),  set_etalis_flag(etalis_justification,on), compile_event_file('../flower_delivery/flower_specification.event'), load_static_rules('../flower_delivery/flower_specification_static_rules.P'), load_database('flower_stream_test_02.db'), execute_event_stream_file('flower_stream_test_02.stream'), findall(stored_event(event(ranking_increase(driverA,8),T)), stored_event(event(ranking_increase(driverA,8),T)),List), ( List = [stored_event(event(ranking_increase(driverA,8),[datime(_,_,_,_,_,_,_),datime(_,_,_,_,_,_,_)]))] -> write(FH,'flower_delivery_02\t\tpassed\n') ; write(FH,'flower_delivery_02\t\tfailed') ), justify_event(assignmentCounter(X1,X2,X3,X4),[T1,T2],J), write_justification_udraw(justify_event(assignmentCounter(X1,X2,X3,X4),[T1,T2]),J,'output.udg'), write_justification(J), halt."

uDrawGraph -init ../../lib/remote-uDraw.txt

