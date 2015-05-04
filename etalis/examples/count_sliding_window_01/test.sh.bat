swipl -g "open('../results.txt',append,FH), ['../../src/etalis.P'], set_etalis_flag(output_temporary_files,on), set_etalis_flag(logging_to_file,on), set_etalis_flag(store_fired_events,on), compile_event_file('test_01.event'), event(a(1)), event(a(2)), event(a(3)), event(a(4)), event(a(5)), event(a(6)), event(a(7)), event(a(8)), event(a(9)), event(a(10)), event(a(11)), event(a(12)), halt."

