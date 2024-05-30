package com.example.myapplication.dto

import java.math.BigDecimal

data class UserDetail(
    val nickname: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val linkImage: String? = null,
    val coin: BigDecimal? = null,
    val password: String? = null
)