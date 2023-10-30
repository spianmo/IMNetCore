package com.teamhelper.imsdk.netcore.constant

/**
 * @Description: 断线原因
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/23
 */
enum class BreakReason {
    /**
     * 授权失败, 被断开连接
     */
    AUTHENTICATION_FAILED,

    /**
     * 直接断开连接的下线
     */
    OFFLINE,

    /**
     * 调用退出的下线
     */
    LOGOUT,

    /**
     * 心跳超时, 被断开连接
     */
    HEARTBEAT_TIMEOUT,

    /**
     * 消息类型未定义
     */
    PROTOCOL_TYPE_HANDLER_CLASS_NOT_FOUND,

    /**
     * 发生异常, 被断开连接
     */
    EXCEPTION,

    /**
     * 相同的客户端上线
     */
    SAME_PLATFORM_ONLINE
}
