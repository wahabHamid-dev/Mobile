package com.wahab.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChequeAdapter(
    private val cheques: List<Cheque>,
    private val onCancelClick: ((String) -> Unit)? = null
) : RecyclerView.Adapter<ChequeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val payeeNameText: TextView = itemView.findViewById(R.id.textPayeeName)
        val beneficiaryNameText: TextView = itemView.findViewById(R.id.textBeneficiaryName)
        val amountText: TextView = itemView.findViewById(R.id.textAmount)
        val statusText: TextView = itemView.findViewById(R.id.textStatus)
        val cancelButton: Button = itemView.findViewById(R.id.buttonCancelCheque)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cheque, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = cheques.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cheque = cheques[position]

        holder.payeeNameText.text = "Payee: ${cheque.payeeName}"
        holder.beneficiaryNameText.text = "Beneficiary: ${cheque.beneficiaryName}"
        holder.amountText.text = "Amount: ${cheque.amount}"
        holder.statusText.text = "Status: ${cheque.status}"

        // Show cancel button only if callback is provided AND cheque is NOT cancelled
        if (onCancelClick != null && !cheque.isCancelled) {
            holder.cancelButton.visibility = View.VISIBLE
            holder.cancelButton.setOnClickListener {
                onCancelClick.invoke(cheque.id)
            }
        } else {
            holder.cancelButton.visibility = View.GONE
            holder.cancelButton.setOnClickListener(null)
        }
    }
}
