package com.teamhelper.imsdk.netcore.config

import androidx.annotation.Keep

/**
 * @Description: Websocket全局配置
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
@Keep
object WebSocketConfig {
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
    var KEEPALIVE_TIMEOUT = -1

    /**
     * 连接超时时间(ms)
     */
    var CONNECT_TIMEOUT = 10 * 1000

    /**
     * 是否开启重连
     */
    var ENABLE_RECONNECT = false

    /**
     * 重连最小间隔时间(ms)
     */
    var RECONNECT_MIN_DELAY = 1000

    /**
     * 重连最大间隔时间(ms)
     */
    var RECONNECT_MAX_DELAY = 10000

    /**
     * @delay_policy
     * 0: fixed
     * min_delay=3s => 3,3,3...
     * 1: linear
     * min_delay=3s max_delay=10s => 3,6,9,10,10...
     * other: exponential
     * min_delay=3s max_delay=60s delay_policy=2 => 3,6,12,24,48,60,60...
     */
    var RECONNECT_DELAY_POLICY = 0
}
