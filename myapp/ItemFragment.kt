package com.wahab.myapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ItemFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private val chequeList = mutableListOf<Cheque>()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_item_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TransactionAdapter(chequeList)
        recyclerView.adapter = adapter

        fetchTransactions()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchTransactions() {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId == null) {
            Toast.makeText(requireContext(), "Not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("cheques")
            .whereEqualTo("senderId", currentUserId)
            .whereEqualTo("isCancelled", false) // Only show non-cancelled cheques
            .get()
            .addOnSuccessListener { result ->
                chequeList.clear()
                for (document in result) {
                    val cheque = Cheque(
                        id = document.id,
                        status = document.getString("status") ?: "unknown",
                        payeeName = document.getString("payeeName") ?: "Unknown",
                        beneficiaryName = document.getString("beneficiaryName") ?: "Unknown",
                        amount = document.getString("chequeAmount") ?: "0",
                        senderId = document.getString("senderId") ?: "",
                        receiverId = document.getString("receiverId") ?: "",
                        isCancelled = document.getBoolean("isCancelled") ?: false
                    )
                    chequeList.add(cheque)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load transactions", Toast.LENGTH_SHORT).show()
            }
    }
}
