#!/bin/bash
./roda_testes.sh 10;
cat out.txt > output/out_10.txt
./roda_testes.sh 50;
cat out.txt > output/out_50.txt
./roda_testes.sh 250;
cat out.txt > output/out_250.txt
./roda_testes.sh 750;
cat out.txt > output/out_750.txt
./roda_testes.sh 1000;
cat out.txt > output/out_1000.txt
./roda_testes.sh 2048;
cat out.txt > output/out_2048.txt
