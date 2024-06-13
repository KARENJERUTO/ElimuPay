import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emt.elimupay.R
import com.emt.elimupay.Student
import com.emt.elimupay.paymentmethods.PaymentMethods
import com.google.android.material.button.MaterialButton

class StudentAdapter(private var students: List<Student>) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstNameTextView: TextView = itemView.findViewById(R.id.textViewFirstName)
        val lastNameTextView: TextView = itemView.findViewById(R.id.textViewLastName)
        val balanceTextView: TextView = itemView.findViewById(R.id.textViewBalance)
        val payFeeButton: MaterialButton = itemView.findViewById(R.id.buttonPayFee) // Assuming buttonPayFee exists in item_student.xml

        init {
            payFeeButton.setOnClickListener {
                val intent = Intent(itemView.context, PaymentMethods::class.java)
                itemView.context.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.firstNameTextView.text = "First Name: ${student.firstName}"
        holder.lastNameTextView.text = "Last Name: ${student.lastName}"
        holder.balanceTextView.text = "Balance: ${student.balance}"
        Log.d("StudentAdapter", "Binding student: ${student.firstName} ${student.lastName}")
    }

    override fun getItemCount(): Int {
        return students.size
    }

    fun updateData(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
        Log.d("StudentAdapter", "Data updated. Count: ${students.size}")
    }
}
