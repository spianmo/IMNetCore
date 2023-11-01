package com.teamhelper.imsdk.handler

import com.teamhelper.imsdk.protocol.Protocol
import java.lang.reflect.Type


/**
 * @Description: 消息处理器接口
 * @Author: Finger
 * @Email: finger@spianmo.com
 */
interface ProtocolHandler<T> {
    /**
     * 消息处理器
     *
     * @param p 消息体
     */
    fun handle(p: Protocol<T>)

    /**
     * 默认方法, 获取实现类的泛型对象的类对象
     * @return
     */
    fun getGenericType(): Type
}
