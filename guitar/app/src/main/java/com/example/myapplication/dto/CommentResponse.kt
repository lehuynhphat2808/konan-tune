package com.example.myapplication.dto

import com.example.myapplication.model.User
import java.util.UUID

data class CommentResponse (val id: UUID?, val user: UserDetail, val productId: UUID, var cmt: String, var rate: Int)