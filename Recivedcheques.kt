data class ReceivedCheque(
    val chequeId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val beneficiaryEmail: String = "",
    val beneficiaryName: String = "",
    val chequeAmount: String = "",
    val bankName: String = "",
    val chequeType: String = "",
    val validTill: String = "",
    val isCancelled: Boolean = false,
    val payerName: String = "",
    val timestamp: Long = 0L
)