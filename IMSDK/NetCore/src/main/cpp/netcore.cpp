#include <jni.h>
#include <string>
#include "base/HttpHandlerRegistry.h"
#include "base/WebSocketHandler.h"
#include "IMWebSocketClient.h"
#include "IMTcpSocketClient.h"
#include "IMUdpSocketClient.h"
#include <iostream>

std::map<int, void *> clientMap;


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    gJvm = vm;  // cache the JavaVM pointer
    auto env = getEnv();
    //replace with one of your classes in the line below
    auto randomClass = env->FindClass("com/teamhelper/imsdk/netcore/NetCore");
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
JNIEXPORT jint JNICALL
Java_com_teamhelper_imsdk_netcore_NetCore_nativeConnect(JNIEnv *env, jobject thiz,
                                                        jint socket_protocol,
                                                        jstring host, jint port, jboolean tls) {
    const char *_host = env->GetStringUTFChars(host, 0);
    if (socket_protocol == 0) {
        std::string url = tls ? "wss://" : "ws://";
        url += _host;
        url += ":";
        url += std::to_string(port);
        auto clientPtr = new IMWebSocketClient();
        int fd = clientPtr->connect(thiz, url.c_str());
        clientMap.insert(std::pair<int, IMWebSocketClient *>(fd, clientPtr));
        return fd;
    } else if (socket_protocol == 1) {
        auto clientPtr = new IMTcpSocketClient();
        int fd = clientPtr->connect(thiz, _host, port, tls);
        clientMap.insert(std::pair<int, IMTcpSocketClient *>(fd, clientPtr));
        return fd;
    } else if (socket_protocol == 2) {
        auto clientPtr = new IMUdpSocketClient();
        int fd = clientPtr->connect(thiz, _host, port);
        clientMap.insert(std::pair<int, IMUdpSocketClient *>(fd, clientPtr));
        return fd;
    }
    env->ReleaseStringUTFChars(host, _host);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_teamhelper_imsdk_netcore_NetCore_nativeConnectWs(JNIEnv *env, jobject thiz,
                                                          jstring ws_url) {
    const char *url = env->GetStringUTFChars(ws_url, 0);
    auto clientPtr = new IMWebSocketClient();
    int fd = clientPtr->connect(thiz, url);
    clientMap.insert(std::pair<int, IMWebSocketClient *>(fd, clientPtr));
    env->ReleaseStringUTFChars(ws_url, url);
    return fd;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_NetCore_nativeSendBinaryMessage(JNIEnv *env, jobject thiz,
                                                                  jint fd, jbyteArray req) {
    jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
    jfieldID fieldID = env->GetFieldID(clazz, "socketProtocol",
                                       "I");
    jint socketProtocol = env->GetIntField(thiz, fieldID);
    if (clientMap[fd] == nullptr) {
        hloge("SocketClient is not initialized, send binary message failed");
        return;
    }
    jbyte *data = env->GetByteArrayElements(req, 0);
    jsize len = env->GetArrayLength(req);

    if (socketProtocol == 0) {
        auto clientPtr = (IMWebSocketClient *) clientMap[fd];
        if (!clientPtr->isConnected()) {
            hloge("WebSocketClient is not connected, send binary message failed");
            return;
        }

        clientPtr->send((const char *) data, len, WS_OPCODE_BINARY);
    } else if (socketProtocol == 1) {
        auto clientPtr = (IMTcpSocketClient *) clientMap[fd];
        if (!clientPtr->isConnected()) {
            hloge("TcpSocketClient is not connected, send binary message failed");
            return;
        }
        if (clientPtr->channel->isWriteComplete()) {
            clientPtr->channel->write(data, len);
        }
    } else if (socketProtocol == 2) {
        auto clientPtr = (IMUdpSocketClient *) clientMap[fd];
        if (clientPtr->channel->isWriteComplete()) {
            clientPtr->sendto(data, len);
        }
    }

    env->ReleaseByteArrayElements(req, data, 0);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_NetCore_nativeSendTextMessage(JNIEnv *env, jobject thiz,
                                                                jint fd, jstring req) {
    jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
    jfieldID fieldID = env->GetFieldID(clazz, "socketProtocol",
                                       "I");
    jint socketProtocol = env->GetIntField(thiz, fieldID);
    if (clientMap[fd] == nullptr) {
        hloge("SocketClient is not initialized, send binary message failed");
        return;
    }

    const char *data = env->GetStringUTFChars(req, 0);

    if (socketProtocol == 0) {
        auto clientPtr = (IMWebSocketClient *) clientMap[fd];
        if (!clientPtr->isConnected()) {
            hloge("WebSocketClient is not connected, send text message failed");
            return;
        }
        clientPtr->send(data);
    } else if (socketProtocol == 1) {
        auto clientPtr = (IMTcpSocketClient *) clientMap[fd];
        if (!clientPtr->isConnected()) {
            hloge("WebSocketClient is not connected, send text message failed");
            return;
        }
        clientPtr->send(data);
    } else if (socketProtocol == 2) {
        auto clientPtr = (IMUdpSocketClient *) clientMap[fd];
        clientPtr->sendto(data);
    }

    env->ReleaseStringUTFChars(req, data);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_NetCore_nativeClose(JNIEnv *env, jobject thiz, jint fd) {
    jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
    jfieldID fieldID = env->GetFieldID(clazz, "socketProtocol",
                                       "I");
    jint socketProtocol = env->GetIntField(thiz, fieldID);
    if (clientMap[fd] == nullptr) {
        hloge("SocketClient is not initialized, close failed");
        return;
    }

    if (socketProtocol == 0) {
        auto clientPtr = (IMWebSocketClient *) clientMap[fd];
        clientPtr->close();
    } else if (socketProtocol == 1) {
        auto clientPtr = (IMTcpSocketClient *) clientMap[fd];
        clientPtr->closesocket();
    } else if (socketProtocol == 2) {
        auto clientPtr = (IMUdpSocketClient *) clientMap[fd];
        clientPtr->closesocket();
    }

    clientMap[fd] = nullptr;
    clientMap.erase(fd);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_teamhelper_imsdk_netcore_NetCore_nativeIsConnected(JNIEnv *env, jobject thiz, jint fd) {
    jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
    jfieldID fieldID = env->GetFieldID(clazz, "socketProtocol",
                                       "I");
    jint socketProtocol = env->GetIntField(thiz, fieldID);
    if (clientMap[fd] == nullptr) {
        hloge("SocketClient is not initialized, can not get connect status");
        return FALSE;
    }

    if (socketProtocol == 0) {
        auto clientPtr = (IMWebSocketClient *) clientMap[fd];
        return clientPtr->isConnected();
    } else if (socketProtocol == 1) {
        auto clientPtr = (IMTcpSocketClient *) clientMap[fd];
        return clientPtr->isConnected();
    }
    return FALSE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_teamhelper_imsdk_netcore_NetCore_nativeIsReconnect(JNIEnv *env, jobject thiz, jint fd) {
    jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
    jfieldID fieldID = env->GetFieldID(clazz, "socketProtocol",
                                       "I");
    jint socketProtocol = env->GetIntField(thiz, fieldID);
    if (clientMap[fd] == nullptr) {
        hloge("SocketClient is not initialized, can not get reconnect status");
        return FALSE;
    }

    if (socketProtocol == 0) {
        auto clientPtr = (IMWebSocketClient *) clientMap[fd];
        return clientPtr->isReconnect();
    } else if (socketProtocol == 1) {
        auto clientPtr = (IMTcpSocketClient *) clientMap[fd];
        return clientPtr->isReconnect();
    }
    return FALSE;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_NetCore_nativeStop(JNIEnv *env, jobject thiz, jint fd) {
    jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
    jfieldID fieldID = env->GetFieldID(clazz, "socketProtocol",
                                       "I");
    jint socketProtocol = env->GetIntField(thiz, fieldID);
    if (clientMap[fd] == nullptr) {
        hloge("SocketClient is not initialized, stop failed");
        return;
    }

    if (socketProtocol == 0) {
        auto clientPtr = (IMWebSocketClient *) clientMap[fd];
        clientPtr->stop();
    } else if (socketProtocol == 1) {
        auto clientPtr = (IMTcpSocketClient *) clientMap[fd];
        clientPtr->stop();
    } else if (socketProtocol == 2) {
        auto clientPtr = (IMUdpSocketClient *) clientMap[fd];
        clientPtr->stop();
    }

    clientMap[fd] = nullptr;
    clientMap.erase(fd);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_teamhelper_imsdk_netcore_NetCore_nativeStart(JNIEnv *env, jobject thiz, jint fd) {
    jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
    jfieldID fieldID = env->GetFieldID(clazz, "socketProtocol",
                                       "I");
    jint socketProtocol = env->GetIntField(thiz, fieldID);
    if (clientMap[fd] == nullptr) {
        hloge("SocketClient is not initialized, start failed");
        return;
    }

    if (socketProtocol == 0) {
        auto clientPtr = (IMWebSocketClient *) clientMap[fd];
        clientPtr->start();
    } else if (socketProtocol == 1) {
        auto clientPtr = (IMTcpSocketClient *) clientMap[fd];
        clientPtr->start();
    } else if (socketProtocol == 2) {
        auto clientPtr = (IMUdpSocketClient *) clientMap[fd];
        clientPtr->start();
    }
}