cmake_minimum_required(VERSION 3.22)

project("netcore")

set(CMAKE_CXX_STANDARD 14)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fPIC -O2 --coverage")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fPIC -O2 --coverage")

include_directories(${CMAKE_SOURCE_DIR})

# Add path for custom CMake modules.
list(APPEND CMAKE_MODULE_PATH
        "${CMAKE_CURRENT_SOURCE_DIR}/cmake")

include(cmake/libhv.cmake)
include(cmake/libfmt.cmake)


add_library(netcore SHARED
        netcore.cpp
        IMWebSocketClient.h
        base/HttpHandlerRegistry.h
        base/HttpHandlerRegistry.cpp
        base/WebSocketHandler.cpp
        base/WebSocketHandler.h
        util.h
)

target_link_libraries(netcore
        crypto
        ssl
        libhv
        fmt
        android
        log)