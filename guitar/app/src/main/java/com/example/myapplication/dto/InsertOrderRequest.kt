package com.example.myapplication.dto

import com.example.myapplication.model.OrderStatus
import java.util.UUID

data class InsertOrderRequest(val address: String, val status: OrderStatus, val userId: UUID, val cartId: UUID, val contactPhoneNumber: String)