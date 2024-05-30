package com.example.myapplication.dto

data class RegisterRequest(
    val username: String,
    val password: String,
    val nickname: String,
    val phone: String,
    val email: String
)