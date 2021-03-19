# TKOM_Currency_Language
Interpreter for my own programming language, supporting operations on currency data types.

Program reads input file sign by sign, then lexer create tokens, later that tokens are parsed by parser. If there aren't any errors, program will run given code and present results.

## Installation
```
cd src
javac -d Main.java
```


## Running
Program get 2 input paths for file with: program code, currencies converter matrix.

Example files paths: ../inputs/program.txt ../inputs/currencies.txt
```
java Main <path to program code> <path to currencies converter matrix>
```

## Documentation
In folder called "dokumentacja" there are files presenting informations about created language, such as:

- syntax tutorial

- gramma

- strengths and weaknesses
