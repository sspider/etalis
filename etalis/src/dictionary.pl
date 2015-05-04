:- ['utils.P'].

:- resetCounter(dictv).

% add_check(+K,+D,-OD,-V)
add_check(K,[],[p(K,V)],V):-
	incCounter(dictv),
	counter(dictv,VN),
	atom_concat('X',VN,V),
	!.
add_check(K,[p(K2,V)|T],[p(K2,V)|T],V):-
	K == K2,
	!.
add_check(K,[H|T],[H|T2],V):-
	add_check(K,T,T2,V),
	!.

?- add_check(X,[],D1,V1),
		nl, write(V1), nl,
		add_check(Y,D1,D2,V2),
		nl, write(V2), nl,
		add_check(X,D2,D3,V3),
		nl, write(V3), nl.
		
		

	