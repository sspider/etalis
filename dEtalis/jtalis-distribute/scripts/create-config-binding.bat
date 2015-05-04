@echo OFF

:: Get script directory
set SCRIPT_DIR=%~dp0

xjc -p com.jtalis.core.config.beans config.xsd -d "%SCRIPT_DIR%/../src/main/java"
