#include <jni.h>
#include <iostream>
#include <windows.h>

#include "controls.h"

using namespace std;

JNIEXPORT void JNICALL Java_snakepackage_Snake_controls (JNIEnv *env, jclass){

    jclass snakeClass = env -> FindClass("snakepackage/Snake");

    jmethodID controlUp = env -> GetStaticMethodID(snakeClass, "controlUp", "()V");
    jmethodID controlRight = env -> GetStaticMethodID(snakeClass, "controlRight", "()V");
    jmethodID controlDown = env -> GetStaticMethodID(snakeClass, "controlDown", "()V");
    jmethodID controlLeft = env -> GetStaticMethodID(snakeClass, "controlLeft", "()V");
    jmethodID controlExit = env -> GetStaticMethodID(snakeClass, "controlExit", "()V");

    RegisterHotKey(NULL, 1, 0x00, VK_UP);
    RegisterHotKey(NULL, 2, 0x00, VK_RIGHT);
    RegisterHotKey(NULL, 3, 0x00, VK_DOWN);
    RegisterHotKey(NULL, 4, 0x00, VK_LEFT);

    RegisterHotKey(NULL, 0, 0x00, VK_ESCAPE);

    MSG msg = {0};
    while (GetMessage(&msg, NULL, 0, 0)){
        if (msg.message == WM_HOTKEY){
            int id = (int) msg.wParam;

            switch (id){
                case 0: 
                    env -> CallStaticVoidMethod(snakeClass, controlExit);
                    break; 
                case 1: // up
                    env -> CallStaticVoidMethod(snakeClass, controlUp);
                    break; 
                case 2: // right
                    env -> CallStaticVoidMethod(snakeClass, controlRight);
                    break; 
                case 3: // down
                    env -> CallStaticVoidMethod(snakeClass, controlDown);
                    break; 
                case 4: // left
                    env -> CallStaticVoidMethod(snakeClass, controlLeft);
                    break; 
                default: break;
            }
        }
    }

}