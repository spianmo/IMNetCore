package com.teamhelper.imsdk.netcore.core.impl;


import com.teamhelper.imsdk.netcore.core.ProtocolTypeHandler;
import com.teamhelper.imsdk.netcore.data.LoginDataContent;
import com.teamhelper.imsdk.netcore.protocol.Protocol;
import com.teamhelper.imsdk.netcore.protocol.ProtocolType;

/**
 * @Description:
 * @Author: ZhangLuo (Moss)
 * @Email: zhangluo.chn@gmail.com
 * @Date: 2023/10/24
 */
public class LoginProtocolTypeHandler implements ProtocolTypeHandler<LoginDataContent> {
    /**
     * 消息处理器
     *
     * @param data    消息体
     * @param content 客户端消息体
     */
    @Override
    public void handle(Protocol<LoginDataContent> data, LoginDataContent content) {
        System.out.println();
    }

    /**
     * 获取处理器实现类处理的消息类型
     *
     * @return
     */
    @Override
    public Integer getProtocolType() {
        return ProtocolType.C.LOGIN;
    }

    /**
     * 默认方法, 获取实现类的泛型对象的类对象
     *
     * @return
     */
    @Override
    public Class<LoginDataContent> getGenericType() {
        return LoginDataContent.class;
    }
}
