swipl -g "['../../src/etalis.P'], query_planner('test_01.event','test_01_qp.event'), set_etalis_flag(binarization_flag,off), compile_event_file('test_01_qp.event'), event(a(2)), event(b(3)), event(c(4)), halt."




# OR run the query plan independently

swipl -g "['../../src/etalis.P'], query_planner('test_01.event','test_01_qp.event'), halt."

swipl -g "['../../src/etalis.P'], set_etalis_flag(binarization_flag,off), compile_event_file('test_01_qp.event'), event(a(2)), event(b(3)), event(c(4)), halt."
