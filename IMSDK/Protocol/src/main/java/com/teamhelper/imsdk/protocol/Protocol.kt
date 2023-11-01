package com.teamhelper.imsdk.protocol

import com.teamhelper.imsdk.constant.Platform

/**
 * @Description: 协议
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/23
 */
class Protocol<T>(
    type: Int,
    from: String,
    platform: Platform,
    to: String,
    fp: String,
    qos: Boolean,
    retryCount: Int,
    dataContent: T
) {
    /**
     * 消息类型
     *
     * @see ProtocolType
     */
    val type: Int? = null

    /**
     * 消息来源
     */
    val from: String? = null

    /**
     * 消息发送的客户端
     */
    val platform: Platform? = null

    /**
     * 发送给谁
     */
    val to: String? = null

    /**
     * 消息指纹, UUID
     */
    val fp: String? = null

    /**
     * 是否开启QOS消息保证
     */
    val qos: Boolean? = null

    /**
     * 消息重试次数
     */
    val retryCount: Int? = null

    /**
     * 携带的消息正文
     */
    val dataContent: T? = null
}
