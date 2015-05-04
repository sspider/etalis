@echo OFF

:: Get script directory
set SCRIPT_DIR=%~dp0

call "%SCRIPT_DIR%\jtalis-setup.bat"
PATH = %SWI_LIB_PATH%;%SWI_BIN_PATH%;%PATH%;

call mvn -f "%1" exec:java -Dexec.mainClass="com.jtalis.core.JtalisMain" -Dexec.args="%2"

pause