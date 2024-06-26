//
// Created by Finger Ebichu on 2023/10/23.
//

#ifndef IMSDKPROJECT_IMWEBSOCKETCLIENT_H
#define IMSDKPROJECT_IMWEBSOCKETCLIENT_H

#include "hv/WebSocketClient.h"

#include "util.h"

class IMWebSocketClient : public hv::WebSocketClient {
private:
    jobject pJobject;
public:
    IMWebSocketClient(hv::EventLoopPtr loop = NULL) : hv::WebSocketClient(loop) {

    }

    ~IMWebSocketClient() {
        JNIEnv *env = getEnv();
        env->DeleteGlobalRef(pJobject);
    }

    int connect(jobject _pJobject, const char *url) {
        hloge("IMWebSocketClient::connect(%s)\n", url);
        JNIEnv *_env = getEnv();
        this->pJobject = _env->NewGlobalRef(_pJobject);
        onopen = [this]() {
            JNIEnv *env = getEnv();
            const HttpResponsePtr &resp = getHttpResponse();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
            jmethodID methodID = env->GetMethodID(clazz, "onConnectOpen",
                                                  "(Ljava/lang/String;)V");
            env->CallVoidMethod(pJobject, methodID, env->NewStringUTF(resp->body.c_str()));
            hloge("onOpen\n%s\n", resp->body.c_str());
        };
        onmessage = [this](const std::string &msg) {
            JNIEnv *env = getEnv();
            auto ws_opcode = opcode();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
            if (ws_opcode == WS_OPCODE_TEXT) {
                jmethodID methodID = env->GetMethodID(clazz, "onTextMessageRecv",
                                                      "(Ljava/lang/String;)V");
                env->CallVoidMethod(pJobject, methodID, env->NewStringUTF(msg.c_str()));
            } else if (ws_opcode == WS_OPCODE_BINARY) {
                jmethodID methodID = env->GetMethodID(clazz, "onBinaryMessageRecv",
                                                      "([B)V");
                size_t size = msg.size();
                jbyteArray byteArray = env->NewByteArray(size);
                env->SetByteArrayRegion(byteArray, 0, size, (jbyte *) msg.c_str());
                env->CallVoidMethod(pJobject, methodID, byteArray);
                env->DeleteLocalRef(byteArray);
            }
            hloge("onMessage(type=%s len=%d): %.*s\n",
                  ws_opcode == WS_OPCODE_TEXT ? "text" : "binary",
                  (int) msg.size(), (int) msg.size(), msg.data());
        };
        onclose = [this]() {
            JNIEnv *env = getEnv();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
            jmethodID methodID = env->GetMethodID(clazz, "onConnectClosed", "(ILjava/lang/String;)V");
            env->CallVoidMethod(pJobject, methodID, channel->error(), env->NewStringUTF(socket_strerror(channel->error())));
            hloge("onClose: %d %s\n", channel->error(), socket_strerror(channel->error()));
        };
        onConnection = [this](const TSocketChannelPtr &) {
            JNIEnv *env = getEnv();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/config/WebSocketConfig");

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
        onWriteComplete = [this](const hv::SocketChannelPtr &channel, hv::Buffer *buf) {
            JNIEnv *env = getEnv();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
            jmethodID methodID = env->GetMethodID(clazz, "onWriteComplete",
                                                  "([B)V");
            const char *data = (const char *) buf->data();
            size_t size = buf->size();
            jbyteArray byteArray = env->NewByteArray(size);
            env->SetByteArrayRegion(byteArray, 0, size, (jbyte *) data);
            env->CallVoidMethod(pJobject, methodID, byteArray);
            env->DeleteLocalRef(byteArray);
            hloge("onWriteComplete(type=%s len=%d): %.*s\n", "binary",
                  (int) size, (int) size, data);
        };
        onReconnect = [this](uint32_t cur_retry_cnt, uint32_t cur_delay) {
            JNIEnv *env = getEnv();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
            jmethodID methodID = env->GetMethodID(clazz, "onReconnect",
                                                  "(II)V");
            env->CallVoidMethod(pJobject, methodID, cur_retry_cnt, cur_delay);
            hloge("onReconnect\n");
        };

        JNIEnv *env = getEnv();
        jclass clazz = findClass("com/teamhelper/imsdk/netcore/config/WebSocketConfig");

        // READER_IDLE_TIME_SECONDS = 5
        jfieldID fieldID = env->GetStaticFieldID(clazz, "PING_INTERVAL", "I");
        jint all_idle_time_seconds = env->GetStaticIntField(clazz, fieldID);
        setPingInterval(all_idle_time_seconds);

        // CONNECT_TIMEOUT = 10 * 1000
        fieldID = env->GetStaticFieldID(clazz, "CONNECT_TIMEOUT", "I");
        jint connect_timeout = env->GetStaticIntField(clazz, fieldID);
        setConnectTimeout(connect_timeout);

        // ENABLE_RECONNECT = true
        fieldID = env->GetStaticFieldID(clazz, "ENABLE_RECONNECT", "Z");
        jboolean enable_reconnect = env->GetStaticBooleanField(clazz, fieldID);

        if (enable_reconnect) {
            reconn_setting_t reconnSetting;
            reconn_setting_init(&reconnSetting);
            // RECONNECT_MIN_DELAY = 1000
            fieldID = env->GetStaticFieldID(clazz, "RECONNECT_MIN_DELAY", "I");
            jint min_delay = env->GetStaticIntField(clazz, fieldID);
            reconnSetting.min_delay = min_delay;
            // RECONNECT_MAX_DELAY = 10000
            fieldID = env->GetStaticFieldID(clazz, "RECONNECT_MAX_DELAY", "I");
            jint max_delay = env->GetStaticIntField(clazz, fieldID);
            reconnSetting.max_delay = max_delay;
            // RECONNECT_DELAY_POLICY = 0
            fieldID = env->GetStaticFieldID(clazz, "RECONNECT_DELAY_POLICY", "I");
            jint delay_policy = env->GetStaticIntField(clazz, fieldID);
            reconnSetting.delay_policy = delay_policy;
            setReconnect(&reconnSetting);
        }

        http_headers headers;
        open(url, headers);
        return this->channel->fd();
    }
};


#endif //IMSDKPROJECT_IMWEBSOCKETCLIENT_H
