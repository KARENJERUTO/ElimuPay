package com.emt.elimupay.adapters

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
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        private val amountTextView: TextView = itemView.findViewById(R.id.textViewAmount)
        private val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)

        fun bind(feeEntity: FeeEntity) {
            descriptionTextView.text = feeEntity.description
            amountTextView.text = feeEntity.amount
            dateTextView.text = feeEntity.date
        }
    }
}
