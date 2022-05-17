# Snake_Java_using_jni

This is a simple snake game.

was develovment using Java with JNI to call C++ code.


My Java version: 11.0.12 2021-07-20 LTS

My C++ compiler version: gcc version 11.3.0 (Rev1, Built by MSYS2 project)

## My commands used
Compile and generate .h file
```
javac -h . "hereyour.java"
```

Compile c++ file
``` 
g++ -c -I"C:\Program Files\Java\jdk-11.0.12\include" -I"C:\Program Files\Java\jdk-11.0.12\include\win32" libCompile/controls.cpp -o libCompile/controls.o
```

Generate .dll file
``` 
g++ -shared -o lib/controls.dll libCompile/controls.o
```
 
Dowload [Java](https://www.oracle.com/java/technologies/downloads)
Dowload [Gcc](https://code.visualstudio.com/docs/cpp/config-mingw)