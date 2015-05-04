swipl -g "['../../src/etalis.P'], set_etalis_flag(output_temporary_files,on), set_etalis_flag(logging_to_file,on), compile_event_file('test_01.event'), event(a(2)), event(b(2)), event(c(2)), halt."
