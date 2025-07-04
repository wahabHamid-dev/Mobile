package com.wahab.myapp

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

class CancelChequesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CancelChequeAdapter
    private val chequeList = mutableListOf<Cheque>()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_cancel_cheques, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerView1)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CancelChequeAdapter(chequeList) { chequeId ->
            cancelCheque(chequeId)
        }
        recyclerView.adapter = adapter

        fetchCheques()
    }

    private fun fetchCheques() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("cheques")
            .whereEqualTo("senderId", userId)
            .whereEqualTo("isCancelled", false)
            .whereEqualTo("status", "created") // Filter only created, non-cancelled
            .get()
            .addOnSuccessListener { result ->
                chequeList.clear()
                for (doc in result) {
                    val cheque = doc.toObject(Cheque::class.java).copy(id = doc.id)
                    chequeList.add(cheque)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun cancelCheque(chequeId: String) {
        firestore.collection("cheques").document(chequeId)
            .update("isCancelled", true, "status", "cancelled")
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Cheque cancelled", Toast.LENGTH_SHORT).show()
                fetchCheques()
            }
    }
}
