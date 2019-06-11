@echo on
set JLINK_VM_OPTIONS=-Xms64m -Xmx64m -Xss256k ^
-XX:TieredStopAtLevel=1 -XX:CICompilerCount=2 -XX:CompileThreshold=1500 ^
-XX:InitialCodeCacheSize=160k -XX:ReservedCodeCacheSize=32m ^
-XX:MetaspaceSize=12m ^
-XX:+UseSerialGC ^
-Dantenna.direction.boundary.serialport1=COM1 ^
-Dantenna.direction.boundary.serialport2=COM2
set DIR=%~dp0
"%DIR%\java" %JLINK_VM_OPTIONS% -m AntennaDirectionViewer/antenna.direction.AntennaDirectionApp %*
