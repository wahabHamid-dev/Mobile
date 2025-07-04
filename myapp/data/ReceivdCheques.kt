package com.wahab.myapp.data

data class ReceivedCheques(
    val id: String = "",
    val payerName: String = "",
    val chequeAmount: String = "",
    val bankName: String = "",
    val isCancelled: Boolean = false
)
