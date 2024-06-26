
# Borescope Viewer

### Description

This project attempts to incorporate [mkarr/boroscope_stream_fixer](https://github.com/mkarr/boroscope_stream_fixer) into a basic Android app, creating a foundation for customizing functionality around a Borescope camera feed.

### Concept

The design concept can be summarized as follows:

- Support build/execution of embedded C/C++ (see [Android NDK](https://developer.android.com/ndk/guides/concepts)
- Support interaction between embedded C/C++ and Java/Kotlin (see [Java Native Interface (JNI)](https://developer.android.com/training/articles/perf-jni)).
- Modify the `main` function in [bsf.c](https://github.com/mkarr/boroscope_stream_fixer/blob/master/bsf.c) (now `bsfConnect()`) to be callable from the app, receive the JNI environment, perform debug logging, etc.
- Create a thread for the embedded C code to execute asynchronously, connecting to the specified Borescope camera, or any compatible/comparable video stream, over TCP.
- Deliver the "unencrypted" frame data back to the main app activity to be rendered using a video player library, perhaps [ExoPlayer](https://github.com/google/ExoPlayer).

### Development

This project is being developed in Android Studio. Clone and import the project and commit any changes that are required to get the project functioning correctly.

If another tool is used, please don't modify files which might interfere with development in Android Studio.

### Issues

As of the latest commit in this branch, it does not work. Probably porting [gsf.c](https://github.com/mkarr/boroscope_stream_fixer/blob/master/bsf.c) to Java/Kotlin would make things a lot easier, but I'm not familiar enough with C to produce a port without loads of new quirks and bugs.

See the [issues](https://github.com/gazj/borescope-viewer-for-android/issues) for remaining to-dos and known challenges which may or may not have known solutions.


