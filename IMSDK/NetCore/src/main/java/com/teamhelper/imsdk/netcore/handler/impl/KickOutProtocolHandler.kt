package com.teamhelper.imsdk.netcore.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.netcore.NetEventRegistry.executeEventHandler
import com.teamhelper.imsdk.netcore.data.KickOutDataContent
import com.teamhelper.imsdk.netcore.event.ServerEventListener
import com.teamhelper.imsdk.netcore.handler.ProtocolHandler
import com.teamhelper.imsdk.netcore.protocol.Protocol
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
        executeEventHandler { serverEventListener: ServerEventListener ->
            serverEventListener.onUserKickOut(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<Protocol<KickOutDataContent>>() {}.type
    }
}
