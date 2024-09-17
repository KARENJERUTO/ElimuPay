import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emt.elimupay.R
import com.emt.elimupay.models.FeeEntity

class FeeEntityAdapter(private val feeEntities: List<FeeEntity>) :
    RecyclerView.Adapter<FeeEntityAdapter.FeeEntityViewHolder>() {

    fun getFeeEntities(): List<FeeEntity> {
        return feeEntities
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeeEntityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fee_entity, parent, false)
        return FeeEntityViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeeEntityViewHolder, position: Int) {
        val feeEntity = feeEntities[position]
        holder.bind(feeEntity)
    }

    override fun getItemCount(): Int {
        return feeEntities.size
    }

    inner class FeeEntityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textStudentName: TextView = itemView.findViewById(R.id.textStudentName)
        private val textStudentID: TextView = itemView.findViewById(R.id.textStudentID)
        private val textDescription: TextView = itemView.findViewById(R.id.textDescription)
        private val textDebit: TextView = itemView.findViewById(R.id.textDebit)
        private val textCredit: TextView = itemView.findViewById(R.id.textCredit)
        private val textBalance: TextView = itemView.findViewById(R.id.textBalance)
        private val textTransactionDate: TextView = itemView.findViewById(R.id.textTransactionDate)

        fun bind(feeEntity: FeeEntity) {
            textStudentName.text = "${feeEntity.firstName} ${feeEntity.middleName}"
            textStudentID.text = feeEntity.student_id.toString()
            textDescription.text = feeEntity.description
            textDebit.text = feeEntity.debit.toString()
            textCredit.text = feeEntity.credit.toString()
            textBalance.text = feeEntity.balance.toString()
            textTransactionDate.text = feeEntity.transaction_date
        }
    }
}
