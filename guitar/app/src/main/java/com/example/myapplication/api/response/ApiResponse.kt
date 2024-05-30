package com.example.myapplication.api.response

data class ApiResponse<T>(
    val message: String,
    val data: T
)
