package com.example.myapplication.model

import java.util.UUID

data class User(
    val id: UUID,
    val cartId: UUID,
    val nickname: String?,
    val role: Role?
)