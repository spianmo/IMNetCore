package com.teamhelper.imsdk.event

import cn.teamhelper.signal.data.KickOutDataContent
import com.teamhelper.imsdk.data.AckDataContent
import com.teamhelper.imsdk.data.CommonDataContent
import com.teamhelper.imsdk.data.ErrorDataContent
import com.teamhelper.imsdk.data.LoginResultDataContent
import com.teamhelper.imsdk.protocol.ProtocolWrapper

/**
 * @Description: Protocol事件监听器
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/23
 */
interface BusinessEventListener {
    /**
     * 用户登录事件
     * @param p: Protocol<LoginResultDataContent>
     */
    fun onUserLogin(p: ProtocolWrapper<LoginResultDataContent>)

    /**
     * 用户被踢出事件
     * @param p: Protocol<KickOutDataContent>
     */
    fun onUserKickOut(p: ProtocolWrapper<KickOutDataContent>)

    /**
     * 通用数据接收事件
     * @param p: ByteArray
     */
    fun onCommonDataReceived(p: ByteArray)

    /**
     * 通用数据接收事件
     * @param p: Protocol<CommonDataContent<T>>
     */
    fun <T> onCommonDataReceived(p: ProtocolWrapper<CommonDataContent<T>>)

    /**
     * 心跳事件
     * @param p: Protocol<String>
     */
    fun onHeartbeat(p: ProtocolWrapper<String>)

    /**
     * 错误事件
     * @param p: Protocol<ErrorDataContent>
     */
    fun onErrorReceived(p: ProtocolWrapper<ErrorDataContent>)

    /**
     * ACK事件
     * @param p: Protocol<AckDataContent>
     */
    fun onAckReceived(p: ProtocolWrapper<AckDataContent>)

}
