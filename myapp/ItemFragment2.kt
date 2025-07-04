package com.wahab.myapp

import ReceivedAdapter
import ReceivedCheque
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

class ReceivedChequesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReceivedAdapter
    private val chequeList = mutableListOf<ReceivedCheque>()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_item_list2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.list1)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReceivedAdapter(chequeList)
        recyclerView.adapter = adapter

        fetchReceivedCheques()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchReceivedCheques() {
        val currentEmail = auth.currentUser?.email?.lowercase() ?: return

        // Query for cheques where beneficiaryEmail matches the current user's email
        firestore.collection("cheques")
            .whereEqualTo("beneficiaryEmail", currentEmail) // This is assuming cheques are directly under 'cheques' collection
            .get()
            .addOnSuccessListener { result ->
                chequeList.clear()

                // Map Firestore documents to ReceivedCheque objects
                val newCheques = result.documents.mapNotNull { document ->
                    document.toObject(ReceivedCheque::class.java)
                }

                chequeList.addAll(newCheques) // Add new cheques to the list
                adapter.notifyDataSetChanged() // Notify the adapter of the data change
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error fetching cheques", Toast.LENGTH_SHORT).show()
            }
    }
}
