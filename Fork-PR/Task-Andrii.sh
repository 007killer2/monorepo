#!/bin/bash

#

echo "My script"

FILE= "/opt/040324-wde/Andrii/MeTest"

for i in {1..10}
do
CURRENT_TIME=$(date +"%H:%M:%S")

echo $CURRENT_TIME >> "$FILE"

sleep 5  

done
