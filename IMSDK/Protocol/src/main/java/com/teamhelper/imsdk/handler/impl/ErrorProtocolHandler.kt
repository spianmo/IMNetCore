package com.teamhelper.imsdk.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.BusinessEventRegistry
import com.teamhelper.imsdk.data.ErrorDataContent
import com.teamhelper.imsdk.event.BusinessEventListener
import com.teamhelper.imsdk.handler.ProtocolHandler
import com.teamhelper.imsdk.protocol.Protocol
import java.lang.reflect.Type

class ErrorProtocolHandler : ProtocolHandler<ErrorDataContent> {
    override fun handle(p: Protocol<ErrorDataContent>) {
        BusinessEventRegistry.executeEventHandler { businessEventListener: BusinessEventListener ->
            businessEventListener.onErrorReceived(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<Protocol<ErrorDataContent>>() {}.type
    }
}
