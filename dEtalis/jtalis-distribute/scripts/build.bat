@echo OFF

:: Get script directory
set SCRIPT_DIR=%~dp0

call "%SCRIPT_DIR%\jtalis-setup.bat"
PATH = %SWI_LIB_PATH%;%SWI_BIN_PATH%;%PATH%;

call mvn -f "%SCRIPT_DIR%\..\pom.xml" install dependency:copy-dependencies

pause
