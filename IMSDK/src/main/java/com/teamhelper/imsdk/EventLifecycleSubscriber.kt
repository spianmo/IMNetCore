package com.teamhelper.imsdk

import android.util.Log

open class EventLifecycleSubscriber {
    fun release() {
        Log.d(javaClass.simpleName, "release")
    }
}