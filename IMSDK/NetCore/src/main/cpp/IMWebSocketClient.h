//
// Created by Finger Ebichu on 2023/10/23.
//

#ifndef IMSDKPROJECT_IMWEBSOCKETCLIENT_H
#define IMSDKPROJECT_IMWEBSOCKETCLIENT_H

#include "WebSocketClient.h"

class IMWebSocketClient : public hv::WebSocketClient {
public:
    IMWebSocketClient(hv::EventLoopPtr loop = NULL) : hv::WebSocketClient(loop) {}

    ~IMWebSocketClient() {}

    int connect(const char *url) {
        onopen = [this]() {
            const HttpResponsePtr &resp = getHttpResponse();
            printf("onopen\n%s\n", resp->body.c_str());
        };
        onmessage = [this](const std::string &msg) {
            printf("onmessage(type=%s len=%d): %.*s\n",
                   opcode() == WS_OPCODE_TEXT ? "text" : "binary",
                   (int) msg.size(), (int) msg.size(), msg.data());
        };
        onclose = []() {
            printf("onclose\n");
        };

        setPingInterval(10000);

        reconn_setting_t reconn;
        reconn_setting_init(&reconn);
        reconn.min_delay = 1000;
        reconn.max_delay = 10000;
        reconn.delay_policy = 2;
        setReconnect(&reconn);

        http_headers headers;
        headers["Origin"] = "http://example.com/";
        return open(url, headers);
    }

};

typedef std::shared_ptr <IMWebSocketClient> MyWebSocketClientPtr;


#endif //IMSDKPROJECT_IMWEBSOCKETCLIENT_H
