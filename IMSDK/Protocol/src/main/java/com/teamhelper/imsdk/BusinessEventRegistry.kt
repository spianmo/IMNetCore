package com.teamhelper.imsdk

import com.teamhelper.imsdk.event.BusinessEventListener
import com.teamhelper.imsdk.handler.ProtocolHandler
import com.teamhelper.imsdk.handler.impl.EchoProtocolHandler
import com.teamhelper.imsdk.handler.impl.ErrorProtocolHandler
import com.teamhelper.imsdk.handler.impl.HeartbeatProtocolHandler
import com.teamhelper.imsdk.handler.impl.KickOutProtocolHandler
import com.teamhelper.imsdk.handler.impl.LoginResponseProtocolHandler
import com.teamhelper.imsdk.handler.impl.RefreshTokenProtocolHandler
import com.teamhelper.imsdk.handler.impl.TokenExpiredProtocolHandler
import com.teamhelper.imsdk.protocol.ProtocolType
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

object BusinessEventRegistry {
    private val businessEventListeners: MutableList<BusinessEventListener> = ArrayList()
    private val handlers: MutableMap<Int, ProtocolHandler<*>> = ConcurrentHashMap()

    init {
        handlers[ProtocolType.S.`FROM_SERVER_TYPE_OF_RESPONSE$LOGIN`] = LoginResponseProtocolHandler()
        handlers[ProtocolType.S.`FROM_SERVER_TYPE_OF_RESPONSE$KEEP$ALIVE`] = HeartbeatProtocolHandler()
        handlers[ProtocolType.S.TOKEN_IS_ABOUT_TO_EXPIRE] = TokenExpiredProtocolHandler()
        handlers[ProtocolType.S.`FROM_SERVER_TYPE_OF_RESPONSE$REFRESH_TOKEN`] = RefreshTokenProtocolHandler()
        handlers[ProtocolType.S.`FROM_SERVER_TYPE_OF_RESPONSE$ECHO`] = EchoProtocolHandler()
        handlers[ProtocolType.S.FROM_SERVER_TYPE_OF_KICKOUT] = KickOutProtocolHandler()
        handlers[ProtocolType.S.`FROM_SERVER_TYPE_OF_RESPONSE$FOR$ERROR`] = ErrorProtocolHandler()
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