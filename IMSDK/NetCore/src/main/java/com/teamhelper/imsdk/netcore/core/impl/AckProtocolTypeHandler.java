package com.teamhelper.imsdk.netcore.core.impl;

import com.teamhelper.imsdk.netcore.core.ProtocolTypeHandler;
import com.teamhelper.imsdk.netcore.data.AckDataContent;
import com.teamhelper.imsdk.netcore.protocol.Protocol;
import com.teamhelper.imsdk.netcore.protocol.ProtocolType;

/**
 * @Description:
 * @Author: Finger
 * @Email: Finger@spianmo.com
 * @Date: 2023/10/24
 */
public class AckProtocolTypeHandler implements ProtocolTypeHandler<AckDataContent> {

    /**
     * 消息确认处理器, 获取fp, 然后去QOS中移除, 停止轮训
     *
     * @param p           消息体
     * @param dataContent 消息正文
     */
    @Override
    public void handle(Protocol<AckDataContent> p, AckDataContent dataContent) {
        String fp = dataContent.getFp();
    }

    /**
     * 获取处理器实现类处理的消息类型
     *
     * @return
     */
    @Override
    public Integer getProtocolType() {
        return ProtocolType.C.ACK;
    }

    /**
     * 默认方法, 获取实现类的泛型对象的类对象
     *
     * @return
     */
    @Override
    public Class<AckDataContent> getGenericType() {
        return AckDataContent.class;
    }
}
