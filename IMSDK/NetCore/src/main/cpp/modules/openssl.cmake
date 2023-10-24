set(OPENSSL_LIB_SUFFIX ${CMAKE_STATIC_LIBRARY_SUFFIX})
set(OPENSSL_SHARED_OPTION no-shared)

set(OPENSSL_BUNDLE_DIR "${PROJECT_BINARY_DIR}/openssl-prefix/src/openssl")
set(OPENSSL_INSTALL_DIR "${OPENSSL_BUNDLE_DIR}/target")
set(OPENSSL_INCLUDE_DIR "${PROJECT_BINARY_DIR}/openssl-prefix/src/openssl/include/")
set(OPENSSL_LIBRARY_SSL "${OPENSSL_INSTALL_DIR}/lib/libssl${OPENSSL_LIB_SUFFIX}")
set(OPENSSL_LIBRARY_CRYPTO "${OPENSSL_INSTALL_DIR}/lib/libcrypto${OPENSSL_LIB_SUFFIX}")
set(OPENSSL_LIBRARIES ${OPENSSL_LIBRARY_SSL} ${OPENSSL_LIBRARY_CRYPTO})

message(STATUS "Using bundled openssl in '${OPENSSL_BUNDLE_DIR}'")

ExternalProject_Add(openssl_ext
        PREFIX "${PROJECT_BINARY_DIR}/openssl-prefix"
        URL "https://github.com/openssl/openssl/releases/download/openssl-3.1.2/openssl-3.1.2.tar.gz"
        URL_HASH "SHA256=a0ce69b8b97ea6a35b96875235aa453b966ba3cba8af2de23657d8b6767d6539"
        CONFIGURE_COMMAND ./config ${OPENSSL_SHARED_OPTION} --prefix=${OPENSSL_INSTALL_DIR} --libdir=lib
        BUILD_IN_SOURCE 1
        BUILD_BYPRODUCTS ${OPENSSL_LIBRARY_SSL} ${OPENSSL_LIBRARY_CRYPTO})
install(FILES "${OPENSSL_LIBRARY_SSL}" DESTINATION "${CMAKE_INSTALL_LIBDIR}/${LIBS_PACKAGE_NAME}"
        COMPONENT "libs-deps")
install(FILES "${OPENSSL_LIBRARY_CRYPTO}" DESTINATION "${CMAKE_INSTALL_LIBDIR}/${LIBS_PACKAGE_NAME}"
        COMPONENT "libs-deps")
install(DIRECTORY "${OPENSSL_INCLUDE_DIR}" DESTINATION "${CMAKE_INSTALL_INCLUDEDIR}/${LIBS_PACKAGE_NAME}"
        COMPONENT "libs-deps")

include_directories("${OPENSSL_INCLUDE_DIR}")
link_directories("${OPENSSL_INSTALL_DIR}/lib")

add_library(ssl STATIC IMPORTED)
add_library(crypto STATIC IMPORTED)

set_target_properties(ssl PROPERTIES
        IMPORTED_LOCATION ${OPENSSL_LIBRARY_SSL}
)

set_target_properties(crypto PROPERTIES
        IMPORTED_LOCATION ${OPENSSL_LIBRARY_CRYPTO}
)

add_dependencies(ssl openssl_ext)
add_dependencies(crypto openssl_ext)