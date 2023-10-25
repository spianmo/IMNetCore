option(WITH_OPENSSL "with openssl library" ON)
option(WITH_KCP "compile event/kcp" ON)

include(ExternalProject)

if (WITH_OPENSSL)
    add_definitions(-DWITH_OPENSSL)
    include(${CMAKE_CURRENT_SOURCE_DIR}/modules/FindOpenSSL.cmake)
endif ()

# libhv
set(LIBHV_VERSION master)

ExternalProject_Add(libhv_ext
        GIT_REPOSITORY https://gitee.com/libhv/libhv.git
        INSTALL_COMMAND ""
        CMAKE_ARGS
        -DCMAKE_TOOLCHAIN_FILE=${CMAKE_ANDROID_NDK}/build/cmake/android.toolchain.cmake
        -DANDROID_ABI=${CMAKE_ANDROID_ARCH_ABI}
        -DBUILD_EXAMPLES=OFF
        -DWITH_OPENSSL=${WITH_OPENSSL}
        -DWITH_KCP=${WITH_KCP}
        -DBUILD_STATIC=ON
        -DBUILD_SHARED=OFF
        -DCMAKE_BUILD_TYPE=Release
        BUILD_BYPRODUCTS ${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/lib/libhv_static${CMAKE_STATIC_LIBRARY_SUFFIX}
        UPDATE_COMMAND ""
        INSTALL_COMMAND ""
        GIT_TAG ${LIBHV_VERSION}
        DEPENDS OpenSSL)

ExternalProject_Get_property(libhv_ext INSTALL_DIR)

set(libhv_ext_INCLUDE "${INSTALL_DIR}/src/libhv_ext-build/include")
set(libhv_ext_LIB "${INSTALL_DIR}/src/libhv_ext-build/lib")

include_directories(${libhv_ext_INCLUDE})
link_directories(${libhv_ext_LIB})

add_library(libhv STATIC IMPORTED GLOBAL)

set_target_properties(libhv PROPERTIES
        IMPORTED_LOCATION ${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/lib/libhv_static${CMAKE_STATIC_LIBRARY_SUFFIX}
)

add_dependencies(libhv libhv_ext)

set(LIBS ${LIBS} log)


target_link_libraries(libhv INTERFACE ${LIBS})