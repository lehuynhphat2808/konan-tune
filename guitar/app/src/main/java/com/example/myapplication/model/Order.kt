package com.example.myapplication.model

import java.time.LocalDateTime
import java.util.UUID

class Order(val id: UUID?, val orderDate: String?, val address: String,
                 val status: OrderStatus, val userId: UUID, val cartId: UUID, val contactPhoneNumber: String?)
