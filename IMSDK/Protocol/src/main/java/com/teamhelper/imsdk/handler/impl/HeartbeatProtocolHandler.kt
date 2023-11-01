package com.teamhelper.imsdk.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.BusinessEventRegistry
import com.teamhelper.imsdk.event.BusinessEventListener
import com.teamhelper.imsdk.handler.ProtocolHandler
import com.teamhelper.imsdk.protocol.Protocol
import java.lang.reflect.Type

/**
 * @Description:
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
class HeartbeatProtocolHandler : ProtocolHandler<String> {
    /**
     * 消息处理器
     *
     * @param p 消息体
     */
    override fun handle(p: Protocol<String>) {
        BusinessEventRegistry.executeEventHandler { businessEventListener: BusinessEventListener ->
            businessEventListener.onHeartbeat(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<Protocol<String>>() {}.type
    }
}
