#include <jni.h>
#include <android/log.h>
#include <stddef.h>
#include "../util/test.h"
#include "../util/log.h"



JNIEXPORT jint JNICALL
Java_com_davidzhou_library_features_JniFeature_getRandom(JNIEnv *env, jclass type) {
    // TODO
    return get_random();
}

JNIEXPORT jstring JNICALL
Java_com_davidzhou_library_features_JniFeature_getFormatString(JNIEnv *env, jclass type,
                                                               jstring msg_) {
    const char *msg = (*env)->GetStringUTFChars(env, msg_, 0);
    LOGI("%s", msg);
    // TODO
    const char *fmt = "[yyMMdd]";
    (*env)->ReleaseStringUTFChars(env, msg_, msg);

    return (*env)->NewStringUTF(env, fmt);
}