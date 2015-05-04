rm output.jpg

swipl -g "open('../results.txt',append,FH), ['../../src/etalis.P'], set_etalis_flag(output_temporary_files,on), set_etalis_flag(logging_to_file,on), set_etalis_flag(store_fired_events,on), set_etalis_flag(etalis_justification,on), compile_event_file('test_01.event'), event(a), findall(stored_event(event(c,T)),stored_event(event(c,T)),List), ( List = [] -> write(FH,'justification_02\t\tpassed\n') ; write(FH,'justification_02\t\tfailed\n') ), nl,nl,write('Justification for NOT c\n'),nl, justify_event(c,[T1,T2],J), write_justification(J), write_justification_udraw(justify_event(c,[T1,T2]),J,'output.udg'), halt."

uDrawGraph -init ../../lib/remote-uDraw.txt

