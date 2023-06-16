package com.bangkit.freshfuel.view.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.freshfuel.R
import com.bangkit.freshfuel.data.AllergiesAdapter
import com.bangkit.freshfuel.data.AllergyData
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.databinding.ActivityRegisterBinding
import com.bangkit.freshfuel.model.AllergyItem
import com.bangkit.freshfuel.model.request.RegisterRequest
import com.bangkit.freshfuel.utils.UserViewModelFactory
import com.bangkit.freshfuel.view.login.LoginActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }
    private lateinit var allergiesRecyclerView: RecyclerView
    private lateinit var allergiesAdapter: AllergiesAdapter
    private val allergiesList: MutableList<AllergyItem> = mutableListOf()

    private val selectedAllergies: List<String>
        get() = allergiesList.filter { it.isSelected }.map { it.name }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupActionView()
        setupButtonEnabled()
        setupFormAction()
    }

    private fun setupFormAction() {
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setupButtonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setupButtonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupButtonEnabled() {
        val name = binding.nameEditText.text
        val nameValid = name.toString().isNotEmpty()

        val email = binding.emailEditText.text
        val emailValid =
            email.toString()
                .isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email.toString())
                .matches()

        val password = binding.passwordEditText.text
        val passwordValid = password.toString().isNotEmpty() && password.toString().length >= 6

        binding.registerButton.isEnabled = nameValid && emailValid && passwordValid
    }

    private fun setupActionView() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val selectedAllergies = selectedAllergies
            val allergies = if (selectedAllergies.isNotEmpty()) {
                selectedAllergies.joinToString(", ")
            } else {
                ""
            }

            val registerRequest = RegisterRequest(
                name = name,
                email = email,
                password = password,
                allergies = allergies
            )

            lifecycleScope.launch {
                viewModel.register(registerRequest)
                    .observe(this@RegisterActivity) { result ->
                        when (result) {
                            is Result.Loading -> {
                                setLoading(true)
                            }

                            is Result.Success -> {
                                setLoading(false)
                                setRegistered(true)
                            }

                            else -> {
                                setLoading(false)
                                setRegistered(false)
                            }
                        }
                    }
            }

        }
    }


    private fun setRegistered(isRegistered: Boolean) {
        if (isRegistered) {
            AlertDialog.Builder(this).apply {
                setTitle("Register")
                setMessage("Register succeed!")
                setPositiveButton("Proceed") { _, _ ->
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Register")
                setMessage("Register failed!")
                setPositiveButton("Return") { _, _ ->
                    clearForm()
                }
                create()
                show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearForm() {
        binding.nameEditText.setText("")
        binding.nameEditTextLayout.error = null
        binding.emailEditText.setText("")
        binding.emailEditTextLayout.error = null
        binding.passwordEditText.setText("")
        binding.passwordEditTextLayout.error = null
        allergiesList.forEach { it.isSelected = false }
        allergiesAdapter.notifyDataSetChanged()
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.registerButton.isEnabled = false
            binding.registerButton.text = getString(R.string.loading)
        } else {
            binding.progressBar.visibility = View.GONE
            binding.registerButton.isEnabled = true
            binding.registerButton.text = getString(R.string.action_register_short)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecyclerView() {
        allergiesRecyclerView = binding.allergiesRecyclerView
        allergiesAdapter = AllergiesAdapter(allergiesList) { position, isChecked ->
            allergiesList[position].isSelected = isChecked
        }
        allergiesRecyclerView.adapter = allergiesAdapter
        allergiesRecyclerView.layoutManager = LinearLayoutManager(this)

        allergiesList.addAll(
            AllergyData.getAllergy()
        )
        allergiesAdapter.notifyDataSetChanged()
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