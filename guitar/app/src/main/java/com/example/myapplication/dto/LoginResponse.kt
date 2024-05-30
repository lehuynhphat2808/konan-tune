package com.example.myapplication.dto

import com.example.myapplication.model.User

data class LoginResponse (val token: String, val user: User)