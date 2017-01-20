#!/bin/bash

clear

echo running disc changer test app...

sudo java -jar raspberry-pi-disc-changer.jar "$@"

echo complete.

