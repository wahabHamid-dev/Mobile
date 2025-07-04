package com.wahab.myapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wahab.myapp.Cheque

class ChequeViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _cheques = MutableLiveData<List<Cheque>>()
    val cheques: LiveData<List<Cheque>> = _cheques

    fun fetchCheques(showCancelled: Boolean = false) {
        val userId = auth.currentUser?.uid ?: return
        var query = firestore.collection("cheques")
            .whereEqualTo("senderId", userId)

        if (!showCancelled) {
            query = query.whereEqualTo("isCancelled", false)
        }

        query.get()
            .addOnSuccessListener { result ->
                val chequeList = result.documents.mapNotNull { doc ->
                    doc.toObject(Cheque::class.java)?.copy(id = doc.id)
                }
                _cheques.postValue(chequeList)
            }
    }

    fun cancelCheque(chequeId: String) {
        firestore.collection("cheques").document(chequeId)
            .update("isCancelled", true)
            .addOnSuccessListener {
                fetchCheques()
            }
    }
}
