@echo off

REM set variables
SET tempFile=ftp.dat
SET serverIp=192.168.2.250
SET username=pi
SET password=password
SET jarLocation="C:\Erik-GitHub\raspberry-pi-disc-changer\raspberry-pi-disc-changer\out\artifacts\raspberry_pi_disc_changer_jar\raspberry-pi-disc-changer.jar"
SET destLocation=disc_changer

REM generate temp ftp script
echo open %serverIp% > %tempFile%
echo user %username% %password% >> %tempFile%
echo binary >> %tempFile%
echo cd %destLocation% >> %tempFile%
echo put %jarLocation% >> %tempFile%
echo disconnect >> %tempFile%
echo bye >> %tempFile%

REM ftp new jar file to Pi
ftp -n -s:%tempFile%

REM delete temp file.
del %tempFile%