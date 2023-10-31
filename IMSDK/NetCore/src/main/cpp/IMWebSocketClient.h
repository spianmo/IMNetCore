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
        onConnection = [this](const TSocketChannelPtr &) {
            JNIEnv *env = getEnv();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/config/WebsocketConfig");

            // MAX_CONTENT_LENGTH = 8 * 1024
            jfieldID fieldID = env->GetStaticFieldID(clazz, "MAX_CONTENT_LENGTH", "I");
            jint max_content_length = env->GetStaticIntField(clazz, fieldID);
            unpack_setting->package_max_length = max_content_length;
            channel->setUnpack(unpack_setting);

            // KEEPALIVE_TIMEOUT = -1
            fieldID = env->GetStaticFieldID(clazz, "KEEPALIVE_TIMEOUT", "I");
            jint keepalive_timeout = env->GetStaticIntField(clazz, fieldID);
            channel->setKeepaliveTimeout(keepalive_timeout);

            // READ_TIMEOUT = 5 * 1000
            fieldID = env->GetStaticFieldID(clazz, "READ_TIMEOUT", "I");
            jint read_timeout = env->GetStaticIntField(clazz, fieldID);
            channel->setReadTimeout(read_timeout);

            // WRITE_TIMEOUT = 5 * 1000
            fieldID = env->GetStaticFieldID(clazz, "WRITE_TIMEOUT", "I");
            jint write_timeout = env->GetStaticIntField(clazz, fieldID);
            channel->setWriteTimeout(write_timeout);
        };
        onReconnect = [this](uint32_t cur_retry_cnt, uint32_t cur_delay) {
            JNIEnv *env = getEnv();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCoreLib");
            jmethodID methodID = env->GetStaticMethodID(clazz, "onReconnect",
                                                        "(II)V");
            env->CallStaticVoidMethod(clazz, methodID, cur_retry_cnt, cur_delay);
            LOGE("onReconnect\n");
        };

        JNIEnv *env = getEnv();
        jclass clazz = findClass("com/teamhelper/imsdk/netcore/config/WebsocketConfig");

        // READER_IDLE_TIME_SECONDS = 5
        jfieldID fieldID = env->GetStaticFieldID(clazz, "PING_INTERVAL", "I");
        jint all_idle_time_seconds = env->GetStaticIntField(clazz, fieldID);
        setPingInterval(all_idle_time_seconds);

        // CONNECT_TIMEOUT = 10 * 1000
        fieldID = env->GetStaticFieldID(clazz, "CONNECT_TIMEOUT", "I");
        jint connect_timeout = env->GetStaticIntField(clazz, fieldID);
        setConnectTimeout(connect_timeout);

        reconn_setting_t reconnSetting;
        reconn_setting_init(&reconnSetting);
        reconnSetting.min_delay = 1000;
        reconnSetting.max_delay = 10000;
        reconnSetting.delay_policy = 0;
        setReconnect(&reconnSetting);

        http_headers headers;
        return open(url, headers);
    }
};


#endif //IMSDKPROJECT_IMWEBSOCKETCLIENT_H
