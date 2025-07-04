import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wahab.myapp.R

class ReceivedAdapter(
    private val cheques: List<ReceivedCheque>
) : RecyclerView.Adapter<ReceivedAdapter.ReceivedViewHolder>() {

    inner class ReceivedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textPayer: TextView = view.findViewById(R.id.textPayer)
        val textAmount: TextView = view.findViewById(R.id.textAmount)
        val textBank: TextView = view.findViewById(R.id.textBank)
        val textStatus: TextView = view.findViewById(R.id.textStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_received_cheque, parent, false)
        return ReceivedViewHolder(view)
    }


    override fun onBindViewHolder(holder: ReceivedViewHolder, position: Int) {
        val cheque = cheques[position]
        holder.textPayer.text = "From: ${cheque.senderName}" // Corrected this line
        holder.textAmount.text = "Amount: ${cheque.chequeAmount}"
        holder.textBank.text = "Bank: ${cheque.bankName}"
        holder.textStatus.text = if (cheque.isCancelled) "Cancelled" else "Valid"
    }

    override fun getItemCount() = cheques.size
}
