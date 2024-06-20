#include <jni.h>
#include <string>
#include "base/HttpHandlerRegistry.h"
#include "base/WebSocketHandler.h"
#include "IMWebSocketClient.h"
#include <iostream>

IMWebSocketClient *clientPtr = nullptr;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    gJvm = vm;  // cache the JavaVM pointer
    auto env = getEnv();
    //replace with one of your classes in the line below
    auto randomClass = env->FindClass("com/teamhelper/imsdk/netcore/internal/NetCoreLib");
    jclass classClass = env->GetObjectClass(randomClass);
    auto classLoaderClass = env->FindClass("java/lang/ClassLoader");
    auto getClassLoaderMethod = env->GetMethodID(classClass, "getClassLoader",
                                                 "()Ljava/lang/ClassLoader;");
    gClassLoader = env->NewGlobalRef(env->CallObjectMethod(randomClass, getClassLoaderMethod));
    gFindClassMethod = env->GetMethodID(classLoaderClass, "findClass",
                                        "(Ljava/lang/String;)Ljava/lang/Class;");
    return JNI_VERSION_1_6;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_internal_NetCoreLib_connect(JNIEnv *env, jobject thiz,
                                                              jstring ws_url) {
    if (clientPtr != nullptr) {
        LOGE("WebSocketClient is already initialized");
        return;
    }
    const char *url = env->GetStringUTFChars(ws_url, 0);
    clientPtr = new IMWebSocketClient();
    clientPtr->connect(url);
    env->ReleaseStringUTFChars(ws_url, url);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_internal_NetCoreLib_sendBinaryMessage(JNIEnv *env, jobject thiz,
                                                                        jbyteArray req) {
    if (clientPtr == nullptr) {
        LOGE("WebSocketClient is not initialized, send binary message failed");
        return;
    }
    if (!clientPtr->isConnected()) {
        LOGE("WebSocketClient is not connected, send binary message failed");
        return;
    }
    jbyte *data = env->GetByteArrayElements(req, 0);
    jsize len = env->GetArrayLength(req);
    clientPtr->send((const char *) data, len, WS_OPCODE_BINARY);
    env->ReleaseByteArrayElements(req, data, 0);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_internal_NetCoreLib_sendTextMessage(JNIEnv *env, jobject thiz,
                                                                      jstring req) {
    if (clientPtr == nullptr) {
        LOGE("WebSocketClient is not initialized, send text message failed");
        return;
    }
    if (!clientPtr->isConnected()) {
        LOGE("WebSocketClient is not connected, send text message failed");
        return;
    }
    const char *data = env->GetStringUTFChars(req, 0);
    clientPtr->send(data);
    env->ReleaseStringUTFChars(req, data);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_internal_NetCoreLib_close(JNIEnv *env, jobject thiz) {
    if (clientPtr == nullptr) {
        LOGE("WebSocketClient is not initialized, close failed");
        return;
    }
    clientPtr->close();
    clientPtr = nullptr;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_teamhelper_imsdk_netcore_internal_NetCoreLib_isConnected(JNIEnv *env, jobject thiz) {
    if (clientPtr == nullptr) {
        LOGE("WebSocketClient is not initialized, can not get connect status");
        return FALSE;
    }
    return clientPtr->isConnected();
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_teamhelper_imsdk_netcore_internal_NetCoreLib_isReconnect(JNIEnv *env, jobject thiz) {
    if (clientPtr == nullptr) {
        LOGE("WebSocketClient is not initialized, can not get reconnect status");
        return FALSE;
    }
    return clientPtr->isReconnect();
}