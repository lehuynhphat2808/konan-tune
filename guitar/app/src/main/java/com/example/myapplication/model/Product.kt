package com.example.myapplication.model

import java.math.BigDecimal
import java.util.UUID

open class Product(
    var id: UUID?,
    var name: String?,
    var summary: String?,
    var content: String?,
    var brand: String?,
    var color: String?,
    var price: BigDecimal?,
    var linkImages: List<String>?,
    var quantity: Int? = 999
)



