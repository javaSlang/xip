@echo off
set /p login=Enter login: 
set /p password=Enter password:
set /p tag=Enter key tag:
java -jar xip-1.0.jar %login% %password% %tag%