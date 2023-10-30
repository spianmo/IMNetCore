package com.teamhelper.imsdk.netcore

import androidx.annotation.Keep

@Keep
class NetCore private constructor(private var netCoreLib: NetCoreLib) {

    fun connect() {
        netCoreLib.connect("ws://192.168.3.107:8888/ws")
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NetCore(NetCoreLib())
        }
    }
}
