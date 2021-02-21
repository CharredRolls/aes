#!/bin/bash

cd /scratch
git clone https://github.com/CharredRolls/aes 
cd aes
javac App.java
java App $1