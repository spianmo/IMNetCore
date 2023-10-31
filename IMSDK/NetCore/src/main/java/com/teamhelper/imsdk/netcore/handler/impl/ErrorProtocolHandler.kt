package com.teamhelper.imsdk.netcore.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.netcore.NetEventRegistry.executeEventHandler
import com.teamhelper.imsdk.netcore.data.ErrorDataContent
import com.teamhelper.imsdk.netcore.event.ServerEventListener
import com.teamhelper.imsdk.netcore.handler.ProtocolHandler
import com.teamhelper.imsdk.netcore.protocol.Protocol
import java.lang.reflect.Type

class ErrorProtocolHandler : ProtocolHandler<ErrorDataContent> {
    override fun handle(p: Protocol<ErrorDataContent>) {
        executeEventHandler { serverEventListener: ServerEventListener ->
            serverEventListener.onErrorReceived(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<Protocol<ErrorDataContent>>() {}.type
    }
}
