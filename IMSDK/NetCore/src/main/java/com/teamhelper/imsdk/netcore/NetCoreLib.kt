package com.teamhelper.imsdk.netcore

import android.util.Log
import androidx.annotation.Keep
import com.teamhelper.imsdk.netcore.constant.BreakReason
import com.teamhelper.imsdk.netcore.constant.Platform
import com.teamhelper.imsdk.netcore.core.ServerGlobalContext
import com.teamhelper.imsdk.netcore.event.ServerEventListener
import com.teamhelper.imsdk.netcore.protocol.Protocol

@Keep
class NetCoreLib {
    external fun connect(wsUrl: String)
    external fun close()
    external fun sendTextMessage(req: String)
    external fun sendBinaryMessage(req: ByteArray)


    companion object {
        init {
            System.loadLibrary("netcore")
        }

        @JvmStatic
        private val TAG = NetCoreLib::class.java.simpleName

        @JvmStatic
        fun onConnectOpen(response: String) {
            Log.e(TAG, "onConnectOpen: $response")
        }

        @JvmStatic
        fun onTextMessageRecv(message: String) {
            Log.e(TAG, "onTextMessageRecv: $message")
        }

        @JvmStatic
        fun onBinaryMessageRecv(binary: ByteArray) {
            Log.e(TAG, "onBinaryMessageRecv: ${binary.size}")
        }

        @JvmStatic
        fun onConnectClosed() {
            Log.e(TAG, "onConnectClosed")
        }
    }

    init {
        ServerGlobalContext.setServerEventListener(object : ServerEventListener {
            override fun onUserLoginEvent(
                uid: String?,
                platform: Platform?,
                token: String?,
                extendData: String?
            ) {
                Log.e(TAG, "onUserLoginEvent: $uid")
            }

            override fun onUserOnlineEvent(uid: String?, platform: Platform?, extendData: String?) {
                Log.e(TAG, "onUserOnlineEvent: $uid")
            }

            override fun onUserOfflineEvent(
                uid: String?,
                platform: Platform?,
                reason: BreakReason?
            ) {
                Log.e(TAG, "onUserOfflineEvent: $uid")
            }

            override fun <T : Any?> onMessageReceivedEvent(p: Protocol<T>?) {
                Log.e(TAG, "onMessageReceivedEvent: ${p?.dataContent}")
            }

            override fun onMessageHandleSuccessEvent(p: Protocol<*>?) {
                Log.e(TAG, "onMessageHandleSuccessEvent: ${p?.dataContent}")
            }

            override fun onMessageHandleFailedEvent(p: Protocol<*>?) {
                Log.e(TAG, "onMessageHandleFailedEvent: ${p?.dataContent}")
            }

            override fun onHeartbeatEvent(p: Protocol<*>?) {
                Log.e(TAG, "onHeartbeatEvent: ${p?.dataContent}")
            }

            override fun onSessionReadTimeOut() {
                Log.e(TAG, "onSessionReadTimeOut")
            }

            override fun onSessionWriterTimeOut() {
                Log.e(TAG, "onSessionWriterTimeOut")
            }

            override fun onHeartbeatTimeOut() {
                Log.e(TAG, "onHeartbeatTimeOut")
            }

        })
    }
}