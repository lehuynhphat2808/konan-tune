package com.example.myapplication.model

import java.util.UUID

data class Category(
    val id: UUID,
    val title: String,
    val parentId: UUID
)