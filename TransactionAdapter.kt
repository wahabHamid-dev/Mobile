package com.wahab.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val cheques: List<Cheque>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.nameText)
        val amountText: TextView = view.findViewById(R.id.accountText)
        val typeText: TextView = view.findViewById(R.id.typeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cheque = cheques[position]
        holder.nameText.text = "Beneficiary: ${cheque.beneficiaryName}"
        holder.amountText.text = "Amount: ${cheque.amount}"
        holder.typeText.text = "Status: ${cheque.status}"
    }

    override fun getItemCount(): Int = cheques.size
}
