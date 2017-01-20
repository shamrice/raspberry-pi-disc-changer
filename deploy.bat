@echo off

REM set variables
SET putty="C:\putty.exe"
SET tempScriptFile=temp.sh
SET tempZipFile=target.zip
SET tempFile=ftp.dat
SET serverIp=192.168.2.250
SET username=pi
SET password=password
SET artifactsDirectory="C:\Erik-GitHub\raspberry-pi-disc-changer\raspberry-pi-disc-changer\out\artifacts\raspberry_pi_disc_changer_jar_and_config\"
SET jarFilename="raspberry-pi-disc-changer.jar"
SET configFilename="config.properties"
SET bootImageFileName="boot.bmp"
SET idleImageFileName="idle.bmp"
SET destLocation=disc_changer

REM zip current artifacts into a zip using the java jar command
cd %artifactsDirectory%
jar -cMf %tempZipFile% .

REM generate temp ftp script
echo open %serverIp% > %tempFile%
echo user %username% %password% >> %tempFile%
echo binary >> %tempFile%
echo cd %destLocation% >> %tempFile%
echo put %artifactsDirectory%%tempZipFile% >> %tempFile%
echo disconnect >> %tempFile%
echo bye >> %tempFile%

REM ftp new jar file to Pi
ftp -n -s:%tempFile%

REM clean up and unpack new artifacts on remote server.
echo cd %destLocation% >> %tempScriptFile%
echo unzip -o %tempZipFile%  >> %tempScriptFile%
echo rm %tempScriptFile% >> %tempScriptFile%

%putty% -ssh %serverIp% -l %username% -pw %password% -m %tempScriptFile%

REM delete temp files.
del %tempFile%
del %tempZipFile%
del %tempScriptFile%