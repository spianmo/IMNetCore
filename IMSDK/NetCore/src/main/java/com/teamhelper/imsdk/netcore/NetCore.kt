package com.teamhelper.imsdk.netcore

import androidx.annotation.Keep

@Keep
class NetCore private constructor(private var netCoreLib: NetCoreLib) {

    fun connect() {
        netCoreLib.connect("ws://192.168.3.107:8888/ws")
    }

    fun sendTextMessage(req: String) {
        netCoreLib.sendTextMessage(req)
    }
    fun sendBinaryMessage(req: ByteArray) {
        netCoreLib.sendBinaryMessage(req)
    }

    fun close() {
        netCoreLib.close()
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NetCore(NetCoreLib())
        }
    }
}
