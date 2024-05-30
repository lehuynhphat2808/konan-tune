package com.example.myapplication.model

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class Voucher (var id: UUID?, var code: String, var startAt: String, var endAt: String, var discount: BigDecimal)