include(FetchContent)

# 声明 fmt 库的 FetchContent 依赖
FetchContent_Declare(
        fmt
        GIT_REPOSITORY https://github.com/fmtlib/fmt.git
        GIT_TAG 10.1.1  # 指定所需的 fmt 版本或分支
)

# 强制编译 fmt 作为一个静态库
set(FMT_STATIC_LIBS TRUE)

# 引入 fmt 依赖
FetchContent_MakeAvailable(fmt)
