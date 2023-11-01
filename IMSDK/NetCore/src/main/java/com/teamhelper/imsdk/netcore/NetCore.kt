package com.teamhelper.imsdk.netcore

import androidx.annotation.Keep
import com.teamhelper.imsdk.netcore.internal.NetCoreLib

@Keep
class NetCore private constructor(private var netCoreLib: NetCoreLib) {

    /**
     * 连接服务器
     */
    fun connect(wsUrl: String = "ws://192.168.3.107:8888/ws") {
        netCoreLib.connect(wsUrl)
    }

    /**
     * 发送文本消息
     * @param req
     */
    fun sendTextMessage(req: String) {
        netCoreLib.sendTextMessage(req)
    }

    /**
     * 发送二进制消息
     * @param req
     */
    fun sendBinaryMessage(req: ByteArray) {
        netCoreLib.sendBinaryMessage(req)
    }

    /**
     * 关闭连接
     */
    fun close() {
        netCoreLib.close()
    }

    /**
     * 是否处于连接状态
     */
    fun isConnected(): Boolean {
        return netCoreLib.isConnected()
    }

    /**
     * 是否处于重连状态
     */
    fun isReconnect(): Boolean {
        return netCoreLib.isReconnect()
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NetCore(NetCoreLib())
        }
    }
}
