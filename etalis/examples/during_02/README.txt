
I slightly modified an example for DURING (and committed to SVN) to demonstrate that the time stamp is not calculated correctly for this operator. For this program:
 
c(X) <- a(X) 'seq' b(X).
d(X) <- a0(X) 'seq' b0(X).

e(X) <- c(X) 'during' d(X).


And this input:
*Event: a0(1) @ [datime(2011,8,22,18,51,2,1),datime(2011,8,22,18,51,2,1)]
*Event: a(1) @ [datime(2011,8,22,18,51,2,2),datime(2011,8,22,18,51,2,2)]
*Event: b(1) @ [datime(2011,8,22,18,51,2,3),datime(2011,8,22,18,51,2,3)]
*Event: c(1) @ [datime(2011,8,22,18,51,2,2),datime(2011,8,22,18,51,2,3)]
*Event: b0(1) @ [datime(2011,8,22,18,51,2,4),datime(2011,8,22,18,51,2,4)]
*Event: d(1) @ [datime(2011,8,22,18,51,2,1),datime(2011,8,22,18,51,2,4)]
*Event: e(1) @ [datime(2011,8,22,18,51,2,2),datime(2011,8,22,18,51,2,4)]
during_01                       passed

T1 of e(1) should be equal to T1 of d(1), i.e., not equal to T1 of c(1). This means we should get:
*Event: e(1) @ [datime(2011,8,22,18,51,2,1),datime(2011,8,22,18,51,2,4)]

Could you please correct this small mistake? (You can delete my change from SVN once you correct it).
Also when I manually input events, ETALIS does not produce e(1) and does produce c(1) for the same input as the one from .bat file. No clue why is it so?

