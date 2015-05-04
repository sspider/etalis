# Change the path to ETALIS' directory

swipl -g "
['../../../../../software/etalis/etalis/src/ep_sparql/ep_sparql.P'],
set_etalis_flag(store_fired_events,on), 
set_etalis_flag(event_consumption_policy,unrestricted),
parse_rdf_xml_file('floorplan.rdf', FloorPlan),
assert_all_rdf_tripples(FloorPlan, floorplan),
compile_events('query1.event'),
['test.P'],
test('stream_2.P'),
findall(stored_event(event(reaches(P1, P2, L1, L2),T)), stored_event(event(reaches(P1, P2, L1, L2), T)), List),
write(List),
statistics,
halt."
