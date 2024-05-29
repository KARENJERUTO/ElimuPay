import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emt.elimupay.R
import com.emt.elimupay.models.FeeEntity

class FeeEntityAdapter(private val feeEntities: List<FeeEntity>) :
    RecyclerView.Adapter<FeeEntityAdapter.FeeEntityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeeEntityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fee_entity, parent, false)
        return FeeEntityViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeeEntityViewHolder, position: Int) {
        val feeEntity = feeEntities[position]
        holder.bind(feeEntity)
    }

    override fun getItemCount(): Int = feeEntities.size

    class FeeEntityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val balanceTextView: TextView = itemView.findViewById(R.id.textViewBalance)
        private val creditTextView: TextView = itemView.findViewById(R.id.textViewCredit)
        private val debitTextView: TextView = itemView.findViewById(R.id.textViewDebit)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        private val idTextView: TextView = itemView.findViewById(R.id.textViewId)
        private val studentIdTextView: TextView = itemView.findViewById(R.id.textViewStudentId)
        private val transactionDateTextView: TextView = itemView.findViewById(R.id.textViewTransactionDate)

        fun bind(feeEntity: FeeEntity) {
            balanceTextView.text = "Balance: ${feeEntity.balance}"
            creditTextView.text = "Credit: ${feeEntity.credit}"
            debitTextView.text = "Debit: ${feeEntity.debit}"
            descriptionTextView.text = "Description: ${feeEntity.description}"
            idTextView.text = "ID: ${feeEntity.id}"
            studentIdTextView.text = "Student ID: ${feeEntity.student_id}"
            transactionDateTextView.text = "Transaction Date: ${feeEntity.transaction_date}"
        }
    }
}
