echo off

cd /d %~dp0

PATH=C:\tools\zulufx-jdk-11\bin;%PATH%
set RUNTIME_PATH=out\runtime
set PROPERTY_DIR=out\production\AntennaDirectionViewer\antenna\direction
set PROPERTY_FILE=AntennaDirectionView.properties

if exist %RUNTIME_PATH% (
   echo #INFO removing old runtime directory
   rmdir /S /Q %RUNTIME_PATH%
)

echo #INFO replace version name in properties file.
SET /P VERSION= < version.txt
if %VERSION%=="" (
    echo #ERROR failed to read version from version file.
    goto :error_exit
)
ren %PROPERTY_DIR%\%PROPERTY_FILE% %PROPERTY_FILE%_
setlocal enabledelayedexpansion
for /f "delims=" %%I in (%PROPERTY_DIR%\%PROPERTY_FILE%_) do (
    set line=%%I
    echo !line:#VERSION_TO_BE_REPLACED#=%VERSION%!>>%PROPERTY_DIR%\%PROPERTY_FILE%
)
endlocal

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
copy antennadirectionviewer.bat %RUNTIME_PATH%\bin\
if errorlevel 1 (
    echo #ERROR failed to copy launcher batch file in runtime
    goto :error_exit
)

echo #INFO copy version file in runtime
copy version.txt %RUNTIME_PATH%
if errorlevel 1 (
    echo #ERROR failed to copy version file in runtime
    goto :error_exit
)

echo #INFO archive runtime image
powershell compress-archive -force out/runtime antenna_pointing_viewer-%VERSION%.zip
if errorlevel 1 (
    echo #ERROR failed to archive runtime image
    goto :error_exit
)

:success
echo #INFO succeeded to build runtime image.
pause
exit

:error_exit
echo #ERROR failed to build runtime image.
pause