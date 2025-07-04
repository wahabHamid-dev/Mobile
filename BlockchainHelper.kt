package com.wahab.myapp

import android.util.Log
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider

object BlockchainHelper {

    private const val TAG = "BlockchainHelper"
    private const val RPC_URL = "https://eth-sepolia.g.alchemy.com/v2/_7-brkn9mTAdgboA9okCM"
    private const val PRIVATE_KEY = "0xb128273f2b326cb2804e92f4905e265af6ba4bd79194430380db0ca60548a9cd"
    private const val CONTRACT_ADDRESS = "0xd9145CCE52D386f254917e481eB44e9943F39138"
    private val web3j: Web3j by lazy {
        Web3j.build(HttpService(RPC_URL))
    }
    private val credentials: Credentials by lazy {
        require(PRIVATE_KEY.matches(Regex("^0x[a-fA-F0-9]{64}$"))) {
            "Invalid PRIVATE_KEY format. Must be a 64-character hex string starting with 0x."
        }
        Credentials.create(PRIVATE_KEY)
    }
    private val transactionManager by lazy {
        RawTransactionManager(web3j, credentials)
    }
    private val contract: ChequeManager by lazy {
        ChequeManager.load(CONTRACT_ADDRESS, web3j, transactionManager, DefaultGasProvider())
    }
    fun issueCheque(
        chequeId: String,
        payer: String,
        beneficiary: String,
        chequeType: String,
        onComplete: (Boolean) -> Unit
    ) {
        Thread {
            try {
                Log.d(TAG, "Issuing cheque with ID: $chequeId")
                val receipt = contract.issueCheque(chequeId, payer, beneficiary, chequeType).send()
                Log.d(TAG, "Cheque issued successfully: ${receipt.transactionHash}")
                onComplete(true)
            } catch (e: Exception) {
                Log.e(TAG, "Error issuing cheque: ${e.message}", e)
                e.printStackTrace()  // 👈 this prints the full error to Logcat
                onComplete(false)}
        }.start()
    }
    fun cancelCheque(chequeId: String, onComplete: (Boolean) -> Unit) {
        Thread {
            try {
                Log.d(TAG, "Cancelling cheque ID: $chequeId")
                val receipt = contract.cancelCheque(chequeId).send()
                Log.d(TAG, "Cheque cancelled: ${receipt.transactionHash}")
                onComplete(true)
            } catch (e: Exception) {
                Log.e(TAG, "Error cancelling cheque: ${e.message}", e)
                onComplete(false)
            }
        }.start()
    }

    fun isChequeValid(chequeId: String, onResult: (Boolean?) -> Unit) {
        Thread {
            try {
                Log.d(TAG, "Checking cheque validity for ID: $chequeId")
                val result = contract.isChequeValid(chequeId).send()
                Log.d(TAG, "Cheque valid: $result")
                onResult(result)
            } catch (e: Exception) {
                Log.e(TAG, "Error checking cheque validity: ${e.message}", e)
                onResult(null)
            }
        }.start()
    }
}