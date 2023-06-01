package com.bangkit.freshfuel.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.freshfuel.R
import com.bangkit.freshfuel.model.AllergyItem

class AllergiesAdapter(
    private val allergies: List<AllergyItem>,
    private val onCheckedChangeListener: (position: Int, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<AllergiesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(allergyItem: AllergyItem) {
            checkbox.text = allergyItem.name
            checkbox.isChecked = allergyItem.isSelected

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChangeListener(adapterPosition, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_allergy, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val allergyItem = allergies[position]
        holder.bind(allergyItem)
    }

    override fun getItemCount(): Int {
        return allergies.size
    }
}
