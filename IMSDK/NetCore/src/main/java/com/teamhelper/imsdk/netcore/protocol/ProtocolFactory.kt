package com.teamhelper.imsdk.netcore.protocol

import com.teamhelper.imsdk.netcore.constant.DefaultFrom
import com.teamhelper.imsdk.netcore.constant.Platform
import java.util.UUID

/**
 * @Description:
 * @Author: ZhangLuo (Moss)
 * @Email: zhangluo.chn@gmail.com
 * @Date: 2023/10/24
 */
object ProtocolFactory {
    /**
     * 全属性的构造
     *
     * @param type
     * @param from
     * @param platform
     * @param to
     * @param qos
     * @return
     */
    fun create(
        type: Int, from: String?, platform: Platform?,
        to: String?, qos: Boolean
    ): Protocol<String> {
        return create(type, from, platform, to, qos, 3, "")
    }

    /**
     * 全属性的构造
     *
     * @param type
     * @param from
     * @param platform
     * @param to
     * @param qos
     * @param retryCount
     * @param dataContent
     * @param <T>
     * @return
    </T> */
    fun <T> create(
        type: Int, from: String?, platform: Platform?,
        to: String?, qos: Boolean, retryCount: Int,
        dataContent: T
    ): Protocol<T> {
        val fp = UUID.randomUUID().toString()
        return Protocol(type, from!!, platform!!, to!!, fp, qos, retryCount, dataContent)
    }

    /**
     * 默认QOS和重试次数的消息体
     *
     * @param type
     * @param from
     * @param platform
     * @param to
     * @param dataContent
     * @param <T>
     * @return
    </T> */
    fun <T> create(
        type: Int, from: String?, platform: Platform?,
        to: String?, dataContent: T
    ): Protocol<T> {
        return create(type, from, platform, to, true, 3, dataContent)
    }

    /**
     * 服务端发出
     * 默认QOS和重试次数的消息体
     *
     * @param type
     * @param to
     * @param dataContent
     * @param <T>
     * @return
    </T> */
    @JvmStatic
    fun <T> create(type: Int, to: String?, dataContent: T): Protocol<T> {
        return create(type, DefaultFrom.SERVER, Platform.SERVER, to, true, 3, dataContent)
    }
}
