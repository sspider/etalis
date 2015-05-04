swipl -g "['../../src/etalis.P'],set_etalis_flag(logging,off), compile_event_file('flower_specification.event'), load_static_rules('flower_specification_static_rules.P'), load_database('use_cases/flower_stream_test_02.db'), execute_event_stream_file('use_cases/flower_stream_test_02.stream')."

################################################################################
rm -rf *.bin *.ctr
