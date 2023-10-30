//
// Created by Finger Ebichu on 2023/8/25.
//

#ifndef TRACKERSERVER_HTTPHANDLERREGISTRY_H
#define TRACKERSERVER_HTTPHANDLERREGISTRY_H

#include <iostream>
#include <map>
#include "hv/HttpService.h"

#if defined(__APPLE__) || defined(__linux__)

#include <fmt/format.h>

using namespace fmt;
#else

#include <format>

#endif

// enum: http_method
enum HTTP_METHOD {
    HTTP_METHOD_GET = 0,
    HTTP_METHOD_POST = 1,
    HTTP_METHOD_PUT = 2,
    HTTP_METHOD_DELETE = 3,
    HTTP_METHOD_PATCH = 4,
    HTTP_METHOD_HEAD = 5,
};

class HttpMethodMeta {
public:
    HttpMethodMeta() = default;

    HTTP_METHOD method;
    std::string path;
    std::function<int(HttpRequest *, HttpResponse *)> func;
};

class HttpControllerMeta {
public:
    std::string path;
    std::vector<HttpMethodMeta> methods;
};

class HttpHandlerRegistry {
public:

    static void registerMethod(const std::string &path, const std::string &subPath, HTTP_METHOD method,
                               std::function<int(HttpRequest *, HttpResponse *)> func);

    static void init(hv::HttpService *router);

private:
    static std::unordered_map<std::string, HttpControllerMeta> *paths;
};

#define $HttpService _http = new HttpService();
#define $WebSocketService _ws = new WebSocketService();
#define $Server WebSocketServer _server; \
struct Startup {                         \
    WebSocketService* ws;                \
    HttpService* http;                   \
    WebSocketServer server;              \
    Startup() {                          \
HttpService* _http = nullptr;            \
WebSocketService* _ws = nullptr;
#define $endDefServer  if (_ws != nullptr) { \
        this->ws = _ws;                      \
    }                                        \
    if (_http != nullptr) {                  \
        HttpHandlerRegistry::init(_http);    \
        this->http = _http;                  \
    }                                        \
    this->server = _server;                  \
    }                                        \
};                                           \
static Startup global;

#define $serverWorkThreadNum(num) global.server.setThreadNum(num);
#define $serverPort(port) global.server.setPort(port);
#define $serverSSL(port, _crt_file, _key_file)     global.server.https_port = port;\
    hssl_ctx_opt_t param;                                                          \
    memset(&param, 0, sizeof(param));                                              \
    param.crt_file = _crt_file;                                                    \
    param.key_file = _key_file;                                                    \
    param.endpoint = HSSL_SERVER;                                                  \
    if (global.server.newSslCtx(&param) != 0) {                                    \
        fprintf(stderr, "new SSL_CTX failed!\n");                                  \
        return -20;\
    }

#define $serverStart()     if (global.ws != nullptr) { \
        global.server.registerWebSocketService(global.ws); \
    }                                                  \
    if (global.http != nullptr) {                      \
        global.server.registerHttpService(global.http);\
    }                                                  \
    global.server.start();
#define $serverStop() global.server.stop();

#define $Router(className, _path) class className { \
    protected:                                      \
        std::string path;                           \
    public:                                         \
    className() {                                   \
        this->path = _path;
#define $endDefRouter }};
#define $GET(_path, func) HttpHandlerRegistry::registerMethod(this->path, _path, HTTP_METHOD_GET, func);
#define $POST(_path, func) HttpHandlerRegistry::registerMethod(this->path, _path, HTTP_METHOD_POST, func);
#define $PUT(_path, func) HttpHandlerRegistry::registerMethod(this->path, _path, HTTP_METHOD_PUT, func);
#define $DELETE(_path, func) HttpHandlerRegistry::registerMethod(this->path, _path, HTTP_METHOD_DELETE, func);
#define $PATCH(_path, func) HttpHandlerRegistry::registerMethod(this->path, _path, HTTP_METHOD_PATCH, func);
#define $HEAD(_path, func) HttpHandlerRegistry::registerMethod(this->path, _path, HTTP_METHOD_HEAD, func);

#define $wsOpen(func) global.ws->onopen = func;
#define $wsSubscribe(func) ctx->addListener(func);
#define $wsMessage(func) global.ws->onmessage = func;
#define $wsClose(func) global.ws->onclose = func;

#define $staticHTML(path, dir) _http->Static(path, dir);
#define $forwardProxy _http->EnableForwardProxy();
#define $trustProxy(host) _http->AddTrustProxy(host);
#define $proxy(path, url) _http->Proxy(path, url);
#define $allowCORS _http->AllowCORS();

#if defined(__APPLE__) || defined(__linux__)
#define FMT fmt
#else
#define FMT std
#endif

#define LOGE(...)                        \
  do {                                               \
    fprintf(stderr, "%s:%s:%d ", __FILE__, __func__, \
            static_cast<int>(__LINE__));             \
    fprintf(stderr, ##__VA_ARGS__);                  \
    fprintf(stderr, "\n");                           \
  } while (0)

#endif //TRACKERSERVER_HTTPHANDLERREGISTRY_H
