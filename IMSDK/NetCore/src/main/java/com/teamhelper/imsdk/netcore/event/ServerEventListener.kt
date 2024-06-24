package com.teamhelper.imsdk.netcore.event

import com.teamhelper.imsdk.netcore.NetCore

/**
 * @Description: 信道监听器
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/23
 */
interface ServerEventListener {
    /**
     * 连接打开回调
     * @param response: String
     */
    fun onConnectOpen(client: NetCore, response: String)

    /**
     * opCode Text消息接收回调
     * @param message: String
     */

    fun onTextMessageRecv(client: NetCore, message: String)

    /**
     * opCode Binary消息接收回调
     * @param binary: ByteArray
     */

    fun onBinaryMessageRecv(client: NetCore, binary: ByteArray)

    /**
     * 连接关闭回调
     */

    fun onConnectClosed(client: NetCore, code: Int, reason: String)

    /**
     * 重连回调
     * @calledByC++
     * @param retryCnt: Int 重连次数 从1开始
     * @param delay: Int 重连延迟时间 单位ms
     */

    fun onReconnect(client: NetCore, retryCnt: Int, delay: Int)

}
