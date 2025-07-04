package com.wahab.myapp

data class Transaction(
    val id: String,
    val payerName: String,
    val chequeAmount: String,
    val beneficiaryName: String,
    val chequeType: String
)
