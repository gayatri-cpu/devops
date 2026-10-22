@REM ----------------------------------------------------------------------------
@REM Maven Wrapper for VaxGuard Vaccination Reminder Portal (Windows)
@REM ----------------------------------------------------------------------------
@echo off
set DIRNAME=%~dp0
if exist "%DIRNAME%.tools\apache-maven-3.9.6\bin\mvn.cmd" (
    call "%DIRNAME%.tools\apache-maven-3.9.6\bin\mvn.cmd" %*
) else (
    mvn %*
)
