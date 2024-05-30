package com.example.myapplication.api.response

data class PaginatedResponse<T>(
    val elements: List<T>,
    val totalElements: Long,
    val totalPages: Int
)
