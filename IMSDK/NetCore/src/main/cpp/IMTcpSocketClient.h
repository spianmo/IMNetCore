//
// Created by Finger Ebichu on 2023/10/23.
//

#ifndef IMSDKPROJECT_IMTCPSOCKETCLIENT_H
#define IMSDKPROJECT_IMTCPSOCKETCLIENT_H

#include <jni.h>
#include "hv/TcpClient.h"
#include "hv/htime.h"

#include "util.h"

class IMTcpSocketClient : public hv::TcpClient {
private:
    jobject pJobject;
public:
    IMTcpSocketClient(hv::EventLoopPtr loop = NULL) : hv::TcpClient(loop) {

    }

    ~IMTcpSocketClient() {
        JNIEnv *env = getEnv();
        env->DeleteGlobalRef(pJobject);
    }

    int connect(jobject _pJobject, const char *remote_host, int remote_port, bool tls = false) {
        hloge("IMTcpSocketClient::connect(%s:%d)\n", remote_host, remote_port);
        JNIEnv *_env = getEnv();
        this->pJobject = _env->NewGlobalRef(_pJobject);
        onConnection = [this](const TSocketChannelPtr &) {
            JNIEnv *env = getEnv();
            if (channel->isConnected()) {
                hloge("onOpen\n%s\n", "connected");
                jclass clazz = findClass("com/teamhelper/imsdk/netcore/config/TcpSocketConfig");

                // KEEPALIVE_TIMEOUT = -1
                jfieldID fieldID = env->GetStaticFieldID(clazz, "KEEPALIVE_TIMEOUT", "I");
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

                jclass clazzNetCoreLib = findClass("com/teamhelper/imsdk/netcore/NetCore");
                jmethodID methodID = env->GetMethodID(clazzNetCoreLib, "onConnectOpen",
                                                      "(Ljava/lang/String;)V");
                env->CallVoidMethod(pJobject, methodID, env->NewStringUTF("connected"));
            } else {
                hloge("onClose: %d %s\n", channel->error(), socket_strerror(channel->error()));
                jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
                jmethodID methodID = env->GetMethodID(clazz, "onConnectClosed",
                                                      "(ILjava/lang/String;)V");
                env->CallVoidMethod(pJobject, methodID, channel->error(), env->NewStringUTF(socket_strerror(channel->error())));
                hloge("onClose: %d %s\n", channel->error(), socket_strerror(channel->error()));
            }
        };
        onMessage = [this](const hv::SocketChannelPtr &channel, hv::Buffer *buf) {
            JNIEnv *env = getEnv();
            jclass clazz = findClass("com/teamhelper/imsdk/netcore/NetCore");
            jmethodID methodID = env->GetMethodID(clazz, "onBinaryMessageRecv",
                                                  "([B)V");
            const char *data = (const char *) buf->data();
            size_t size = buf->size();
            jbyteArray byteArray = env->NewByteArray(size);
            env->SetByteArrayRegion(byteArray, 0, size, (jbyte *) data);
            env->CallVoidMethod(pJobject, methodID, byteArray);
            env->DeleteLocalRef(byteArray);
            hloge("onMessage(type=%s len=%d): %.*s\n", "binary",
                  (int) size, (int) size, data);
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

        int connfd = createsocket(remote_port, remote_host);
        if (connfd < 0) {
            return -20;
        }
        hloge("client connect to port %d, connfd=%d ...\n", remote_port, connfd);

        JNIEnv *env = getEnv();
        jclass clazz = findClass("com/teamhelper/imsdk/netcore/config/TcpSocketConfig");

        // READER_IDLE_TIME_SECONDS = 5
        jfieldID fieldID = env->GetStaticFieldID(clazz, "CONNECT_TIMEOUT", "I");
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

        if (tls) {
            withTLS();
        }
        start();
        return connfd;
    }
};


#endif //IMSDKPROJECT_IMTCPSOCKETCLIENT_H
