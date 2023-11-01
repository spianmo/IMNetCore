package com.teamhelper.imsdk.netcore

import android.util.Log
import androidx.annotation.Keep
import com.google.gson.Gson
import com.teamhelper.imsdk.netcore.handler.ProtocolHandler
import com.teamhelper.imsdk.netcore.protocol.Protocol

@Keep
class NetCoreLib {
    external fun connect(wsUrl: String)
    external fun close()
    external fun sendTextMessage(req: String)
    external fun sendBinaryMessage(req: ByteArray)
    external fun isConnected(): Boolean
    external fun isReconnect(): Boolean


    companion object {
        init {
            System.loadLibrary("netcore")
        }

        @JvmStatic
        private val TAG = NetCoreLib::class.java.simpleName

        /**
         * 连接打开回调
         * @calledByC++
         * @param response: String
         */
        @JvmStatic
        fun onConnectOpen(response: String) {
            Log.e(TAG, "onConnectOpen: $response")
        }

        /**
         * opCode Text消息接收回调
         * @calledByC++
         * @param message: String
         */
        @JvmStatic
        fun onTextMessageRecv(message: String) {
            Log.e(TAG, "onTextMessageRecv: $message")

            val anyProtocol = Gson().fromJson(message, Protocol::class.java)
            if (anyProtocol.type == null) {
                Log.e(TAG, "onTextMessageRecv: Irregular protocol format, type == null")
                return
            }
            val protocolHandler: ProtocolHandler<Any>? =
                NetEventRegistry.getProtocolHandler(anyProtocol.type) as ProtocolHandler<Any>?

            if (protocolHandler == null) {
                Log.e(TAG, "onTextMessageRecv: unknown protocol")
                return
            }

            val fromJson = Gson().fromJson<Protocol<*>>(message, protocolHandler.getGenericType())
            protocolHandler.handle(fromJson as Protocol<Any>)
        }

        /**
         * opCode Binary消息接收回调
         * @calledByC++
         * @param binary: ByteArray
         */
        @JvmStatic
        fun onBinaryMessageRecv(binary: ByteArray) {
            Log.e(TAG, "onBinaryMessageRecv: ${binary.size}")
        }

        /**
         * 连接关闭回调
         * @calledByC++
         */
        @JvmStatic
        fun onConnectClosed() {
            Log.e(TAG, "onConnectClosed")
        }

        /**
         * 重连回调
         * @calledByC++
         * @param retryCnt: Int 重连次数 从1开始
         * @param delay: Int 重连延迟时间 单位ms
         */
        @JvmStatic
        fun onReconnect(retryCnt: Int, delay: Int) {
            Log.e(TAG, "onReconnect retryCnt: $retryCnt, delay: $delay")
        }
    }
}