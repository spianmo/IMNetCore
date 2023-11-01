package com.teamhelper.imsdk.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.BusinessEventRegistry
import com.teamhelper.imsdk.data.LoginResultDataContent
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
class LoginResponseProtocolHandler : ProtocolHandler<LoginResultDataContent> {
    /**
     * 消息处理器
     *
     * @param p 消息体
     */
    override fun handle(p: Protocol<LoginResultDataContent>) {
        BusinessEventRegistry.executeEventHandler { businessEventListener: BusinessEventListener ->
            businessEventListener.onUserLogin(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<Protocol<LoginResultDataContent>>() {}.type
    }
}
