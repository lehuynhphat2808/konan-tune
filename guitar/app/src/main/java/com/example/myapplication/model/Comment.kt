package com.example.myapplication.model

import java.util.UUID

data class Comment (val id: UUID?, val userId: UUID, val productId: UUID, var cmt: String, var rate: Int)