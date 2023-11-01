package com.teamhelper.imsdk.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.BusinessEventRegistry
import com.teamhelper.imsdk.base.BusinessEventType
import com.teamhelper.imsdk.base.EventRegistry
import com.teamhelper.imsdk.data.KickOutDataContent
import com.teamhelper.imsdk.event.BusinessEventListener
import com.teamhelper.imsdk.handler.ProtocolHandler
import com.teamhelper.imsdk.protocol.Protocol
import java.lang.reflect.Type

/**
 * @Description: 登录消息处理器
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
class KickOutProtocolHandler : ProtocolHandler<KickOutDataContent> {
    /**
     * 消息处理器
     *
     * @param p 消息体
     */
    override fun handle(p: Protocol<KickOutDataContent>) {
        EventRegistry.post(BusinessEventType.onUserKickOut, p)
        BusinessEventRegistry.executeEventHandler { businessEventListener: BusinessEventListener ->
            businessEventListener.onUserKickOut(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<Protocol<KickOutDataContent>>() {}.type
    }
}
