//
// Created by Finger Ebichu on 2023/10/30.
//

#ifndef IMSDKPROJECT_UTIL_H
#define IMSDKPROJECT_UTIL_H

#include <android/log.h>
#include "util.h"

JavaVM *jvm = NULL;


bool get_jni_env(JNIEnv **env) {
    bool did_attach_thread = false;
    *env = NULL;
    // Check if the current thread is attached to the VM
    auto get_env_result = jvm->GetEnv((void **) env, JNI_VERSION_1_6);
    if (get_env_result == JNI_EDETACHED) {
        if (jvm->AttachCurrentThread(env, NULL) == JNI_OK) {
            did_attach_thread = true;
        }
    } else if (get_env_result == JNI_EVERSION) {
        // Unsupported JNI version. Throw an exception if you want to.
    }
    return did_attach_thread;
}

#define LOG_TAG  "=======WebSocket========>"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG ,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,LOG_TAG ,__VA_ARGS__)

#endif //IMSDKPROJECT_UTIL_H
