package com.bangkit.freshfuel.view.recommender

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.freshfuel.R
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.databinding.ActivityRecommendBinding
import com.bangkit.freshfuel.model.request.ProgressRequest
import com.bangkit.freshfuel.utils.RecipeViewModelFactory
import com.bangkit.freshfuel.utils.getCurrentDateFormatted
import kotlinx.coroutines.launch

class RecommendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendBinding
    private lateinit var radioGroup: RadioGroup
    private val viewModel: RecommenderViewModel by viewModels {
        RecipeViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipes = intent.getStringArrayListExtra(EXTRA_RECOMMENDATIONS) as List<String>
        radioGroup = binding.radioGroup

        createRadioButtons(recipes)
        setupAction()
    }

    private fun setupAction() {
        val radioGroup = binding.radioGroup
        val submitButton = binding.submitButton

        submitButton.setOnClickListener {
            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(checkedRadioButtonId)

            if (radioButton != null) {
                val currentDate = getCurrentDateFormatted()
                val recipe = radioButton.text.toString()

                setupViewModel(currentDate, recipe)
            }
        }
    }

    private fun setupViewModel(currentDate: String, recipe: String) {
        lifecycleScope.launch {
            viewModel.postProgress(ProgressRequest(currentDate, recipe))
                .observe(this@RecommendActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            setLoading(true)
                        }

                        is Result.Success -> {
                            setLoading(false)
                            finish()
                        }

                        is Result.Error -> {
                            setLoading(false)
                            Toast.makeText(
                                this@RecommendActivity,
                                "something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            submitButton.isEnabled = !isLoading
            if (isLoading) {
                submitButton.text = getString(R.string.loading)
                progressBar.visibility = android.view.View.VISIBLE
            } else {
                submitButton.text = getString(R.string.select_menu)
                progressBar.visibility = android.view.View.GONE
            }
        }
    }

    private fun createRadioButtons(recipes: List<String>) {
        val inflater = LayoutInflater.from(this)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        for (option in recipes) {
            val radioButton =
                inflater.inflate(R.layout.new_menu_item, radioGroup, false) as RadioButton
            radioButton.text = option
            radioGroup.addView(radioButton)
        }
    }

    companion object {
        const val EXTRA_RECOMMENDATIONS = "extra_recommendations"
    }
}