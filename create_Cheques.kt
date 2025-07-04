package com.wahab.myapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CreateChequeFragment : Fragment() {

    private lateinit var beneficiaryNameEditText: EditText
    private lateinit var chequeAmountEditText: EditText
    private lateinit var bankNameEditText: EditText
    private lateinit var beneficiaryEmailEditText: EditText
    private lateinit var validTillEditText: EditText
    private lateinit var chequeTypeGroup: RadioGroup
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create__cheques, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        beneficiaryNameEditText = view.findViewById(R.id.editTextText)
        chequeAmountEditText = view.findViewById(R.id.editTextNumber)
        bankNameEditText = view.findViewById(R.id.editTextNumber2)
        beneficiaryEmailEditText = view.findViewById(R.id.editTextNumber3)
        validTillEditText = view.findViewById(R.id.editTextDate)
        chequeTypeGroup = view.findViewById(R.id.chequeTypeGroup)
        submitButton = view.findViewById(R.id.submitChequeTypeButton)

        validTillEditText.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    val date = "%02d/%02d/%04d".format(d, m + 1, y)
                    validTillEditText.setText(date)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.minDate = System.currentTimeMillis()
                show()
            }
        }

        submitButton.setOnClickListener {
            val name = beneficiaryNameEditText.text.toString().trim()
            val amount = chequeAmountEditText.text.toString().trim()
            val bank = bankNameEditText.text.toString().trim()
            val rawEmail = beneficiaryEmailEditText.text.toString().trim()
            val email = rawEmail.lowercase(Locale.getDefault())
            val validTill = validTillEditText.text.toString().trim()
            val selectedRb =
                chequeTypeGroup.findViewById<RadioButton>(chequeTypeGroup.checkedRadioButtonId)
            val chequeType = selectedRb?.text?.toString() ?: ""

            if (name.isNotEmpty() && amount.isNotEmpty() && bank.isNotEmpty()
                && email.isNotEmpty() && validTill.isNotEmpty() && chequeType.isNotEmpty()
            ) {
                val chequeId = "chk_${System.currentTimeMillis()}"
                val currentUser = FirebaseAuth.getInstance().currentUser ?: return@setOnClickListener
                val senderId = currentUser.uid

                val chequeData = hashMapOf(
                    "payerName" to (currentUser.displayName ?: "Unknown"),
                    "chequeId" to chequeId,
                    "beneficiaryName" to name,
                    "senderName" to (currentUser.displayName ?: "Wahab Hamid"),
                    "chequeAmount" to amount,
                    "bankName" to bank,
                    "beneficiaryEmail" to email,
                    "senderId" to senderId,
                    "validTill" to validTill,
                    "chequeType" to chequeType,
                    "status" to "created",
                    "isCancelled" to false,
                    "timestamp" to System.currentTimeMillis()
                )

                Log.d("CreateCheque", "Saving: $chequeData")
                Toast.makeText(requireContext(), "Saving cheque...", Toast.LENGTH_SHORT).show()

                // Use `add()` to avoid overwriting by email
                FirebaseFirestore.getInstance().collection("cheques")
                    .add(chequeData)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Cheque saved", Toast.LENGTH_SHORT).show()
                        BlockchainHelper.issueCheque(chequeId, name, bank, chequeType) { success ->
                            requireActivity().runOnUiThread {
                                val msg = if (success) "Issued on blockchain" else "Blockchain failed 1"
                                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Firestore error", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
