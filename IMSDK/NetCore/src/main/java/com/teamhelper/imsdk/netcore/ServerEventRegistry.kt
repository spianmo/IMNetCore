package com.teamhelper.imsdk.netcore

import com.teamhelper.imsdk.netcore.event.ServerEventListener
import java.util.function.Consumer

/**
 * @Description:
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
object ServerEventRegistry {
    private val serverEventListeners: MutableList<ServerEventListener> = ArrayList()

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
}
