package com.wahab.myapp

import android.util.Log
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider

class BlockchainWriter {

    private val contractAddress = "0xc88dD5E5CaFd78e67AA107C5bEc9C12dC2b2aA52"
    private val privateKey = "0xb128273f2b326cb2804e92f4905e265af6ba4bd79194430380db0ca60548a9cd"
    private val web3 = Web3j.build(HttpService("https://rpc.gensyn.network"))
    val credentials = WalletUtils.loadBip39Credentials(
        "YOUR_PASSPHRASE",
        "language tunnel neck fetch limb snow van believe vivid display deer juice"
    )
    private val txManager = RawTransactionManager(web3, credentials)
    private val contract = ChequeManager.load(contractAddress, web3, txManager, DefaultGasProvider())
    fun issueCheque(chequeId: String, payer: String, beneficiary: String, chequeType: String) {
        Thread {
            try {
                val receipt = contract.issueCheque(chequeId, payer, beneficiary, chequeType).send()
                Log.d("BlockchainWriter", "Issued: ${receipt.transactionHash}")
            } catch (e: Exception) {
                Log.e("BlockchainWriter", "Error issuing cheque", e)
            }
        }.start()
    }

    fun cancelCheque(chequeId: String) {
        Thread {
            try {
                val receipt = contract.cancelCheque(chequeId).send()
                Log.d("BlockchainWriter", "Cancelled: ${receipt.transactionHash}")
            } catch (e: Exception) {
                Log.e("BlockchainWriter", "Error cancelling cheque", e)
            }
        }.start()
    }
}
