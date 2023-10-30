package com.teamhelper.imsdk.netcore.event

import com.teamhelper.imsdk.netcore.constant.BreakReason
import com.teamhelper.imsdk.netcore.constant.Platform
import com.teamhelper.imsdk.netcore.protocol.Protocol

/**
 * @Description: 服务端事件监听器
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/23
 */
interface ServerEventListener {
    /**
     * 用户登录事件
     *
     * @param uid
     * @param platform
     * @param token
     * @param extendData
     * @return
     */
    fun onUserLoginEvent(
        uid: String?,
        platform: Platform?,
        token: String?,
        extendData: String?
    )

    /**
     * 用户上线事件
     *
     * @param uid
     * @param platform
     * @param extendData
     */
    fun onUserOnlineEvent(uid: String?, platform: Platform?, extendData: String?)

    /**
     * 用户离线事件
     *
     * @param uid
     * @param platform
     * @param reason
     */
    fun onUserOfflineEvent(uid: String?, platform: Platform?, reason: BreakReason?)

    /**
     * 消息被收到事件
     *
     * @param p
     * @param <T>
     * @return
    </T> */
    fun <T> onMessageReceivedEvent(p: Protocol<T>?)

    /**
     * 消息处理成功事件
     *
     * @param p
     */
    fun onMessageHandleSuccessEvent(p: Protocol<*>?)

    /**
     * 消息处理失败事件
     * 1. 可能对方不在线
     * 2. 可能消息处理
     *
     * @param p
     */
    fun onMessageHandleFailedEvent(p: Protocol<*>?)

    /**
     * 心跳事件
     * 如果心跳中携带了其他的状态内容, 可以监听该消息处理
     *
     * @param p
     */
    fun onHeartbeatEvent(p: Protocol<*>?)

    /**
     * 会话读超时, 超时时间配置
     *
     * @see WebsocketConfig
     */
    fun onSessionReadTimeOut()

    /**
     * 会话写超时, 超时时间配置
     *
     * @see WebsocketConfig
     */
    fun onSessionWriterTimeOut()

    /**
     * 会话心跳超时, 超时时间配置 ALL_IDLE_TIME_SECONDS
     *
     * @see WebsocketConfig
     */
    fun onHeartbeatTimeOut()
}
