package com.teamhelper.imsdk.netcore.internal

import android.util.Log
import androidx.annotation.Keep
import com.google.gson.Gson
import com.teamhelper.imsdk.BusinessEventRegistry
import com.teamhelper.imsdk.base.EventRegistry
import com.teamhelper.imsdk.base.ServerEventType
import com.teamhelper.imsdk.handler.ProtocolHandler
import com.teamhelper.imsdk.netcore.ServerEventRegistry
import com.teamhelper.imsdk.protocol.ProtocolFactory
import com.teamhelper.imsdk.protocol.ProtocolWrapper

@Keep
internal class NetCoreLib {
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

        @JvmStatic
        var DEBUG = false

        /**
         * 连接打开回调
         * @calledByC++
         * @param response: String
         */
        @JvmStatic
        fun onConnectOpen(response: String) {
            EventRegistry.post(ServerEventType.onConnectOpen, response)
            ServerEventRegistry.executeEventHandler {
                it.onConnectOpen(response)
            }
            logD("onConnectOpen: $response")
        }

        /**
         * opCode Text消息接收回调
         * @calledByC++
         * @param message: String
         */
        @JvmStatic
        fun onTextMessageRecv(message: String) {
            EventRegistry.post(ServerEventType.onTextMessageRecv, message)
            ServerEventRegistry.executeEventHandler {
                it.onTextMessageRecv(message)
            }
            logD("onTextMessageRecv: $message")
        }

        /**
         * opCode Binary消息接收回调
         * @calledByC++
         * @param binary: ByteArray
         */
        @JvmStatic
        fun onBinaryMessageRecv(binary: ByteArray) {
            EventRegistry.post(ServerEventType.onBinaryMessageRecv, binary)
            ServerEventRegistry.executeEventHandler {
                it.onBinaryMessageRecv(binary)
            }
            logD("onBinaryMessageRecv: ${binary.size}")

            val protobuf = ProtocolFactory.convertBytesToProtobuf(binary)
            if (protobuf == null) {
                logD("onTextMessageRecv: Irregular protocol format, type == null")
                return
            }
            val dataContent = protobuf.dataContent
            val protocolHandler: ProtocolHandler<Any>? =
                BusinessEventRegistry.getProtocolHandler(protobuf.type) as ProtocolHandler<Any>?

            if (protocolHandler == null) {
                logD("onTextMessageRecv: unknown protocol")
                return
            }

            val fromJson = Gson().fromJson<ProtocolWrapper<*>>(dataContent, protocolHandler.getGenericType())
            protocolHandler.handle(fromJson as ProtocolWrapper<Any>)
        }

        /**
         * 连接关闭回调
         * @calledByC++
         */
        @JvmStatic
        fun onConnectClosed() {
            EventRegistry.post(ServerEventType.onConnectClosed)
            ServerEventRegistry.executeEventHandler {
                it.onConnectClosed()
            }
            logD("onConnectClosed")
        }

        /**
         * 重连回调
         * @calledByC++
         * @param retryCnt: Int 重连次数 从1开始
         * @param delay: Int 重连延迟时间 单位ms
         */
        @JvmStatic
        fun onReconnect(retryCnt: Int, delay: Int) {
            EventRegistry.post(ServerEventType.onReconnect, retryCnt, delay)
            ServerEventRegistry.executeEventHandler {
                it.onReconnect(retryCnt, delay)
            }
            logD("onReconnect retryCnt: $retryCnt, delay: $delay")
        }

        @JvmStatic
        private fun logD(str: String) {
            if (DEBUG) {
                Log.d(TAG, str)
            }
        }
    }
}