package com.teamhelper.imsdk.netcore.protocol

/**
 * @Description: 协议类型
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/23
 */
interface ProtocolType {
    interface C {
        companion object {
            /**
             * 登录协议类型
             */
            const val LOGIN = 0

            /**
             * 心跳协议类型
             */
            const val HEARTBEAT = 1

            /**
             * 通用数据协议类型
             */
            const val COMMON_DATA = 2

            /**
             * ACK消息协议类型
             */
            const val ACK = 3

            /**
             * 登出协议类型
             */
            const val LOGOUT = 4
        }
    }

    interface S {
        companion object {
            /**
             * 登录响应协议类型
             */
            const val LOGIN_RESPONSE = 50

            /**
             * 服务端的心跳协议类型
             */
            const val HEARTBEAT = 51

            /**
             * 消息处理异常, 通知客户端
             */
            const val ERROR = 52

            /**
             * 服务端的ACK消息协议类型
             */
            const val ACK = 53

            /**
             * 服务端发出的踢客户端下线协议类型
             */
            const val KICK_OUT = 54
        }
    }
}
