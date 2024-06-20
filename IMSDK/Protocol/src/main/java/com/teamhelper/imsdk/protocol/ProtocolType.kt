package com.teamhelper.imsdk.protocol

/**
 * @Description: 协议类型
 * @Author: ZhangLuo (Moss)
 * @Email: zhangluo.chn@gmail.com
 * @Date: 2023/10/23
 */
interface ProtocolType {
    //------------------------------------------------------- from client
    interface C {
        companion object {
            /**
             * 由客户端发出 - 协议类型：客户端登陆
             */
            const val FROM_CLIENT_TYPE_OF_LOGIN: Int = 0

            /**
             * 由客户端发出 - 协议类型：心跳包
             */
            const val `FROM_CLIENT_TYPE_OF_KEEP$ALIVE`: Int = 1

            /**
             * 由客户端发出 - 协议类型：发送通用数据
             */
            const val `FROM_CLIENT_TYPE_OF_COMMON$DATA`: Int = 2

            /**
             * 由客户端发出 - 协议类型：客户端退出登陆
             */
            const val FROM_CLIENT_TYPE_OF_LOGOUT: Int = 3

            /**
             * 由客户端发出 - 协议类型：QoS保证机制中的消息应答包（目前只支持客户端间的QoS机制哦）
             */
            const val FROM_CLIENT_TYPE_OF_RECIVED: Int = 4

            /**
             * 由客户端发出 - 协议类型：C2S时的回显指令（此指令目前仅用于测试时）
             */
            const val FROM_CLIENT_TYPE_OF_ECHO: Int = 5

            /**
             * 由客户端发出 - 协议类型: 刷新token
             */
            const val FROM_CLIENT_TYPE_OF_REFRESH_TOKEN: Int = 6
        }
    }

    //------------------------------------------------------- from server
    interface S {
        companion object {
            /**
             * 由服务端发出 - 协议类型：响应客户端的登陆
             */
            const val `FROM_SERVER_TYPE_OF_RESPONSE$LOGIN`: Int = 50

            /**
             * 由服务端发出 - 协议类型：响应客户端的心跳包
             */
            const val `FROM_SERVER_TYPE_OF_RESPONSE$KEEP$ALIVE`: Int = 51

            /**
             * 由服务端发出 - 协议类型：反馈给客户端的错误信息
             */
            const val `FROM_SERVER_TYPE_OF_RESPONSE$FOR$ERROR`: Int = 52

            /**
             * 由服务端发出 - 协议类型：反馈回显指令给客户端
             */
            const val `FROM_SERVER_TYPE_OF_RESPONSE$ECHO`: Int = 53

            /**
             * 由服务端发出 - 协议类型：向客户端发出“被踢”指令
             */
            const val FROM_SERVER_TYPE_OF_KICKOUT: Int = 54

            /**
             * 由服务端发出- 协议类型: 向客户端发出TOKEN还有10分钟过期的指令
             */
            const val TOKEN_IS_ABOUT_TO_EXPIRE: Int = 55

            /**
             * 由服务端发出 - 协议类型：响应客户端的刷新token
             */
            const val `FROM_SERVER_TYPE_OF_RESPONSE$REFRESH_TOKEN`: Int = 56
        }
    }
}