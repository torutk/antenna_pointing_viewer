cd /d %~dp0
PATH=C:\tools\zulufx-jdk-11\bin;%PATH%
jlink --no-header-files --no-man-pages --compress=2 --strip-debug ^
 --module-path out\production\AntennaDirectionViewer;"C:\Program Files\Java\jserialcomm\jSerialComm-2.4.0.jar" ^
 --add-modules AntennaDirectionViewer ^
 --output out\runtime ^
 --launcher antennadirectionviewer=AntennaDirectionViewer/antenna.direction.AntennaDirectionApp
 
pause
