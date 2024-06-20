package com.teamhelper.imsdk.data

/**
 * @Description:
 * @Author: ZhangLuo (Moss)
 * @Email: zhangluo.chn@gmail.com
 * @Date: 2023/10/24
 */

data class LoginDataContent(
    val loginUserId: String?,
    val loginToken: String?,
    val extra: String?,
    val firstLoginTime: Long?,
    val timestamp: Long?
)
