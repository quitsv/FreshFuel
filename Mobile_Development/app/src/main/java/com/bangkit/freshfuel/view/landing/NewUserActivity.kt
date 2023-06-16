package com.bangkit.freshfuel.view.landing

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
import com.bangkit.freshfuel.databinding.ActivityNewUserBinding
import com.bangkit.freshfuel.model.request.ProgressRequest
import com.bangkit.freshfuel.utils.RecipeViewModelFactory
import com.bangkit.freshfuel.utils.getCurrentDateFormatted
import kotlinx.coroutines.launch

class NewUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewUserBinding
    private lateinit var radioGroup: RadioGroup
    private val viewModel: NewUserViewModel by viewModels {
        RecipeViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupForm()

    }

    private fun setupForm() {
        val preference = RecipePreference.getInstance(this)
        val recipes = preference.getRecipes()
        radioGroup = binding.radioGroup
        radioGroup.removeAllViews()
        createRadioButtons(recipes)

        val radioGroup = binding.radioGroup
        val submitButton = binding.submitButton

        submitButton.setOnClickListener {
            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(checkedRadioButtonId)

            if (radioButton != null) {
                val recipe = radioButton.text.toString()

                submitAction(recipe)
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

    private fun submitAction(recipe: String) {
        lifecycleScope.launch {
            viewModel.postProgress(ProgressRequest(getCurrentDateFormatted(), recipe))
                .observe(this@NewUserActivity) { result ->
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
                                this@NewUserActivity,
                                "something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.submitButton.isEnabled = false
        } else {
            binding.progressBar.visibility = android.view.View.GONE
            binding.submitButton.isEnabled = true
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
}