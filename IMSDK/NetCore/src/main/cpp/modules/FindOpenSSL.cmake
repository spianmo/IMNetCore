include(ExternalProject)

# openssl
set(OPENSSL_LIB_SUFFIX ${CMAKE_STATIC_LIBRARY_SUFFIX})
set(OPENSSL_SHARED_OPTION)

set(OPENSSL_BUNDLE_DIR "${PROJECT_BINARY_DIR}/OpenSSL-prefix/src/OpenSSL-build")
set(OPENSSL_INCLUDE_DIR "${OPENSSL_BUNDLE_DIR}/include/")
set(OPENSSL_LIBRARY_SSL "${OPENSSL_BUNDLE_DIR}/lib/libssl${OPENSSL_LIB_SUFFIX}")
set(OPENSSL_LIBRARY_CRYPTO "${OPENSSL_BUNDLE_DIR}/lib/libcrypto${OPENSSL_LIB_SUFFIX}")
set(OPENSSL_LIBRARIES ${OPENSSL_LIBRARY_SSL} ${OPENSSL_LIBRARY_CRYPTO})

message(STATUS "Using bundled openssl in '${OPENSSL_BUNDLE_DIR}'")

ExternalProject_Add(OpenSSL
        PREFIX openssl-prefix
        URL "http://sentry.teamhelper.cn:9090/openssl-3.1.2.tar.gz"
        URL_HASH "SHA256=a0ce69b8b97ea6a35b96875235aa453b966ba3cba8af2de23657d8b6767d6539"
        CONFIGURE_COMMAND ./config no-shared -fPIC --prefix=${OPENSSL_BUNDLE_DIR} --libdir=lib
        CMAKE_ARGS
        -DCMAKE_TOOLCHAIN_FILE=${CMAKE_ANDROID_NDK}/build/cmake/android.toolchain.cmake
        -DANDROID_ABI=${CMAKE_ANDROID_ARCH_ABI}
        BUILD_IN_SOURCE 1
        BUILD_BYPRODUCTS ${OPENSSL_LIBRARY_SSL} ${OPENSSL_LIBRARY_CRYPTO})

install(FILES "${OPENSSL_LIBRARY_SSL}" DESTINATION "${OPENSSL_BUNDLE_DIR}/${LIBS_PACKAGE_NAME}"
        COMPONENT "libs-deps")
install(FILES "${OPENSSL_LIBRARY_CRYPTO}" DESTINATION "${OPENSSL_BUNDLE_DIR}/${LIBS_PACKAGE_NAME}"
        COMPONENT "libs-deps")
install(DIRECTORY "${OPENSSL_INCLUDE_DIR}" DESTINATION "${OPENSSL_BUNDLE_DIR}/${LIBS_PACKAGE_NAME}"
        COMPONENT "libs-deps")

include_directories(${OPENSSL_INCLUDE_DIR})
link_directories(${OPENSSL_BUNDLE_DIR}/lib)

add_library(ssl STATIC IMPORTED GLOBAL)
add_library(crypto STATIC IMPORTED GLOBAL)

set_target_properties(ssl PROPERTIES
        IMPORTED_LOCATION ${OPENSSL_LIBRARY_SSL}
)

set_target_properties(crypto PROPERTIES
        IMPORTED_LOCATION ${OPENSSL_LIBRARY_CRYPTO}
)

add_dependencies(ssl OpenSSL)
add_dependencies(crypto OpenSSL)

set(OPENSSL_ROOT_DIR ${OPENSSL_BUNDLE_DIR})
set(OPENSSL_LIBRARIES ${OPENSSL_BUNDLE_DIR}/lib)
set(OPENSSL_INCLUDE_DIR ${OPENSSL_BUNDLE_DIR}/include)