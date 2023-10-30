package com.teamhelper.imsdk.netcore.data

/**
 * @Description:
 * @Author: ZhangLuo (Moss)
 * @Email: zhangluo.chn@gmail.com
 * @Date: 2023/10/24
 */
data class LoginDataContent(
    /**
     * 授权token
     */
    val token: String? = null,

    /**
     * 客户自定义的扩展数据
     * 比如可以实现token以外的其他方式鉴权
     */
    val extendData: String? = null,

    /**
     * 最后一次登录的毫秒级时间戳
     */
    val lastLoginTime: Long? = null
)
