REM ETALIS PERFORMANCE TESTER v1.0
@echo OFF
cls
echo ETALIS PERFORMANCE TESTER v1.0
echo.
if EXIST %CD%\PerformanceTester.bat (
goto start 
) else (
echo Please run the PerformaceTester from its directory.
)
:start
setlocal
set prCommand=C:\SWI\bin\swipl
REM set /P etalisPath=Please Provide the ETALIS source path for the ETALIS version you want to test : 
set etalisPath=etalis/src_31_okt_2011
echo Using the following ETALIS path : %etalisPath%
echo Using the following Prolog compiler : %prCommand%
REM see specs.txt for the available tests
set /a count=0
REM wait 3 seconds before starting the Tests to enable user to abort.
echo Running the tests in 3 seconds ... 
echo Hit Ctrl+c to cancel.
ping -n 3 127.0.0.1 > nul
setlocal ENABLEDELAYEDEXPANSION
for %%T in (test1 , test2 , test3, test4) do (
	for %%A in (recent , unrestricted) do (
		for %%B in (off, on) do (
			for %%C in (off , on) do (							
				set /a count=count+1				
				if %%T == test1 set dat=r_500.P
				if %%T == test2 set dat=2_25000.P
				if %%T == test3 set dat=2_25000.P
				if %%T == test4 set dat=2_25000.P
				echo Test !count!: %%T using stream !dat! and with flags ^(Policy : %%A, Garbage Collector : %%B, Out-Of-Order : %%C^)
				%prCommand%  -g ^"['LoadStream.P'],['%etalisPath%/etalis.P'],set_etalis_flag^(event_consumption_policy,%%A^),set_etalis_flag^(out_of_order,%%C^),set_etalis_flag^(garbage_control,%%B^),compile_events^('%%T/test.event'^),profile^(test^('data/!dat!'^)^),halt.^"				
				))))

endlocal
endlocal
rem %prCommand% -q -g ^"['LoadStream.P'],['%etalisPath%/etalis.P'],set_etalis_flag^(event_consumption_policy,%%A^),set_etalis_flag^(out_of_order,%%C^),set_etalis_flag^(garbage_control,%%B^),compile_events^('%%T/test.event'^),profile^(test^('data/r_100.P'^)^),halt.^"	
