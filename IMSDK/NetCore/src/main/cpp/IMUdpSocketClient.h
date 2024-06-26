//
// Created by Finger Ebichu on 2024/6/25.
//

#ifndef IMSDKPROJECT_IMUDPSOCKETCLIENT_H
#define IMSDKPROJECT_IMUDPSOCKETCLIENT_H

#include <jni.h>
#include "hv/UdpClient.h"
#include "hv/htime.h"

#include "util.h"

class IMUdpSocketClient : public hv::UdpClient {
private:
    jobject pJobject;
public:
    IMUdpSocketClient(hv::EventLoopPtr loop = NULL) : hv::UdpClient(loop) {

    }

    ~IMUdpSocketClient() {
        JNIEnv *env = getEnv();
        env->DeleteGlobalRef(pJobject);
    }

    int connect(jobject _pJobject, const char *remote_host, int remote_port) {
        hloge("IMUdpSocketClient::connect(%s:%d)\n", remote_host, remote_port);
        JNIEnv *_env = getEnv();
        this->pJobject = _env->NewGlobalRef(_pJobject);
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

        int connfd = createsocket(remote_port, remote_host);
        if (connfd < 0) {
            return -20;
        }
        hloge("client connect to port %d, connfd=%d ...\n", remote_port, connfd);

        JNIEnv *env = getEnv();
        jclass clazz = findClass("com/teamhelper/imsdk/netcore/config/UdpSocketConfig");

        // READER_IDLE_TIME_SECONDS = 5
        jfieldID fieldID = env->GetStaticFieldID(clazz, "CONNECT_TIMEOUT", "I");
        jint connect_timeout = env->GetStaticIntField(clazz, fieldID);
        channel->setConnectTimeout(connect_timeout);

        // ENABLE_RECONNECT = true
        fieldID = env->GetStaticFieldID(clazz, "ENABLE_KCP", "Z");
        jboolean enable_kcp = env->GetStaticBooleanField(clazz, fieldID);

        if (enable_kcp) {
            kcp_setting_t kcpSetting;
            fieldID = env->GetStaticFieldID(clazz, "KCP_CONV", "I");
            jint kcp_conv = env->GetStaticIntField(clazz, fieldID);
            kcpSetting.conv = kcp_conv;
            fieldID = env->GetStaticFieldID(clazz, "KCP_NODELAY", "I");
            jint kcp_nodelay = env->GetStaticIntField(clazz, fieldID);
            kcpSetting.nodelay = kcp_nodelay;
            fieldID = env->GetStaticFieldID(clazz, "KCP_INTERVAL", "I");
            jint kcp_interval = env->GetStaticIntField(clazz, fieldID);
            kcpSetting.interval = kcp_interval;
            fieldID = env->GetStaticFieldID(clazz, "KCP_FAST_RESEND", "I");
            jint kcp_fast_resend = env->GetStaticIntField(clazz, fieldID);
            kcpSetting.fastresend = kcp_fast_resend;
            fieldID = env->GetStaticFieldID(clazz, "KCP_NOCWND", "I");
            jint kcp_nocwnd = env->GetStaticIntField(clazz, fieldID);
            kcpSetting.nocwnd = kcp_nocwnd;
            fieldID = env->GetStaticFieldID(clazz, "KCP_SNDWND", "I");
            jint kcp_sndwnd = env->GetStaticIntField(clazz, fieldID);
            kcpSetting.sndwnd = kcp_sndwnd;
            fieldID = env->GetStaticFieldID(clazz, "KCP_RCVWND", "I");
            jint kcp_rcvwnd = env->GetStaticIntField(clazz, fieldID);
            kcpSetting.rcvwnd = kcp_rcvwnd;
            fieldID = env->GetStaticFieldID(clazz, "KCP_MTU", "I");
            jint kcp_mtu = env->GetStaticIntField(clazz, fieldID);
            kcpSetting.mtu = kcp_mtu;
            fieldID = env->GetStaticFieldID(clazz, "KCP_UPDATE_INTERVAL", "I");
            jint kcp_update_interval = env->GetStaticIntField(clazz, fieldID);
            kcpSetting.update_interval = kcp_update_interval;
            setKcp(kcp_setting);
        }

        start();
        return connfd;
    }
};

#endif //IMSDKPROJECT_IMUDPSOCKETCLIENT_H
