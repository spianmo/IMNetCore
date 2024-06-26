package com.teamhelper.imsdk.netcore

import android.util.Log
import androidx.annotation.Keep
import com.google.gson.Gson
import com.teamhelper.imsdk.BusinessEventRegistry
import com.teamhelper.imsdk.base.EventRegistry
import com.teamhelper.imsdk.base.ServerEventType
import com.teamhelper.imsdk.handler.ProtocolHandler
import com.teamhelper.imsdk.protocol.ProtocolFactory
import com.teamhelper.imsdk.protocol.ProtocolWrapper

@Keep
open class NetCore {
    private var fd = -1
    private var socketProtocol: Int = -1
    private external fun nativeConnect(socketProtocol: Int, host: String, port: Int, tls: Boolean): Int
    private external fun nativeConnectWs(wsUrl: String): Int
    private external fun nativeClose(fd: Int)
    private external fun nativeStart(fd: Int)
    private external fun nativeStop(fd: Int)
    private external fun nativeSendTextMessage(fd: Int, req: String)
    private external fun nativeSendBinaryMessage(fd: Int, req: ByteArray)
    private external fun nativeIsConnected(fd: Int): Boolean
    private external fun nativeIsReconnect(fd: Int): Boolean

    /**
     * connect
     * @When Websocket
     */
    fun connect(wsUrl: String) {
        if (fd <= 0) {
            fd = nativeConnectWs(wsUrl)
            this@NetCore.socketProtocol = SocketProtocol.WEBSOCKET.value
        } else {
            logD("connect: already connected")
        }
    }

    /**
     * connect
     * @When TCP,Websocket,Udp
     */
    fun connect(socketProtocol: SocketProtocol, host: String, port: Int, tls: Boolean) {
        if (fd <= 0) {
            fd = nativeConnect(socketProtocol.value, host, port, tls)
            this@NetCore.socketProtocol = socketProtocol.value
        } else {
            if (!isConnected()) {
                nativeStart(fd)
                logD("connect: reconnecting")
                return
            }
            logD("connect: already connected")
        }
    }

    /**
     * close
     * @When TCP,Websocket,Udp
     */
    fun close() {
        if (fd > 0) {
            nativeClose(fd)
            fd = -1
            this@NetCore.socketProtocol = -1
        } else {
            logD("close: already closed")
        }
    }

    /**
     * sendTextMessage
     * @When Websocket
     */
    fun sendTextMessage(req: String) {
        if (fd > 0) {
            nativeSendTextMessage(fd, req)
        } else {
            logD("sendTextMessage: not connected")
        }
    }

    /**
     * sendBinaryMessage
     * @When TCP,Websocket,Udp
     */
    fun sendBinaryMessage(req: ByteArray) {
        if (fd > 0) {
            nativeSendBinaryMessage(fd, req)
        } else {
            logD("sendBinaryMessage: not connected")
        }
    }

    /**
     * isConnected
     * @When TCP,Websocket
     */
    fun isConnected(): Boolean {
        return if (fd > 0) {
            nativeIsConnected(fd)
        } else {
            logD("isConnected: not connected")
            false
        }
    }

    /**
     * isReconnect
     * @When TCP,Websocket
     */
    fun isReconnect(): Boolean {
        return if (fd > 0) {
            nativeIsReconnect(fd)
        } else {
            logD("isReconnect: not connected")
            false
        }
    }

    /**
     * 连接打开回调
     * @calledByC++
     * @When TCP,Websocket
     * @param response: String
     */
    private fun onConnectOpen(response: String) {
        EventRegistry.post(ServerEventType.onConnectOpen, this, response)
        ServerEventRegistry.executeEventHandler {
            it.onConnectOpen(this, response)
        }
        logD("onConnectOpen: $response")
    }

    /**
     * opCode Text消息接收回调
     * @calledByC++
     * @When Websocket
     * @param message: String
     */
    private fun onTextMessageRecv(message: String) {
        EventRegistry.post(ServerEventType.onTextMessageRecv, this, message)
        ServerEventRegistry.executeEventHandler {
            it.onTextMessageRecv(this, message)
        }
        logD("onTextMessageRecv: $message")
    }

    /**
     * opCode Binary消息接收回调
     * @calledByC++
     * @When TCP,Websocket,Udp
     * @param binary: ByteArray
     */
    private fun onBinaryMessageRecv(binary: ByteArray) {
        EventRegistry.post(ServerEventType.onBinaryMessageRecv, this, binary)
        ServerEventRegistry.executeEventHandler {
            it.onBinaryMessageRecv(this, binary)
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
     * 写入完成回调
     * @calledByC++
     * @When TCP,Websocket,Udp
     */
    private fun onWriteComplete(binary: ByteArray) {
        EventRegistry.post(ServerEventType.onWriteComplete, this)
        ServerEventRegistry.executeEventHandler {
            it.onWriteComplete(this, binary)
        }
        logD("onWriteComplete")
    }

    /**
     * 连接关闭回调
     * @calledByC++
     * @When TCP,Websocket
     */
    private fun onConnectClosed(code: Int, reason: String) {
        EventRegistry.post(ServerEventType.onConnectClosed, this, code, reason)
        ServerEventRegistry.executeEventHandler {
            it.onConnectClosed(this, code, reason)
        }
        logD("onConnectClosed")
    }

    /**
     * 重连回调
     * @calledByC++
     * @When TCP,Websocket
     * @param retryCnt: Int 重连次数 从1开始
     * @param delay: Int 重连延迟时间 单位ms
     */
    private fun onReconnect(retryCnt: Int, delay: Int) {
        EventRegistry.post(ServerEventType.onReconnect, this, retryCnt, delay)
        ServerEventRegistry.executeEventHandler {
            it.onReconnect(this, retryCnt, delay)
        }
        logD("onReconnect retryCnt: $retryCnt, delay: $delay")
    }

    private fun logD(str: String) {
        if (DEBUG) {
            Log.d(TAG, str)
        }
    }


    companion object {
        init {
            System.loadLibrary("netcore")
        }

        @JvmStatic
        private val TAG = NetCore::class.java.simpleName

        @JvmStatic
        var DEBUG = true

        enum class SocketProtocol(val value: Int) {
            WEBSOCKET(0),
            TCP(1),
            UDP(2);
        }
    }
}