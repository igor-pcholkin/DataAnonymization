@echo off
setlocal enabledelayedexpansion

REM Find the path of the Java executable in the system's PATH
for /f "delims=" %%i in ('where java') do set "java_cmd=%%i"

REM Check if java_cmd is not empty, indicating that the 'java' executable is in the PATH
if not "!java_cmd!"=="" (
    echo found java executable in PATH
) else (
    REM If 'java' is not in the PATH, check if JAVA_HOME is set and points to a valid 'java' executable
    if defined JAVA_HOME (
        if exist "!JAVA_HOME!\bin\java.exe" (
            echo found java executable in JAVA_HOME
            set "java_cmd=!JAVA_HOME!\bin\java.exe"
        ) else (
            echo Java 19 not found, please download and install it first.
            exit /b 1
        )
    ) else (
        echo Java 19 not found, please download and install it first.
        exit /b 1
    )
)

REM Get the Java version
for /f "tokens=3" %%v in ('!java_cmd! -version 2^>^&1 ^| find "version"') do set "version=%%v"

echo !java_cmd!
echo version !version!

REM Check if the Java version is less than 19
if !version! lss 19 (
    echo version is less than 19
    exit /b 1
)

REM Run the 'dataprotection.jar' JAR file with any additional command-line arguments
!java_cmd! -jar dataprotection.jar %*

:end