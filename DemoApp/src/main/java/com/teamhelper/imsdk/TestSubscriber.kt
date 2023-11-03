package com.teamhelper.imsdk

import android.util.Log
import com.teamhelper.imsdk.base.EventSubscriber
import com.teamhelper.imsdk.base.ServerEvent
import com.teamhelper.imsdk.base.ServerEventType

@EventSubscriber
class TestSubscriber : EventLifecycleSubscriber() {
    @ServerEvent(ServerEventType.onConnectClosed)
    fun onConnectClosed() {
        Log.e("TestSubscriber", "onConnectClosed")
    }

    @ServerEvent(ServerEventType.onReconnect)
    fun onReconnect(retryCnt: Int, delay: Int) {
        Log.e("TestSubscriber", "onReconnect")
    }
}