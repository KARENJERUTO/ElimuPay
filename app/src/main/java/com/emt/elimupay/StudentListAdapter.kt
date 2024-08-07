//package com.emt.elimupay
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.emt.elimupay.databinding.ItemStudentlistBinding
//
//class StudentListAdapter : ListAdapter<StudentListResponse, StudentListAdapter.StudentViewHolder>(StudentDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
//        val binding = ItemStudentlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return StudentViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    class StudentViewHolder(private val binding: ItemStudentlistBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(student: StudentListResponse) {
//            binding.studentName.text = "Student Name: ${student.firstName} ${student.lastName}"
//            binding.studentGrade.text = "Grade: ${student.grade}"
//            binding.studentAdmNumber.text = "Admission Number: ${student.admNumber}"
//            binding.studentDateOfAdmission.text = "Date of Admission: ${student.dateOfAdmission}"
//        }
//    }
//
//    class StudentDiffCallback : DiffUtil.ItemCallback<StudentListResponse>() {
//        override fun areItemsTheSame(
//            oldItem: StudentListResponse,
//            newItem: StudentListResponse
//        ): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(
//            oldItem: StudentListResponse,
//            newItem: StudentListResponse
//        ): Boolean {
//            return oldItem == newItem
//        }
//    }
//}
