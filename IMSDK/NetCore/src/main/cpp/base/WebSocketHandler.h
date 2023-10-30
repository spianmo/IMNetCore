//
// Created by Finger Ebichu on 2023/8/28.
//

#ifndef TRACKERSERVER_WEBSOCKETHANDLER_H
#define TRACKERSERVER_WEBSOCKETHANDLER_H

#include "hv/WebSocketServer.h"
#include "hv/EventLoop.h"
#include "hv/htime.h"

using namespace hv;

class WebSocketHandler {
public:
    WebSocketHandler();

    ~WebSocketHandler() = default;

    void handleMessage(const WebSocketChannelPtr &channel, const std::string &msg, enum ws_opcode opcode);

    void setInterval(int ms, const WebSocketChannelPtr &channel, const std::function<void()> &cb);

    void clearInterval();

    void addListener(const std::function<void(const WebSocketChannelPtr &channel, const std::string &msg,
                                              enum ws_opcode opcode)> &listener);

    TimerID timerId;
    std::vector<std::function<void(const WebSocketChannelPtr &channel, const std::string &msg,
                                   enum ws_opcode opcode)>> listeners;
};


#endif //TRACKERSERVER_WEBSOCKETHANDLER_H
