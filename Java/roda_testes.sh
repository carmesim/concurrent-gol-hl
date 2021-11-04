#!/bin/bash
./run_hl.sh $1 1  | grep -o "[0-9]*" | head -n1 > out.txt
./run_hl.sh $1 2  | grep -o "[0-9]*" | head -n1 >> out.txt
./run_hl.sh $1 4  | grep -o "[0-9]*" | head -n1 >> out.txt
./run_hl.sh $1 8  | grep -o "[0-9]*" | head -n1 >> out.txt

