package com.bangkit.freshfuel.view.rating

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.freshfuel.R
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.data.preference.RecipePreference
import com.bangkit.freshfuel.databinding.ActivityRatingBinding
import com.bangkit.freshfuel.model.request.PredictRequest
import com.bangkit.freshfuel.model.request.UpdateRequest
import com.bangkit.freshfuel.model.response.RecipeData
import com.bangkit.freshfuel.utils.RecipeViewModelFactory
import com.bangkit.freshfuel.utils.getCurrentDateFormatted
import kotlinx.coroutines.launch

class RatingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatingBinding
    private val viewModel: RatingViewModel by viewModels {
        RecipeViewModelFactory.getInstance(this)
    }
    private lateinit var radioGroup: RadioGroup
    private var rating: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeName = intent.getStringExtra(RECIPE_NAME) ?: ""

        setupView()
        setupUIData(recipeName)
        ratingListener(recipeName)
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.submitRatingButton.isEnabled = false
        } else {
            binding.progressBar.visibility = android.view.View.GONE
            binding.submitRatingButton.isEnabled = true
        }
    }

    private fun setupUIData(recipeName: String) {
        lifecycleScope.launch {
            viewModel.getRecipeDetail(recipeName)
                .observe(this@RatingActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            setLoading(true)
                        }

                        is Result.Success -> {
                            setLoading(false)
                            val recipe = result.data
                            setupUI(recipe)
                        }

                        else -> {
                            setLoading(false)
                            Toast.makeText(
                                this@RatingActivity,
                                "something wrong occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    private fun setupUI(data: RecipeData) {
        binding.apply {
            calories.text = data.information?.calories.toString().plus(" calories")
            recipeName.text = data.information?.recipeName
            ratingNumber.text = data.information?.rating.toString()
        }
    }

    private fun setupForm() {
        val preference = RecipePreference.getInstance(this)
        val recipes = preference.getRecipes()
        radioGroup = binding.radioGroup
        radioGroup.removeAllViews()
        createRadioButtons(recipes)

        val radioGroup = binding.radioGroup
        val submitButton = binding.submitRatingButton

        submitButton.setOnClickListener {
            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(checkedRadioButtonId)

            if (radioButton != null) {
                val recipe = radioButton.text.toString()

                submitAction(recipe)
            }
        }
    }

    private fun submitAction(recipe: String) {
        val email = viewModel.userData.dataUser?.email!!
        val date = getCurrentDateFormatted()
        lifecycleScope.launch {
            viewModel.getCurrentProgress(email, date)
                .observe(this@RatingActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            setLoading(true)
                            disableButton(true)
                        }

                        is Result.Success -> {
                            val request: UpdateRequest = when (result.data.size) {
                                1 -> {
                                    UpdateRequest(
                                        date,
                                        recipe,
                                        null,
                                        rating
                                    )
                                }

                                2 -> {
                                    UpdateRequest(
                                        date,
                                        null,
                                        recipe,
                                        rating
                                    )
                                }

                                else -> {
                                    UpdateRequest(
                                        date,
                                        null,
                                        null,
                                        rating
                                    )
                                }
                            }

                            updateProgress(request)
                        }

                        else -> {
                            Toast.makeText(
                                this@RatingActivity,
                                "something wrong occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    private fun updateProgress(request: UpdateRequest) {
        lifecycleScope.launch {
            viewModel.updateProgress(request).observe(this@RatingActivity) {
                when (it) {
                    is Result.Loading -> {
                        setLoading(true)
                        disableButton(true)
                    }

                    is Result.Success -> {
                        setLoading(false)
                        disableButton(false)
                        Toast.makeText(
                            this@RatingActivity,
                            "success",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    else -> {
                        setLoading(false)
                        disableButton(false)
                        Toast.makeText(
                            this@RatingActivity,
                            "something wrong occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun createRadioButtons(recipes: List<String>) {
        val inflater = LayoutInflater.from(this)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        for (option in recipes) {
            val radioButton =
                inflater.inflate(R.layout.item_new_menu, radioGroup, false) as RadioButton
            radioButton.text = option
            radioGroup.addView(radioButton)
        }
    }

    private fun ratingListener(recipeName: String) {
        binding.ratingBar.setOnRatingBarChangeListener { _, rate, _ ->
            rating = rate.toInt()
            if (rating == 0) {
                disableButton(true)
            } else {
                val request =
                    PredictRequest(recipeName, viewModel.userData.dataUser?.allergies!!, rating)
                lifecycleScope.launch {
                    viewModel.postRating(request).observe(this@RatingActivity) { result ->
                        when (result) {
                            is Result.Loading -> {
                                setLoading(true)
                                disableButton(true)
                            }

                            is Result.Success -> {
                                saveNewRecipe(result.data)
                                setupForm()
                                setLoading(false)
                                disableButton(false)
                            }

                            else -> {
                                Toast.makeText(
                                    this@RatingActivity,
                                    "something wrong occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                                setLoading(false)
                                disableButton(false)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun disableButton(isDisabled: Boolean) {
        binding.submitRatingButton.isEnabled = !isDisabled
        binding.ratingBar.isClickable = isDisabled
    }

    private fun saveNewRecipe(data: List<String>) {
        val preference = RecipePreference(this)
        preference.setRecipe(data)
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