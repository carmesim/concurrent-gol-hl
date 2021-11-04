#!/bin/bash
echo "======== GAME OF LIFE ========"
./run_gol.sh 2048 1 > result_gol/saida1.txt
cat result_gol/saida1.txt
./run_gol.sh 2048 2 > result_gol/saida2.txt
cat result_gol/saida2.txt
./run_gol.sh 2048 4 > result_gol/saida4.txt
cat result_gol/saida4.txt
./run_gol.sh 2048 8 > result_gol/saida8.txt
cat result_gol/saida8.txt
echo "======== HIGH LIFE ========"
./run_hl.sh 2048 1 > result_hl/saida1.txt
cat result_hl/saida1.txt
./run_hl.sh 2048 2 > result_hl/saida2.txt
cat result_hl/saida2.txt
./run_hl.sh 2048 4 > result_hl/saida4.txt
cat result_hl/saida4.txt
./run_hl.sh 2048 8 > result_hl/saida8.txt
cat result_hl/saida8.txt

