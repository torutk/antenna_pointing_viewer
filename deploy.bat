echo off

cd /d %~dp0

PATH=C:\tools\zulufx-jdk-11\bin;%PATH%
set RUNTIME_PATH=out\runtime

if exist %RUNTIME_PATH% (
   echo #INFO removing old runtime directory
   rmdir /S /Q %RUNTIME_PATH%
)

echo #INFO jlink to generate custom runtime image under %RUNTIME_PATH%
jlink --no-header-files --no-man-pages --compress=2 --strip-debug ^
 --module-path out\production\AntennaDirectionViewer;"C:\Program Files\Java\jserialcomm\jSerialComm-2.4.0.jar" ^
 --add-modules AntennaDirectionViewer ^
 --output  %RUNTIME_PATH% ^
 --launcher antennadirectionviewer=AntennaDirectionViewer/antenna.direction.AntennaDirectionApp

if errorlevel 1 (
    echo #ERROR jlink failed
    goto :error_exit
)

echo #INFO copy launcher batch file in runtime
copy antennadirectionviewer.bat out\runtime\bin\

if errorlevel 1 (
    echo #ERROR failed to copy launcher batch file in runtime
    goto :error_exit
)

:success
echo #INFO succeeded to build runtime image.
pause
exit

:error_exit
echo #ERROR failed to build runtime image.
pause