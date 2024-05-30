package com.example.myapplication.model

import java.util.UUID

data class CartProduct (val id: UUID, val product: Product, var quantity: Int, var selected: Boolean)