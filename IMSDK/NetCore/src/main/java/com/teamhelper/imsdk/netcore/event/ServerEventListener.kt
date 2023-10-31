package com.teamhelper.imsdk.netcore.event

import com.teamhelper.imsdk.netcore.data.AckDataContent
import com.teamhelper.imsdk.netcore.data.CommonDataContent
import com.teamhelper.imsdk.netcore.data.ErrorDataContent
import com.teamhelper.imsdk.netcore.data.KickOutDataContent
import com.teamhelper.imsdk.netcore.data.LoginResultDataContent
import com.teamhelper.imsdk.netcore.protocol.Protocol

/**
 * @Description: 服务端事件监听器
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/23
 */
interface ServerEventListener {

    fun onUserLogin(p: Protocol<LoginResultDataContent>)
    fun onUserKickOut(p: Protocol<KickOutDataContent>)
    fun onCommonDataReceived(p: ByteArray)
    fun <T> onCommonDataReceived(p: Protocol<CommonDataContent<T>>)
    fun onHeartbeat(p: Protocol<String>)
    fun onErrorReceived(p: Protocol<ErrorDataContent>)
    fun onAckReceived(p: Protocol<AckDataContent>)

}
