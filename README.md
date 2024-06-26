
# Borescope Viewer

### Description

This project aims to provide basic streaming functionality when connected to a Borescope camera device, eliminating the need to use the official app.

### Concept

The design concept can be summarized as follows:

- Embed a modified version of [mkarr/boroscope_stream_fixer](https://github.com/mkarr/boroscope_stream_fixer).
  - Uses [Android NDK](https://developer.android.com/ndk/guides/concepts) to build/execute embedded C/C++. 
  - Uses [Java Native Interface (JNI)](https://developer.android.com/training/articles/perf-jni) to facilitate interactions with native code.
- Execute native code in its own thread, connecting to a specified input URI (i.e. the Borescope camera's TCP socket).
- Serve "unencrypted" frame data using a local TCP socket to be rendered using a video player library -- a `VideoView` element or perhaps [ExoPlayer](https://github.com/google/ExoPlayer).

### Development

This project is being developed in [Android Studio](https://developer.android.com/studio). Clone and import the project and commit any changes that are required to get the project functioning correctly.

If another tool is used, please don't modify files which might interfere with development in Android Studio.

### Issues

As of the latest commit in this branch, it does not work. Probably porting [gsf.c](https://github.com/mkarr/boroscope_stream_fixer/blob/master/bsf.c) to Java/Kotlin would make things a lot easier, but I'm not familiar enough with C to produce a port without loads of new quirks and bugs.

See the [issues](https://github.com/gazj/borescope-viewer-for-android/issues) for remaining to-dos and known challenges which may or may not have known solutions.

### Contributions

Contributions are accepted informally. Just fork the project and create an issue describing the changes, then submit a pull request which includes those changes.

