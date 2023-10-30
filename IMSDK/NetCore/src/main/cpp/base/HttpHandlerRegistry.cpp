//
// Created by Finger Ebichu on 2023/8/25.
//
#include "HttpHandlerRegistry.h"

#include <utility>

#define INITIAL_BUCKET_COUNT 4
std::unordered_map<std::string, HttpControllerMeta> *HttpHandlerRegistry::paths;

std::string checkPrefix(std::string path) {
    if (path[0] != '/') {
        path = "/" + path;
    }
    return path;
}

std::string http_method_str(HTTP_METHOD method) {
    switch (method) {
        case HTTP_METHOD_GET:
            return "GET";
        case HTTP_METHOD_POST:
            return "POST";
        case HTTP_METHOD_PUT:
            return "PUT";
        case HTTP_METHOD_DELETE:
            return "DELETE";
        case HTTP_METHOD_PATCH:
            return "PATCH";
        case HTTP_METHOD_HEAD:
            return "HEAD";
    }
}

void HttpHandlerRegistry::registerMethod(const std::string &path, const std::string &subPath, HTTP_METHOD method,
                                         std::function<int(HttpRequest *, HttpResponse *)> func) {
    if (paths == nullptr) {
        paths = new std::unordered_map<std::string, HttpControllerMeta>(INITIAL_BUCKET_COUNT);
        paths->max_load_factor(0.5);
    }
    std::string _path = checkPrefix(path);
    std::string _subPath = checkPrefix(subPath);
    auto *meta = new HttpMethodMeta();
    meta->method = method;
    meta->path = FMT::format("{}{}", _path, _subPath);
    meta->func = std::move(func);
    (*HttpHandlerRegistry::paths)[_path].methods.push_back(*meta);
}

void HttpHandlerRegistry::init(hv::HttpService *router) {
    if (router == nullptr) {
        std::cout << "Please explicitly register $Http Service in $Server" << std::endl;
        exit(-1);
    }
    for (auto [path, meta]: *HttpHandlerRegistry::paths) {
        for (const auto &httpMethod: meta.methods) {
            std::cout << FMT::format("register path: {} method: {}", httpMethod.path,
                                     http_method_str(httpMethod.method)) << std::endl;
            switch (httpMethod.method) {
                case HTTP_METHOD_GET:
                    router->GET(httpMethod.path.c_str(), httpMethod.func);
                    break;
                case HTTP_METHOD_POST:
                    router->POST(httpMethod.path.c_str(), httpMethod.func);
                    break;
                case HTTP_METHOD_PUT:
                    router->PUT(httpMethod.path.c_str(), httpMethod.func);
                    break;
                case HTTP_METHOD_DELETE:
                    router->Delete(httpMethod.path.c_str(), httpMethod.func);
                    break;
                case HTTP_METHOD_PATCH:
                    router->PATCH(httpMethod.path.c_str(), httpMethod.func);
                    break;
                case HTTP_METHOD_HEAD:
                    router->HEAD(httpMethod.path.c_str(), httpMethod.func);
                    break;
            }
        }
    }
}
