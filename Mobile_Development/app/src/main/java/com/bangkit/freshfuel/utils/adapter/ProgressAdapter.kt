package com.bangkit.freshfuel.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.freshfuel.R
import com.bangkit.freshfuel.model.response.ProgressItem

class ProgressAdapter(private val listProgress: List<ProgressItem>) :
    RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>() {

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuName: TextView = view.findViewById(R.id.menuName)
        val menuCalories: TextView = view.findViewById(R.id.menuCalories)
        val menuImage: ImageView = view.findViewById(R.id.menuImage)
        val menuChecked: ImageView = view.findViewById(R.id.menuCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProgressViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_card, parent, false)
        )

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        val progress = listProgress[position]
        val calories = "calories: ${progress.calories.toString()}"
        holder.apply {
            menuName.text = progress.recipe
            menuCalories.text = calories
            if (progress.status == 1) {
                menuChecked.visibility = View.VISIBLE
            } else {
                menuChecked.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if (menuImage.visibility == View.GONE) {
                    menuImage.visibility = View.VISIBLE
                    menuName.visibility = View.VISIBLE
                    menuCalories.visibility = View.VISIBLE
                } else {
                    menuImage.visibility = View.GONE
                    menuName.visibility = View.GONE
                    menuCalories.visibility = View.GONE
                }
            }

            holder.menuImage.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        return listProgress.size
    }
}