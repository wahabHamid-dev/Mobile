package com.wahab.myapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Dashboard : Fragment() {

    private lateinit var bankCardContainer: LinearLayout
    private lateinit var createChequeButton: LinearLayout
    private lateinit var receivedChequesButton: LinearLayout
    private lateinit var transactionsButton: LinearLayout
    private lateinit var cancelChequeButton: LinearLayout
    private lateinit var addBankButton: Button

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bankCardContainer = view.findViewById(R.id.bankCardContainer)
        createChequeButton = view.findViewById(R.id.linearLayout2)
        receivedChequesButton = view.findViewById(R.id.linearLayout)
        transactionsButton = view.findViewById(R.id.linearLayout4)
        cancelChequeButton = view.findViewById(R.id.linearLayout3)
        addBankButton = view.findViewById(R.id.addbankaccount)

        val userId = auth.currentUser?.uid ?: return

        firestore.collection("banks")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val bankName = document.getString("bankName") ?: "Unknown Bank"
                    val accountNumber = document.getString("accountNumber") ?: "N/A"
                    val accountholder = document.getString("accountHolder") ?: "N/A"
                    val accountType = document.getString("accountType") ?: "N/A"

                    val cardView = CardView(requireContext()).apply {
                        radius = 16f
                        setCardBackgroundColor(Color.parseColor("#F3F3F3"))
                        setContentPadding(24, 24, 24, 24)
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        ).apply {
                            setMargins(16, 0, 16, 0)
                        }

                        addView(TextView(requireContext()).apply {
                            text = "Account Holder Name: $accountholder\n Account Type: $accountType\n Bank Name: $bankName\nAccount: $accountNumber"
                            textSize = 16f
                        })
                    }

                    bankCardContainer.addView(cardView)
                }
            }
            .addOnFailureListener {
            }
        view.findViewById<LinearLayout>(R.id.linearLayout2).setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_create_Cheques)
        }

        view.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_itemFragment2)
        }

        view.findViewById<LinearLayout>(R.id.linearLayout3).setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_cancelCheques)
        }

        view.findViewById<LinearLayout>(R.id.linearLayout4).setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_itemFragment)
        }

        view.findViewById<Button>(R.id.addbankaccount).setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_addBank)
        }
    }

}
