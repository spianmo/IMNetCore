package com.teamhelper.imsdk.netcore.core;


import com.teamhelper.imsdk.netcore.core.impl.AckProtocolTypeHandler;
import com.teamhelper.imsdk.netcore.core.impl.CommonDataProtocolTypeHandler;
import com.teamhelper.imsdk.netcore.core.impl.HeartbeatProtocolTypeHandler;
import com.teamhelper.imsdk.netcore.core.impl.LoginProtocolTypeHandler;
import com.teamhelper.imsdk.netcore.core.impl.LogoutProtocolTypeHandler;
import com.teamhelper.imsdk.netcore.event.ServerEventListener;
import com.teamhelper.imsdk.netcore.exception.HandlerExistsException;
import com.teamhelper.imsdk.netcore.protocol.ProtocolType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: ZhangLuo (Moss)
 * @Email: zhangluo.chn@gmail.com
 * @Date: 2023/10/24
 */
public class ServerGlobalContext {

    private static ServerEventListener serverEventListener;

    private static Map<Integer, ProtocolTypeHandler> handlers = new ConcurrentHashMap<>();

    static {
        handlers.put(ProtocolType.C.LOGIN, new LoginProtocolTypeHandler());
        handlers.put(ProtocolType.C.HEARTBEAT, new HeartbeatProtocolTypeHandler());
        handlers.put(ProtocolType.C.COMMON_DATA, new CommonDataProtocolTypeHandler());
        handlers.put(ProtocolType.C.ACK, new AckProtocolTypeHandler());
        handlers.put(ProtocolType.C.LOGOUT, new LogoutProtocolTypeHandler());
    }

    /**
     * 获取服务端事件监听器
     *
     * @return
     */
    public static ServerEventListener getServerEventListener() {
        return serverEventListener;
    }

    /**
     * 设置服务端事件监听器
     *
     * @param eventListener
     */
    public static void setServerEventListener(ServerEventListener eventListener) {
        if (eventListener == null) {
            throw new NullPointerException("ServerEventListener can not be null");
        }
        serverEventListener = eventListener;
    }

    /**
     * 注册消息处理器
     *
     * @param handlerArr
     */
    public static void registerProtocolTypeHandler(ProtocolTypeHandler... handlerArr) {
        if (handlerArr == null) {
            throw new NullPointerException("Protocol type handler can not be null");
        }
        for (ProtocolTypeHandler handler : handlerArr) {
            if (handlers.containsKey(handler.getProtocolType())) {
                throw new HandlerExistsException("Protocol type already exists: " + handler.getProtocolType());
            }
            handlers.put(handler.getProtocolType(), handler);
        }
    }

    /**
     * 获取消息类型处理器
     *
     * @param protocolType
     * @return
     */
    public static ProtocolTypeHandler getProtocolTypeHandler(Integer protocolType) {
        return handlers.get(protocolType);
    }
}
