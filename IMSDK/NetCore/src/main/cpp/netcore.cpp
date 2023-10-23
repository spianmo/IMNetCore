#include <jni.h>
#include <string>
#include "IMWebSocketClient.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_teamhelper_imsdk_netcore_NativeLib_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    auto client = new IMWebSocketClient();
    client->connect("");
    return env->NewStringUTF(hello.c_str());
}