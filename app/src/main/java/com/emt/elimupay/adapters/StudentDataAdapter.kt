package com.emt.elimupay.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emt.elimupay.R
import com.emt.elimupay.models.Data

class StudentDataAdapter(private var students: List<Data>) : RecyclerView.Adapter<StudentDataAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student_data, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val studentData = students[position]
        holder.bind(studentData)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    fun updateData(newStudents: List<Data>) {
        this.students = newStudents
        notifyDataSetChanged()  // Update adapter data
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFirstName: TextView = itemView.findViewById(R.id.textFirstName)
        private val tvLastName: TextView = itemView.findViewById(R.id.textLastName)
        private val tvAdmNumber: TextView = itemView.findViewById(R.id.textAdmNumber)
        private val tvGrade: TextView = itemView.findViewById(R.id.textGrade)
        private val tvStream: TextView = itemView.findViewById(R.id.textStream)
        private val tvUPINumber: TextView = itemView.findViewById(R.id.textUpiNumber)
        private val tvHealthStatus: TextView = itemView.findViewById(R.id.textHealthStatus)
        private val tvGender: TextView = itemView.findViewById(R.id.textGender) // Ensure this TextView is defined in the layout

        fun bind(student: Data) {
            tvFirstName.text = student.firstName
            tvLastName.text = student.lastName
            tvAdmNumber.text = "Adm Number: ${student.admNumber}"
            tvGrade.text = "Grade: ${student.grade}"
            tvStream.text = "Stream: ${student.stream}"
            tvUPINumber.text = "UPI Number: ${student.upiNumber}"
            tvHealthStatus.text = "Health Status: ${student.healthStatus ?: "N/A"}"  // Use null-safe operator
            tvGender.text = "Gender: ${student.studentGender}"  // Set gender
        }
    }
}
