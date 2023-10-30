package com.teamhelper.imsdk.netcore.config

import androidx.annotation.Keep

/**
 * @Description: Websocket全局配置
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
@Keep
object WebsocketConfig {
    /**
     * 默认8kb
     */
    var MAX_CONTENT_LENGTH = 1 shl 13

    /**
     * 心跳间隔时间(ms)
     */
    var PING_INTERVAL = 10 * 1000

    /**
     * 读超时时间(ms)
     */
    var READ_TIMEOUT = 5 * 1000

    /**
     * 写超时时间(ms)
     */
    var WRITE_TIMEOUT = 5 * 1000

    /**
     * 闲置超时时间(ms)
     */
    var KEEPALIVE_TIMEOUT = 75 * 1000

    /**
     * 连接超时时间(ms)
     */
    var CONNECT_TIMEOUT = 10 * 1000
}
