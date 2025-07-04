package com.wahab.myapp

import android.util.Log
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.RawTransactionManager


class BlockchainManager(
    private val contractAddress: String,
    private val privateKey: String
) {
    private val web3: Web3j = Web3j.build(HttpService("https://rpc.gensyn.network"))
    private val credentials: Credentials = Credentials.create(privateKey)
    private val txManager = RawTransactionManager(web3, credentials)
    private val contract = ChequeManager.load(contractAddress, web3, txManager, DefaultGasProvider())

    fun issueCheque(id: String, payer: String, beneficiary: String, type: String) {
        Thread {
            try {
                val receipt = contract.issueCheque(id, payer, beneficiary, type).send()
                Log.d("Blockchain", "Cheque issued: ${receipt.transactionHash}")
            } catch (e: Exception) {
                Log.e("Blockchain", "Error issuing cheque", e)
            }
        }.start()
    }

    fun checkChequeValid(id: String, callback: (Boolean) -> Unit) {
        Thread {
            try {
                val isValid = contract.isChequeValid(id).send()
                callback(isValid)
            } catch (e: Exception) {
                Log.e("Blockchain", "Error checking cheque", e)
                callback(false)
            }
        }.start()
    }

    fun cancelCheque(id: String) {
        Thread {
            try {
                val receipt = contract.cancelCheque(id).send()
                Log.d("Blockchain", "Cheque cancelled: ${receipt.transactionHash}")
            } catch (e: Exception) {
                Log.e("Blockchain", "Error cancelling cheque", e)
            }
        }.start()
    }
}
