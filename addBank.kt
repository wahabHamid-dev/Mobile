package com.wahab.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddBankFragment : Fragment() {

    private lateinit var bankNameEditText: EditText
    private lateinit var accountNumberEditText: EditText
    private lateinit var accountHolderEditText: EditText
    private lateinit var accountTypeEditText: EditText
    private lateinit var addBankButton: Button

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_bank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bankNameEditText = view.findViewById(R.id.editBankName)
        accountNumberEditText = view.findViewById(R.id.editAccountNumber)
        accountHolderEditText = view.findViewById(R.id.editAccountHolder)
        accountTypeEditText = view.findViewById(R.id.editTextText2)
        addBankButton = view.findViewById(R.id.buttonAddBank)

        addBankButton.setOnClickListener {
            val bankName = bankNameEditText.text.toString().trim()
            val accountNumber = accountNumberEditText.text.toString().trim()
            val accountHolder = accountHolderEditText.text.toString().trim()
            val accountType = accountTypeEditText.text.toString().trim()

            if (bankName.isNotEmpty() && accountNumber.isNotEmpty() && accountHolder.isNotEmpty()) {
                addBankToFirestore(bankName, accountNumber, accountHolder, accountType)
            } else {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addBankToFirestore(name: String, number: String, holder: String, accountType: String ) {
        val userId = auth.currentUser?.uid ?: return

        val bankData = mapOf(
            "bankName" to name,
            "accountNumber" to number,
            "accountHolder" to holder,
            "userId" to userId,
            "accountType" to accountType
        )

        firestore.collection("banks")
            .add(bankData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Bank added", Toast.LENGTH_SHORT).show()
                bankNameEditText.text.clear()
                accountNumberEditText.text.clear()
                accountHolderEditText.text.clear()
                accountTypeEditText.text.clear()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to add bank", Toast.LENGTH_SHORT).show()
            }
    }
}
