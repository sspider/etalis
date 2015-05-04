:- ['utils.P'].

:- resetCounter(dictv).

% add_check(+K,-V,+D,-D2)
add_check(K,V,[],[p(K,V)]):-
	incCounter(dictv),
	counter(dictv,VN),
	atom_concat('X',VN,V),
	!.
add_check(K,V,[p(K2,V)|T],[p(K2,V)|T]):-
	K == K2,
	!.
add_check(K,V,[H|T],[H|T2]):-
	add_check(K,V,T,T2),
	!.

% writeTerm(+H,+D,-D2)
writeTerm(H,D1,D2):-
	var(H),
	add_check(H,V1,D1,D2),
	write(V1),
	!.
writeTerm(H,D1,D1):-
	atomic(H),
	write(H),
	!.
writeTerm(H,D1,D2):-
	H =.. [P|T],
	write(P),
	write('( '),
	writeTermList(T,D1,D2),
	write(' )'),
	!.
  
% writeTermList(+L,+D1,-D2)
writeTermList([],D1,D1):-
	!.
writeTermList([H],D1,D2):-
	writeTerm(H,D1,D2),
	!.
writeTermList([H|T],D1,D2):-
	writeTerm(H,D1,D3),
	write(', '),
	writeTermList(T,D3,D2),
	!.

?- writeTerm(p(X),[],D2), nl,
writeTerm(p(X),D2,D3), nl,
writeTerm(p(X),D3,D4), nl,
writeTerm(p(X),D4,D5), nl,
writeTerm(p(X),D5,D6), nl,
writeTerm(p(X),D6,D7), nl,
write(D7), nl.

