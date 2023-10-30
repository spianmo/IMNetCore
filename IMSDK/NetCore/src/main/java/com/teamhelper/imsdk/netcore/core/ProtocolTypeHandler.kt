package com.teamhelper.imsdk.netcore.core

import com.teamhelper.imsdk.netcore.protocol.Protocol

/**
 * @Description: 消息处理器接口
 * @Author: ZhangLuo
 * @Email: 1946430@qq.com
 */
interface ProtocolTypeHandler<T> {
    /**
     * 消息处理器
     *
     * @param p 消息体
     */
    fun handle(p: Protocol<T>?, dataContent: T)

    /**
     * 获取处理器实现类处理的消息类型
     *
     * @return
     */
    val protocolType: Int?

    /**
     * 默认方法, 获取实现类的泛型对象的类对象
     *
     * @return
     */
    val genericType: Class<T>?
}
