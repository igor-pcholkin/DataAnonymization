@echo off

setlocal enabledelayedexpansion

REM Get the Java version
for /f tokens^=2-5^ delims^=.-_^" %%v in ('java -fullversion 2^>^&1') do set "version=%%v"

if [!version!] == [] (
    echo Java 19, required by this application is not found.
    echo Please install it and add a reference to the java executable to the PATH variable.
    exit /b 1
)

REM Check if the Java version is less than 19
if !version! lss 19 (
    echo Java version is !version! which is less than 19, required by this application.
    echo Please install it and add a reference to the java executable to the PATH variable.
    exit /b 1
)

REM Run the 'dataprotection.jar' JAR file with any additional command-line arguments
java -jar dataprotection.jar %*

:end