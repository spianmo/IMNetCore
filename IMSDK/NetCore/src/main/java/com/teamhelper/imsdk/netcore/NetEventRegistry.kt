package com.teamhelper.imsdk.netcore

import com.teamhelper.imsdk.netcore.event.ServerEventListener
import com.teamhelper.imsdk.netcore.handler.ProtocolHandler
import com.teamhelper.imsdk.netcore.handler.impl.AckProtocolHandler
import com.teamhelper.imsdk.netcore.handler.impl.CommonProtocolHandler
import com.teamhelper.imsdk.netcore.handler.impl.ErrorProtocolHandler
import com.teamhelper.imsdk.netcore.handler.impl.HeartbeatProtocolHandler
import com.teamhelper.imsdk.netcore.handler.impl.KickOutProtocolHandler
import com.teamhelper.imsdk.netcore.handler.impl.LoginResponseProtocolHandler
import com.teamhelper.imsdk.netcore.protocol.ProtocolType
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

/**
 * @Description:
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
object NetEventRegistry {
    private val serverEventListeners: MutableList<ServerEventListener> = ArrayList()
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
    fun executeEventHandler(consumer: Consumer<ServerEventListener>) {
        serverEventListeners.forEach(consumer)
    }

    /**
     * 设置事件监听器
     *
     * @param eventListener
     */
    fun addServerEventListener(eventListener: ServerEventListener) {
        serverEventListeners.add(eventListener)
    }

    /**
     * 移除事件监听器
     *
     * @param eventListener
     */
    fun removeServerEventListener(eventListener: ServerEventListener) {
        serverEventListeners.remove(eventListener)
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
