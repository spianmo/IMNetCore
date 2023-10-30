#include <jni.h>
#include <string>
#include "base/HttpHandlerRegistry.h"
#include "base/WebSocketHandler.h"
#include "IMWebSocketClient.h"
#include "openssl/opensslv.h"
#include <iostream>

IMWebSocketClient *clientPtr = nullptr;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGE("JNI load");
    jvm = vm;

    JNIEnv *env;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        LOGE("JNI load GetEnv failed");
        return -1;
    }
    return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM *vm, void *reserved) {
    LOGE("JNI unload");

    JNIEnv *env;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK)
        LOGE("JNI load GetEnv failed");
}


extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_NetCoreLib_connect(JNIEnv *env, jobject thiz, jstring ws_url) {
    const char *url = env->GetStringUTFChars(ws_url, 0);
    clientPtr = new IMWebSocketClient();
    clientPtr->connect(url);
    env->ReleaseStringUTFChars(ws_url, url);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_NetCoreLib_sendBinaryMessage(JNIEnv *env, jobject thiz,
                                                               jbyteArray req) {
    jbyte *data = env->GetByteArrayElements(req, 0);
    jsize len = env->GetArrayLength(req);
    clientPtr->send((const char *) data, len);
    env->ReleaseByteArrayElements(req, data, 0);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_NetCoreLib_sendTextMessage(JNIEnv *env, jobject thiz,
                                                             jstring req) {
    const char *data = env->GetStringUTFChars(req, 0);
    clientPtr->send(data);
    env->ReleaseStringUTFChars(req, data);
}