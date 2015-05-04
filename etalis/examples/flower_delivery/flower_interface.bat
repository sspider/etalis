swipl -g "['../../src/etalis.P'],set_etalis_flag(logging,off), compile_event_file('flower_specification.event'), load_static_rules('flower_specification_static_rules.P'), load_database('use_cases/flower_interface_01.db'), flower_use_case_interface."

################################################################################
rm -rf *.bin *.ctr
