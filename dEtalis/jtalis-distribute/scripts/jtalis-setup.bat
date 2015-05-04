@echo OFF

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Get script directory
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
set SCRIPT_DIR=%~dp0

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Get SWI_HOME_DIR
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if not defined SWI_HOME_DIR (
	call set /p SWI_HOME_DIR=Enter your SWI Prolog home directory:
)

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Find export SWI_LIB_PATH and SWI_BIN_PATH
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
call set SWI_LIB_PATH=%SWI_HOME_DIR%\lib
call set SWI_BIN_PATH=%SWI_HOME_DIR%\bin
echo [SETUP INFO] SWI_LIB_PATH found at: %SWI_LIB_PATH%
echo [SETUP INFO] SWI_BIN_PATH found at: %SWI_BIN_PATH%

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Install Interprolog and JPL dependencies in Maven repo
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if not EXIST %HOMEDRIVE%%HOMEPATH%\.m2\com\veskogeorgiev\probin\1.0.0\probin-1.0.0.jar (
	echo [SETUP INFO] Installing probin-1.0.0.jar in local Maven repository ...
	call mvn install:install-file^
	 -Dfile="%SCRIPT_DIR%\libs\probin-1.0.0.jar"^
	 -DgroupId=com.veskogeorgiev^
	 -DartifactId=probin^
	 -Dversion=1.0.0^
	 -Dpackaging=jar^
	 -DgeneratePom=true
)
if not EXIST %HOMEDRIVE%%HOMEPATH%\.m2\repository\interprolog\interprolog\1.0\interprolog-1.0.jar (
	echo [SETUP INFO] Installing Interprolog.jar in local Maven repository ...
	call mvn install:install-file^
	 -Dfile="%SCRIPT_DIR%\libs\interprolog.jar"^
	 -DgroupId=interprolog^
	 -DartifactId=interprolog^
	 -Dversion=1.0^
	 -Dpackaging=jar^
	 -DgeneratePom=true
)
if not EXIST %HOMEDRIVE%%HOMEPATH%\.m2\repository\jpl\jpl\1.0\jpl-1.0.jar (
	echo [SETUP INFO] Installing jpl.jar in local Maven repository ...
	call mvn install:install-file^
	 -Dfile="%SWI_HOME_DIR%\lib\jpl.jar"^
	 -DgroupId=jpl^
	 -DartifactId=jpl^
	 -Dversion=1.0^
	 -Dpackaging=jar^
	 -DgeneratePom=true
)
if not EXIST %HOMEDRIVE%%HOMEPATH%\.m2\repository\org\apache\incubator\kafka\kafka\0.7.2\kafka-0.7.2.jar (
	echo [SETUP INFO] Installing kafka-0.7.2.jar in local Maven repository ...
	call mvn install:install-file^
	 -Dfile="%SCRIPT_DIR%/libs/kafka-0.7.2.jar"^
	 -DgroupId=org.apache.incubator.kafka^
	 -DartifactId=kafka^
	 -Dversion=0.7.2^
	 -Dpackaging=jar^
	 -DgeneratePom=true
)

