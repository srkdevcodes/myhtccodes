@echo off
cd /d %~dp0
ECHO.
echo ************************************************** ************
dir D:\ICRES\ICRES\src\com\cimb\icres\req\*.java
ECHO.
echo ************************************************** ************
ECHO.
set /p Name=Enter one Java file name from above list for execution:
javac -d ..\Classes %Name%.java
call D:\ICRES\ICRES\bin\com\cimb\icres\req\Classes.bat %1 %Name%
echo "Successfully Executed"