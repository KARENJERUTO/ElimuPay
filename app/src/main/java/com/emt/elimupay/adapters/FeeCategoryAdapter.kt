package com.emt.elimupay.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emt.elimupay.databinding.ItemFeeCategoryBinding
import com.emt.elimupay.models.FeeCategory

class FeeCategoryAdapter(private var feeCategories: List<FeeCategory>) :
    RecyclerView.Adapter<FeeCategoryAdapter.FeeCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeeCategoryViewHolder {
        val binding = ItemFeeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeeCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeeCategoryViewHolder, position: Int) {
        holder.bind(feeCategories[position])
    }

    override fun getItemCount(): Int = feeCategories.size

    fun updateData(newFeeCategories: List<FeeCategory>) {
        feeCategories = newFeeCategories
        notifyDataSetChanged()
    }

    class FeeCategoryViewHolder(private val binding: ItemFeeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(feeCategory: FeeCategory) {
            binding.categoryId.text = feeCategory.categoryId.toString()
            binding.categoryName.text = feeCategory.categoryName
            binding.amount.text = feeCategory.amount.toString()
        }
    }
}
