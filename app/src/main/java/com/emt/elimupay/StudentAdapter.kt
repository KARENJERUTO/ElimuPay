import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.emt.elimupay.R
import com.emt.elimupay.Student

class StudentAdapter(private var students: List<Student>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFirstName: TextView = itemView.findViewById(R.id.textViewFirstName)
        val textViewLastName: TextView = itemView.findViewById(R.id.textViewLastName)
        val textViewBalance: TextView = itemView.findViewById(R.id.textViewBalance)

        init {
            // Attach tooltip to balance TextView
            ViewCompat.setTooltipText(textViewBalance, "Click to pay balance")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = students[position]
        holder.textViewFirstName.text = "First Name: ${currentStudent.firstName}"
        holder.textViewLastName.text = "Last Name: ${currentStudent.lastName}"
        holder.textViewBalance.text = "Balance: ${currentStudent.balance}"

        // Set OnClickListener to the balance TextView
        holder.textViewBalance.setOnClickListener {
            // Show payment dialog when balance is clicked
            showPaymentDialog(holder.itemView.context, currentStudent)
        }
    }

    override fun getItemCount() = students.size

    // Method to update dataset
    fun updateData(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }

    private fun showPaymentDialog(context: Context, student: Student) {
        // Create and show payment dialog
        AlertDialog.Builder(context)
            .setTitle("Make Payment")
            .setMessage("Pay ${student.balance} for ${student.firstName} ${student.lastName}?")
            .setPositiveButton("Pay") { dialog, _ ->
                // Implement payment logic here
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
