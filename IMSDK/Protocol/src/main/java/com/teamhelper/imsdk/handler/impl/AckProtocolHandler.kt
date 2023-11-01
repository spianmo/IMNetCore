package com.teamhelper.imsdk.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.BusinessEventRegistry
import com.teamhelper.imsdk.base.BusinessEventType
import com.teamhelper.imsdk.base.EventRegistry
import com.teamhelper.imsdk.data.AckDataContent
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
class AckProtocolHandler : ProtocolHandler<AckDataContent> {
    /**
     * 消息确认处理器, 获取fp, 然后去QOS中移除, 停止轮训
     *
     * @param p 消息体
     */
    override fun handle(p: Protocol<AckDataContent>) {
        EventRegistry.post(BusinessEventType.onAckReceived, p)
        BusinessEventRegistry.executeEventHandler { businessEventListener: BusinessEventListener ->
            businessEventListener.onAckReceived(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<Protocol<AckDataContent>>() {}.type
    }
}
