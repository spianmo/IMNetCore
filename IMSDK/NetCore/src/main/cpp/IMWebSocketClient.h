//
// Created by Finger Ebichu on 2023/10/23.
//

#ifndef IMSDKPROJECT_IMWEBSOCKETCLIENT_H
#define IMSDKPROJECT_IMWEBSOCKETCLIENT_H

#include "hv/WebSocketClient.h"

#include "util.h"

class IMWebSocketClient : public hv::WebSocketClient {
public:
    IMWebSocketClient(hv::EventLoopPtr loop = NULL) : hv::WebSocketClient(loop) {}

    ~IMWebSocketClient() {}

    int connect(const char *url) {
        onopen = [this]() {
            JNIEnv *env = getEnv();
            const HttpResponsePtr &resp = getHttpResponse();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCoreLib");
            jmethodID methodID = env->GetStaticMethodID(clazz, "onConnectOpen",
                                                        "(Ljava/lang/String;)V");
            env->CallStaticVoidMethod(clazz, methodID, env->NewStringUTF(resp->body.c_str()));
            LOGE("onOpen\n%s\n", resp->body.c_str());
        };
        onmessage = [this](const std::string &msg) {
            JNIEnv *env = getEnv();
            auto ws_opcode = opcode();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCoreLib");
            if (ws_opcode == WS_OPCODE_TEXT) {
                jmethodID methodID = env->GetStaticMethodID(clazz, "onTextMessageRecv",
                                                            "(Ljava/lang/String;)V");
                env->CallStaticVoidMethod(clazz, methodID, env->NewStringUTF(msg.c_str()));
            } else if (ws_opcode == WS_OPCODE_BINARY) {
                jmethodID methodID = env->GetStaticMethodID(clazz, "onBinaryMessageRecv",
                                                            "([B)V");
                env->CallStaticVoidMethod(clazz, methodID,
                                          env->NewDirectByteBuffer((void *) msg.c_str(),
                                                                   msg.size()));
            }
            LOGE("onMessage(type=%s len=%d): %.*s\n",
                 ws_opcode == WS_OPCODE_TEXT ? "text" : "binary",
                 (int) msg.size(), (int) msg.size(), msg.data());
        };
        onclose = []() {
            JNIEnv *env = getEnv();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCoreLib");
            jmethodID methodID = env->GetStaticMethodID(clazz, "onConnectClosed",
                                                        "()V");
            env->CallStaticVoidMethod(clazz, methodID);
            LOGE("onClose\n");
        };

        JNIEnv *env = getEnv();

        jclass clazz = findClass("com/teamhelper/imsdk/netcore/config/WebsocketConfig");
        // MAX_CONTENT_LENGTH = 8 * 1024
        jfieldID fieldID = env->GetStaticFieldID(clazz, "MAX_CONTENT_LENGTH", "I");
        jint max_content_length = env->GetStaticIntField(clazz, fieldID);
        // setMaxContentLength(max_content_length);

        // READER_IDLE_TIME_SECONDS = 5
        fieldID = env->GetStaticFieldID(clazz, "IDLE_PING_INTERVAL", "I");
        jint all_idle_time_seconds = env->GetStaticIntField(clazz, fieldID);
        setPingInterval(all_idle_time_seconds);

        // CONNECT_TIMEOUT = 10 * 1000
        fieldID = env->GetStaticFieldID(clazz, "CONNECT_TIMEOUT", "I");
        jint connect_timeout = env->GetStaticIntField(clazz, fieldID);
        setConnectTimeout(connect_timeout);

        reconn_setting_t reconn;
        reconn_setting_init(&reconn);
        reconn.min_delay = 1000;
        reconn.max_delay = 10000;
        reconn.delay_policy = 0;
        setReconnect(&reconn);

        http_headers headers;
        return open(url, headers);
    }
};


#endif //IMSDKPROJECT_IMWEBSOCKETCLIENT_H
