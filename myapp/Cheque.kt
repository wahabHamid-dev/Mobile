package com.wahab.myapp

data class Cheque(
    val id: String = "",
    val status: String = "",
    val payeeName: String = "",
    val beneficiaryName: String = "",
    val amount: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val isCancelled: Boolean = false
)
