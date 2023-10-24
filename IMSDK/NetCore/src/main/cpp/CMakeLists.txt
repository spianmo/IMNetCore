cmake_minimum_required(VERSION 3.22)

project("netcore")

set(CMAKE_CXX_STANDARD 14)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wextra -Wpedantic -Wno-unused-parameter -Wno-unused-variable -Wno-unused-function -Wno-unused-private-field -Wno-unused-const-variable -Wno-unused-lambda-capture -Wno-unused-local-typedefs -Wno-unused-const-variable -fexceptions -fpermissive -fPIC -Wl,-Bsymbolic -fvisibility=hidden -fvisibility-inlines-hidden -DFIXED_POINT -Wno-pedantic -Wdeprecated-declarations -O2")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -Wextra -Wpedantic -Wno-unused-parameter -Wno-unused-variable -Wno-unused-function -Wno-unused-private-field -Wno-unused-const-variable -Wno-unused-lambda-capture -Wno-unused-local-typedefs -Wno-unused-const-variable -fexceptions -fpermissive -fPIC -Wl,-Bsymbolic -fvisibility=hidden -fvisibility-inlines-hidden -DFIXED_POINT -Wno-pedantic -Wdeprecated-declarations -O2")

include_directories(${CMAKE_SOURCE_DIR})

# Add path for custom CMake modules.
list(APPEND CMAKE_MODULE_PATH
        "${CMAKE_CURRENT_SOURCE_DIR}/modules")

include(hv)

add_library(${CMAKE_PROJECT_NAME} SHARED
        netcore.cpp
        IMWebSocketClient.h
)

target_link_libraries(${CMAKE_PROJECT_NAME}
        libhv
        android
        log)