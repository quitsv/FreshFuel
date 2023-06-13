package com.bangkit.freshfuel.view.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.databinding.ActivityDetailBinding
import com.bangkit.freshfuel.model.response.Data
import com.bangkit.freshfuel.utils.RecipeViewModelFactory
import com.bangkit.freshfuel.utils.adapter.IngredientsAdapter
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        RecipeViewModelFactory.getInstance(this)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeName = intent.getStringExtra(RECIPE_NAME) ?: ""

        setupView()
        setupRecyclerView()
        setupViewModel(recipeName)
    }

    private fun setupRecyclerView() {
        recyclerView = binding.ingredients
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupViewModel(recipeName: String) {
        lifecycleScope.launch {
            viewModel.getRecipeDetail(recipeName).observe(this@DetailActivity) { result ->
                when (result) {
                    is Result.Loading -> {
                        setupLoading(true)
                    }

                    is Result.Success -> {
                        setupLoading(false)
                        setupUI(result.data)
                    }

                    else -> {
                        setupLoading(false)
                        Toast.makeText(
                            this@DetailActivity,
                            "Error getting data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setupLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun setupUI(data: Data) {
        adapter = IngredientsAdapter(data.ingredients as List<String>)
        recyclerView.adapter = adapter
        binding.apply {
            calories.text = data.information?.calories.toString().plus(" calories")
            recipeName.text = data.information?.recipeName
            ratingNumber.text = data.information?.rating.toString()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    companion object {
        const val RECIPE_NAME = "recipe_name"
    }
}