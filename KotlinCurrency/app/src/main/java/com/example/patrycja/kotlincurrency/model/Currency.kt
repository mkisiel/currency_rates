package com.example.patrycja.kotlincurrency.model

data class Table(
        val effectiveDate: String,
        val no: String,
        val rates: List<Rate>,
        val table: String
)

data class Rate(
    val code: String,
    val currency: String,
    val mid: Double
)