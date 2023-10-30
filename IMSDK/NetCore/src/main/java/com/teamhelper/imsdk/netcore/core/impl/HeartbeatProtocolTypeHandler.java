package com.teamhelper.imsdk.netcore.core.impl;


import com.teamhelper.imsdk.netcore.core.ProtocolTypeHandler;
import com.teamhelper.imsdk.netcore.core.ServerGlobalContext;
import com.teamhelper.imsdk.netcore.protocol.Protocol;

/**
 * @Description:
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
public class HeartbeatProtocolTypeHandler implements ProtocolTypeHandler<String> {

    /**
     * 消息处理器
     *
     * @param p           消息体
     * @param dataContent
     */
    @Override
    public void handle(Protocol<String> p, String dataContent) {
        // 触发监听
        ServerGlobalContext.getServerEventListener().onHeartbeatEvent(p);
    }

    /**
     * 获取处理器实现类处理的消息类型
     *
     * @return
     */
    @Override
    public Integer getProtocolType() {
        return null;
    }

    /**
     * 默认方法, 获取实现类的泛型对象的类对象
     *
     * @return
     */
    @Override
    public Class<String> getGenericType() {
        return null;
    }
}
