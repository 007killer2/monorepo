#!/bin/bash
#
echo"Hallo Anatolii"
mkdir -p /opt/040324-wde/Anatolii/

for i in {1..10}; do
   `date +%T` >> "\$file_path"
    sleep 5
done
