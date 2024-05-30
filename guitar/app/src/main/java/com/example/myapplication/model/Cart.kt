package com.example.myapplication.model

import java.util.UUID

data class Cart (val id: UUID, val totalAmount: Double, val cartProducts: List<CartProduct>)