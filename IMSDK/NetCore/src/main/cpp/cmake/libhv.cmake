option(WITH_OPENSSL "with openssl library" ON)
option(WITH_KCP "compile event/kcp" ON)


# libhv
set(LIBHV_VERSION master)
include(ExternalProject)

ExternalProject_Add(libhv_ext
        GIT_REPOSITORY https://gitee.com/spianmo/libhv.git
        CMAKE_ARGS
        -DCMAKE_TOOLCHAIN_FILE=${CMAKE_ANDROID_NDK}/build/cmake/android.toolchain.cmake
        -DANDROID_ABI=${CMAKE_ANDROID_ARCH_ABI}
        -DBUILD_EXAMPLES=OFF
        -DWITH_OPENSSL=${WITH_OPENSSL}
        -DWITH_KCP=${WITH_KCP}
        -DBUILD_STATIC=ON
        -DBUILD_SHARED=OFF
        -DCMAKE_BUILD_TYPE=Release
        -DANDROID_NDK_ROOT=${CMAKE_ANDROID_NDK}
        -DANDROID_PLATFORM=${ANDROID_PLATFORM}
        BUILD_BYPRODUCTS ${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/lib/libhv_static${CMAKE_STATIC_LIBRARY_SUFFIX}
        BUILD_BYPRODUCTS ${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/lib/libcrypto${CMAKE_STATIC_LIBRARY_SUFFIX}
        BUILD_BYPRODUCTS ${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/lib/libssl${CMAKE_STATIC_LIBRARY_SUFFIX}
        UPDATE_COMMAND ""
        INSTALL_COMMAND ""
        GIT_TAG ${LIBHV_VERSION})


ExternalProject_Get_property(libhv_ext INSTALL_DIR)

set(libhv_ext_INCLUDE "${INSTALL_DIR}/src/libhv_ext-build/include")
set(libhv_ext_LIB "${INSTALL_DIR}/src/libhv_ext-build/lib")

include_directories(${libhv_ext_INCLUDE})
link_directories(${libhv_ext_LIB})

add_library(libhv STATIC IMPORTED GLOBAL)

set_target_properties(libhv PROPERTIES
        IMPORTED_LOCATION ${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/lib/libhv_static${CMAKE_STATIC_LIBRARY_SUFFIX}
)

include_directories(${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/include)

add_library(crypto STATIC IMPORTED GLOBAL)

set_target_properties(crypto PROPERTIES
        IMPORTED_LOCATION ${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/lib/libcrypto${CMAKE_STATIC_LIBRARY_SUFFIX}
)

add_library(ssl STATIC IMPORTED GLOBAL)

set_target_properties(ssl PROPERTIES
        IMPORTED_LOCATION ${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/lib/libssl${CMAKE_STATIC_LIBRARY_SUFFIX}
)
add_dependencies(libhv libhv_ext)