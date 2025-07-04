package com.wahab.myapp

import ReceivedCheque
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecievedAdapter(private val chequeList: List<ReceivedCheque>) :
    RecyclerView.Adapter<RecievedAdapter.ChequeViewHolder>() {

    class ChequeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val payerName: TextView = view.findViewById(R.id.textPayer)
        val amount: TextView = view.findViewById(R.id.textAmount)
        val bank: TextView = view.findViewById(R.id.tvBank)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChequeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_received_cheque, parent, false)
        return ChequeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChequeViewHolder, position: Int) {
        val cheque = chequeList[position]
        holder.payerName.text = "From: ${cheque.payerName}"
        holder.amount.text = "Amount: ${cheque.chequeAmount}"
        holder.bank.text = "Bank: ${cheque.bankName}"
    }

    override fun getItemCount(): Int = chequeList.size
}
