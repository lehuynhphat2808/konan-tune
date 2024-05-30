package com.example.myapplication.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class InsertVoucherRequest (val id: UUID?, var code: String, var startAt: LocalDate, var endAt: LocalDate, var discount: BigDecimal)