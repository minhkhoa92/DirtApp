#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_de_minh_smartar_dirtapp_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_de_minh_smartar_dirtapp_MainActivity_stringGetExt(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}