#include <jni.h>
#include <string>
#include "openssl/ssl.h"
#include <iostream>

extern "C" JNIEXPORT jstring JNICALL
Java_com_teamhelper_imsdk_netcore_NativeLib_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    std::cout << "OpenSSL Version: " << OPENSSL_VERSION_TEXT << std::endl;
    std::cout << "OpenSSL Version Number: " << OPENSSL_VERSION_NUMBER << std::endl;
    return env->NewStringUTF(OPENSSL_VERSION_TEXT);
}