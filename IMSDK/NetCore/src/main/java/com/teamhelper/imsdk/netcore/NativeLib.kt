package com.teamhelper.imsdk.netcore

class NativeLib {

    /**
     * A native method that is implemented by the 'netcore' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'netcore' library on application startup.
        init {
            System.loadLibrary("netcore")
        }
    }
}