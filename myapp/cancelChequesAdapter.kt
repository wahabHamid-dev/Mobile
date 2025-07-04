package com.wahab.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CancelChequeAdapter(
    private val cheques: List<Cheque>,
    private val onCancelClick: (String) -> Unit
) : RecyclerView.Adapter<CancelChequeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val payee = view.findViewById<TextView>(R.id.textPayeeName)
        val beneficiary = view.findViewById<TextView>(R.id.textBeneficiaryName)
        val amount = view.findViewById<TextView>(R.id.textAmount)
        val status = view.findViewById<TextView>(R.id.textStatus)
        val cancelButton = view.findViewById<Button>(R.id.buttonCancelCheque)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cheque, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cheque = cheques[position]
        holder.payee.text = "Payee: ${cheque.payeeName}"
        holder.beneficiary.text = "Beneficiary: ${cheque.beneficiaryName}"
        holder.amount.text = "Amount: ${cheque.amount}"
        holder.status.text = "Status: ${cheque.status}"

        // Only show cancel if status is created
        holder.cancelButton.visibility = if (cheque.status == "created") View.VISIBLE else View.GONE
        holder.cancelButton.setOnClickListener {
            onCancelClick(cheque.id)
        }
    }

    override fun getItemCount(): Int = cheques.size
}
