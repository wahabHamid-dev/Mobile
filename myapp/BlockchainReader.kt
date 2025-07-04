package com.wahab.myapp

import android.util.Log
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider

class BlockchainReader {

    private val contractAddress = "0xc88dD5E5CaFd78e67AA107C5bEc9C12dC2b2aA52"
    private val privateKey = "0xb128273f2b326cb2804e92f4905e265af6ba4bd79194430380db0ca60548a9cd"
    private val web3 = Web3j.build(HttpService("https://rpc.gensyn.network"))
    private val credentials = Credentials.create(privateKey)
    private val txManager = RawTransactionManager(web3, credentials)
    private val contract = ChequeManager.load(contractAddress, web3, txManager, DefaultGasProvider())

    fun checkChequeValid(chequeId: String, callback: (Boolean) -> Unit) {
        Thread {
            try {
                val isValid = contract.isChequeValid(chequeId).send()
                Log.d("BlockchainReader", "Cheque $chequeId is valid? $isValid")
                callback(isValid)
            } catch (e: Exception) {
                Log.e("BlockchainReader", "Validation error", e)
                callback(false)
            }
        }.start()
    }
}
