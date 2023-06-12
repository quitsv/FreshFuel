package com.bangkit.freshfuel.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.freshfuel.R
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.data.UserPreference
import com.bangkit.freshfuel.databinding.ActivityLoginBinding
import com.bangkit.freshfuel.model.request.LoginRequest
import com.bangkit.freshfuel.model.response.LoginResponse
import com.bangkit.freshfuel.utils.UserViewModelFactory
import com.bangkit.freshfuel.view.main.MainActivity
import com.bangkit.freshfuel.view.register.RegisterActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupActionView()
    }

    private fun setupActionView() {
        setupAction()
        binding.registerTextView.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Enter email"
                }

                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Enter password"
                }

                else -> {
                    lifecycleScope.launch {
                        viewModel.loginUser(LoginRequest(email, password))
                            .observe(this@LoginActivity) { result ->
                                when (result) {
                                    is Result.Loading -> {
                                        setupLoading(true)
                                    }

                                    is Result.Success -> {
                                        setupLoading(false)
                                        setUser(result.data)
                                        setLogin(true)
                                    }

                                    else -> {
                                        setupLoading(false)
                                        setLogin(false)
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    private fun setLogin(isLogin: Boolean) {
        if (isLogin) {
            AlertDialog.Builder(this).apply {
                setTitle("Login")
                setMessage("Login succeed!")
                setPositiveButton("Proceed") { _, _ ->
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
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
                setTitle("Login")
                setMessage("Login failed!")
                setPositiveButton("Return") { _, _ ->
                    clearForm()
                }
                create()
                show()
            }
        }
    }

    private fun clearForm() {
        binding.emailEditText.text?.clear()
        binding.passwordEditText.text?.clear()
        binding.emailEditTextLayout.error = null
        binding.passwordEditTextLayout.error = null
    }

    private fun setUser(data: LoginResponse) {
        val userPreference = UserPreference(this)
        userPreference.setUser(data)
    }

    private fun setupLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.loginButton.isEnabled = false
            binding.loginButton.text = getString(R.string.loading)
        } else {
            binding.progressBar.visibility = View.GONE
            binding.loginButton.isEnabled = true
            binding.loginButton.text = getString(R.string.login)
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