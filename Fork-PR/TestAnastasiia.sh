#!/bin/bash

touch myTest

for i in {1..10}
do
CURRENT_TIME=$(date +"%H:%M:%S")

echo $CURRENT_TIME >> myTest

sleep 5

done

cat myTest
