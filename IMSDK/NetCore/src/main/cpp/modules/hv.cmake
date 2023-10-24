option(WITH_OPENSSL "with openssl library" ON)
option(WITH_KCP "compile event/kcp" ON)


# libhv
set(LIBHV_VERSION master)
include(ExternalProject)

ExternalProject_Add(libhv_ext
        GIT_REPOSITORY https://github.com/ithewei/libhv.git
        INSTALL_COMMAND ""
        CMAKE_ARGS
        -DBUILD_EXAMPLES=OFF
        -DWITH_OPENSSL=${WITH_OPENSSL}
        -DWITH_KCP=${WITH_KCP}
        -DBUILD_STATIC=ON
        -DBUILD_SHARED=OFF
        -DCMAKE_BUILD_TYPE=Release
        BUILD_BYPRODUCTS ${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/lib/libhv_static${CMAKE_STATIC_LIBRARY_SUFFIX}
        UPDATE_COMMAND ""
        INSTALL_COMMAND ""
        GIT_TAG ${LIBHV_VERSION})


ExternalProject_Get_property(libhv_ext INSTALL_DIR)

set(libhv_ext_INCLUDE "${INSTALL_DIR}/src/libhv_ext-build/include")
set(libhv_ext_LIB "${INSTALL_DIR}/src/libhv_ext-build/lib")

include_directories(${libhv_ext_INCLUDE})
link_directories(${libhv_ext_LIB})

add_library(libhv STATIC IMPORTED)

set_target_properties(libhv PROPERTIES
        IMPORTED_LOCATION ${CMAKE_CURRENT_BINARY_DIR}/libhv_ext-prefix/src/libhv_ext-build/lib/libhv_static${CMAKE_STATIC_LIBRARY_SUFFIX}
)

add_dependencies(libhv libhv_ext)

if (ANDROID)
    set(LIBS ${LIBS} log)
elseif (UNIX)
    set(LIBS ${LIBS} pthread m dl)
    if (CMAKE_COMPILER_IS_GNUCC)
        set(LIBS ${LIBS} rt)
    endif ()
endif ()

if (WITH_OPENSSL)
    add_definitions(-DWITH_OPENSSL)
    include(openssl)
    set(LIBS ${LIBS} ssl crypto)
endif ()

target_link_libraries(libhv INTERFACE ${LIBS})