//
// Created by David on 2018/4/26 0026.
//

#ifndef ANDROIDPOTATO_LOG_H
#define ANDROIDPOTATO_LOG_H
#include <android/log.h>
#define LOG_TAG "FromJni"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)


#endif //ANDROIDPOTATO_LOG_H
