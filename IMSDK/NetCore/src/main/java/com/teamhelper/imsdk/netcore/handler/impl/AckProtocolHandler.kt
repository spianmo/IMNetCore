package com.teamhelper.imsdk.netcore.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.netcore.NetEventRegistry.executeEventHandler
import com.teamhelper.imsdk.netcore.data.AckDataContent
import com.teamhelper.imsdk.netcore.event.ServerEventListener
import com.teamhelper.imsdk.netcore.handler.ProtocolHandler
import com.teamhelper.imsdk.netcore.protocol.Protocol
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
        executeEventHandler { serverEventListener: ServerEventListener ->
            serverEventListener.onAckReceived(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<Protocol<AckDataContent>>() {}.type
    }
}
