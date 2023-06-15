package com.bangkit.freshfuel.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.freshfuel.R
import com.bangkit.freshfuel.model.response.ProgressItem
import com.bangkit.freshfuel.view.detail.DetailActivity
import com.bangkit.freshfuel.view.rating.RatingActivity

class ProgressAdapter(private val listProgress: List<ProgressItem>) :
    RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>() {

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuName: TextView = view.findViewById(R.id.menuName)
        val menuCalories: TextView = view.findViewById(R.id.menuCalories)
        val menuImage: ImageView = view.findViewById(R.id.menuImage)
        val menuChecked: ImageView = view.findViewById(R.id.menuCheck)
        val menuRateButton: Button = view.findViewById(R.id.rateButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProgressViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_card, parent, false)
        )

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        val progress = listProgress[position]
        val calories = "calories: ${progress.calories.toString()}"
        holder.apply {
            menuName.text = progress.recipe
            menuCalories.text = calories
            if (progress.status == 1) {
                menuChecked.visibility = View.VISIBLE
                menuRateButton.visibility = View.INVISIBLE
            } else {
                menuRateButton.visibility = View.VISIBLE
                menuChecked.visibility = View.INVISIBLE
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

            holder.menuRateButton.setOnClickListener {
                val intent = Intent(holder.itemView.context, RatingActivity::class.java)
                intent.putExtra(DetailActivity.RECIPE_NAME, progress.recipe)
                holder.itemView.context.startActivity(intent)
            }

            holder.menuImage.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.RECIPE_NAME, progress.recipe)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return listProgress.size
    }
}