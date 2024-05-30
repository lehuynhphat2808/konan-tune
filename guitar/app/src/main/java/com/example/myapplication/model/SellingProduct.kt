package com.example.myapplication.model

import java.math.BigDecimal
import java.util.UUID

class SellingProduct(
    id: UUID?,
    name: String?,
    summary: String?,
    content: String?,
    brand: String?,
    color: String?,
    price: BigDecimal?,
    linkImages: List<String>?,
    val categoryId: UUID?,
    val userId: UUID?,
    val deleted: Boolean? = null,
    quantity: Int? = 987
) : Product(id, name, summary, content, brand, color, price, linkImages, quantity)