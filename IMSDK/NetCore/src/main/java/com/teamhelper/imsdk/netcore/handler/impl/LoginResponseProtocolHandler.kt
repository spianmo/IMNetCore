package com.teamhelper.imsdk.netcore.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.netcore.NetEventRegistry.executeEventHandler
import com.teamhelper.imsdk.netcore.data.LoginResultDataContent
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
class LoginResponseProtocolHandler : ProtocolHandler<LoginResultDataContent> {
    /**
     * 消息处理器
     *
     * @param p 消息体
     */
    override fun handle(p: Protocol<LoginResultDataContent>) {
        executeEventHandler { serverEventListener: ServerEventListener ->
            serverEventListener.onUserLogin(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<Protocol<LoginResultDataContent>>() {}.type
    }
}
