package com.teamhelper.imsdk.netcore.core.impl;


import com.teamhelper.imsdk.netcore.core.ProtocolTypeHandler;
import com.teamhelper.imsdk.netcore.protocol.Protocol;
import com.teamhelper.imsdk.netcore.protocol.ProtocolType;

/**
 * @Description: 登录消息处理器
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
public class LogoutProtocolTypeHandler implements ProtocolTypeHandler<Void> {

    /**
     * 消息处理器
     *
     * @param p           消息体
     * @param dataContent
     */
    @Override
    public void handle(Protocol<Void> p, Void dataContent) {

    }

    /**
     * 获取处理器实现类处理的消息类型
     *
     * @return
     */
    @Override
    public Integer getProtocolType() {
        return ProtocolType.C.LOGOUT;
    }

    /**
     * 默认方法, 获取实现类的泛型对象的类对象
     *
     * @return
     */
    @Override
    public Class<Void> getGenericType() {
        return Void.class;
    }
}
