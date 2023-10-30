//
// Created by Finger Ebichu on 2023/8/28.
//

#include "WebSocketHandler.h"


WebSocketHandler::WebSocketHandler() {
    timerId = INVALID_TIMER_ID;
}

void
WebSocketHandler::handleMessage(const WebSocketChannelPtr &channel, const std::string &msg, enum ws_opcode opcode) {
//    std::cout << "onMessage(type=" << (opcode == WS_OPCODE_TEXT ? "text" : "binary") << " len=" << msg.size()
//              << "): " << msg << std::endl;
    for (auto &listener: listeners) {
        listener(channel, msg, opcode);
    }
}

void WebSocketHandler::setInterval(int ms, const WebSocketChannelPtr &channel,
                                   const std::function<void()> &cb) {
    this->timerId = hv::setInterval(ms, [channel, cb](hv::TimerID id) {
        if (channel->isConnected() && channel->isWriteComplete()) {
            cb();
        }
    });
}

void WebSocketHandler::clearInterval() {
    if (this->timerId != INVALID_TIMER_ID) {
        killTimer(this->timerId);
        this->timerId = INVALID_TIMER_ID;
    }
}

void WebSocketHandler::addListener(
        const std::function<void(const WebSocketChannelPtr &channel, const std::string &, enum ws_opcode)> &listener) {
    listeners.push_back(listener);
}
