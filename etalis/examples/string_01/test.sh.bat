swipl -g "['../../src/etalis.P'], set_etalis_flag(output_temporary_files,on), set_etalis_flag(logging_to_file,on), set_etalis_flag(store_fired_events,on), ['test_01.db'], compile_event_file('event_specification.event'), event(a(aba)), event(b(a)), halt."

