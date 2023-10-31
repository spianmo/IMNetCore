package com.teamhelper.imsdk.netcore.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.netcore.NetEventRegistry.executeEventHandler
import com.teamhelper.imsdk.netcore.data.CommonDataContent
import com.teamhelper.imsdk.netcore.event.ServerEventListener
import com.teamhelper.imsdk.netcore.handler.ProtocolHandler
import com.teamhelper.imsdk.netcore.protocol.Protocol
import java.lang.reflect.Type

/**
 * @Description: 通用消息体的处理
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
class CommonProtocolHandler : ProtocolHandler<CommonDataContent<Any>> {
    /**
     * 消息处理器
     *
     * @param p 消息体
     */
    override fun handle(p: Protocol<CommonDataContent<Any>>) {
        executeEventHandler { serverEventListener: ServerEventListener ->
            serverEventListener.onCommonDataReceived(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<Protocol<CommonDataContent<Any>>>() {}.type
    }
}
