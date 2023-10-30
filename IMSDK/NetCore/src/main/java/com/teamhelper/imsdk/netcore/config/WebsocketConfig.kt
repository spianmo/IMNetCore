package com.teamhelper.imsdk.netcore.config

import androidx.annotation.Keep

/**
 * @Description: Websocket全局配置
 * @Author: ZhangLuo (Moss)
 * @Email: zhangluo.chn@gmail.com
 * @Date: 2023/10/24
 */
@Keep
object WebsocketConfig {
    /**
     * 默认8kb
     */
    var MAX_CONTENT_LENGTH = 8 * 1024

    /**
     * 读写超时时间(ms)
     */
    var IDLE_PING_INTERVAL = 10 * 1000

    /**
     * 连接超时时间(ms)
     */
    var CONNECT_TIMEOUT = 10 * 1000
}
