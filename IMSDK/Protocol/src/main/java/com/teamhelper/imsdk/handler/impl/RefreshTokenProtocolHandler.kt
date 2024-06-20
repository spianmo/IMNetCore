package com.teamhelper.imsdk.handler.impl

import com.google.gson.reflect.TypeToken
import com.teamhelper.imsdk.BusinessEventRegistry
import com.teamhelper.imsdk.base.BusinessEventType
import com.teamhelper.imsdk.base.EventRegistry
import com.teamhelper.imsdk.data.CommonDataContent
import com.teamhelper.imsdk.event.BusinessEventListener
import com.teamhelper.imsdk.handler.ProtocolHandler
import com.teamhelper.imsdk.protocol.ProtocolWrapper
import java.lang.reflect.Type

/**
 * @Description: 通用消息体的处理
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
class RefreshTokenProtocolHandler : ProtocolHandler<CommonDataContent<Any>> {
    /**
     * 消息处理器
     *
     * @param p 消息体
     */
    override fun handle(p: ProtocolWrapper<CommonDataContent<Any>>) {
        EventRegistry.post(BusinessEventType.onCommonDataReceived, p)
        BusinessEventRegistry.executeEventHandler { businessEventListener: BusinessEventListener ->
            businessEventListener.onCommonDataReceived(
                p
            )
        }
    }

    override fun getGenericType(): Type {
        return object : TypeToken<ProtocolWrapper<CommonDataContent<Any>>>() {}.type
    }
}
