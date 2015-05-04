swipl -g "open('../results.txt',append,FH), ['../../src/etalis.P'], set_etalis_flag(output_temporary_files,on), set_etalis_flag(logging_to_file,on), set_etalis_flag(store_fired_events,on), set_etalis_flag(event_consumption_policy,recent), compile_event_file('test_01.event'), event(a), event(a), event(b), event(b), halt."

swipl -g "open('../results.txt',append,FH), ['../../src/etalis.P'], set_etalis_flag(output_temporary_files,on), set_etalis_flag(logging_to_file,on), set_etalis_flag(store_fired_events,on), set_etalis_flag(event_consumption_policy,recent_2), compile_event_file('test_01.event'), event(a), event(a), event(b), event(b), halt."

swipl -g "open('../results.txt',append,FH), ['../../src/etalis.P'], set_etalis_flag(output_temporary_files,on), set_etalis_flag(logging_to_file,on), set_etalis_flag(store_fired_events,on), set_etalis_flag(event_consumption_policy,chronological), compile_event_file('test_01.event'), event(a), event(a), event(b), event(b), halt."

swipl -g "open('../results.txt',append,FH), ['../../src/etalis.P'], set_etalis_flag(output_temporary_files,on), set_etalis_flag(logging_to_file,on), set_etalis_flag(store_fired_events,on), set_etalis_flag(event_consumption_policy,chronological_2), compile_event_file('test_01.event'), event(a), event(a), event(b), event(b), halt."

swipl -g "open('../results.txt',append,FH), ['../../src/etalis.P'], set_etalis_flag(output_temporary_files,on), set_etalis_flag(logging_to_file,on), set_etalis_flag(store_fired_events,on), set_etalis_flag(event_consumption_policy,unrestricted), compile_event_file('test_01.event'), event(a), event(a), event(b), event(b), halt."
