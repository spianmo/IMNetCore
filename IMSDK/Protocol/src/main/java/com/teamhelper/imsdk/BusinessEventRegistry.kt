package com.teamhelper.imsdk

import com.teamhelper.imsdk.event.BusinessEventListener
import com.teamhelper.imsdk.handler.ProtocolHandler
import com.teamhelper.imsdk.handler.impl.AckProtocolHandler
import com.teamhelper.imsdk.handler.impl.CommonProtocolHandler
import com.teamhelper.imsdk.handler.impl.ErrorProtocolHandler
import com.teamhelper.imsdk.handler.impl.HeartbeatProtocolHandler
import com.teamhelper.imsdk.handler.impl.KickOutProtocolHandler
import com.teamhelper.imsdk.handler.impl.LoginResponseProtocolHandler
import com.teamhelper.imsdk.protocol.ProtocolType
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

object BusinessEventRegistry {
    private val businessEventListeners: MutableList<BusinessEventListener> = ArrayList()
    private val handlers: MutableMap<Int, ProtocolHandler<*>> = ConcurrentHashMap()

    init {
        handlers[ProtocolType.S.LOGIN_RESPONSE] = LoginResponseProtocolHandler()
        handlers[ProtocolType.S.HEARTBEAT] = HeartbeatProtocolHandler()
        handlers[ProtocolType.S.COMMON_DATA] = CommonProtocolHandler()
        handlers[ProtocolType.S.ACK] = AckProtocolHandler()
        handlers[ProtocolType.S.KICK_OUT] = KickOutProtocolHandler()
        handlers[ProtocolType.S.ERROR] = ErrorProtocolHandler()
    }

    /**
     * 获取服务端事件监听器
     *
     * @return
     */
    fun executeEventHandler(consumer: Consumer<BusinessEventListener>) {
        businessEventListeners.forEach(consumer)
    }

    /**
     * 设置事件监听器
     *
     * @param eventListener
     */
    fun addBusinessEventListener(eventListener: BusinessEventListener) {
        businessEventListeners.add(eventListener)
    }

    /**
     * 移除事件监听器
     *
     * @param eventListener
     */
    fun removeBusinessEventListener(eventListener: BusinessEventListener) {
        businessEventListeners.remove(eventListener)
    }

    /**
     * 获取消息类型处理器
     *
     * @param protocolType
     * @return
     */
    fun getProtocolHandler(protocolType: Int): ProtocolHandler<*>? {
        return handlers[protocolType]
    }
}