@echo off
rem ofdrw.bat - OFDRW OFD Document Tool launcher (Windows)
rem Place this file in the same directory as ofdrw-cli-2.3.9.jar, or set OFDRW_JAR.

if not defined OFDRW_JAR (
    set OFDRW_JAR=%~dp0ofdrw-cli-2.3.9.jar
)

if not exist "%OFDRW_JAR%" (
    echo ERROR: Cannot find %OFDRW_JAR%
    echo Set OFDRW_JAR environment variable to the correct path.
    exit /b 1
)

java -jar "%OFDRW_JAR%" %*
