package com.example.dicodingsubmissionsatucharles.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.dicodingsubmissionsatucharles.R
import com.example.dicodingsubmissionsatucharles.databinding.ActivityLoginBinding
import com.example.dicodingsubmissionsatucharles.view.main.MainActivity
import com.example.dicodingsubmissionsatucharles.viewmodel.LoginViewModel
import com.example.dicodingsubmissionsatucharles.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
        observeLoading()
        loginMessage()
        loginResponse()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        with(binding) {
            btnLogin.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()
                when {
                    email.isEmpty() -> {
                        edLoginEmail.error = getString(R.string.email_validation)
                    }

                    password.isEmpty() -> {
                        edLoginPassword.error = getString(R.string.password_validation)
                    }

                    else -> {
                        login(email, password)
                    }
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        loginViewModel.login(email, password)
    }

    private fun loginResponse() {
        loginViewModel.loginResponse.observe(this) { response ->
            if (response.error) {
                showError(response.message)
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.awesome)
                    setMessage(getString(R.string.is_logged_in))
                    setPositiveButton(getString(R.string.next)) { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
    }

    private fun observeLoading() {
        loginViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun loginMessage() {
        loginViewModel.loginMessage.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                showError(error)
            }
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun playAnimation() {
        with(binding) {

            ObjectAnimator.ofFloat(ivLoginImage, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

            val title = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, 1f).setDuration(100)
            val message =
                ObjectAnimator.ofFloat(tvSubtitle, View.ALPHA, 1f).setDuration(100)
            val emailTextView =
                ObjectAnimator.ofFloat(tvLabelEmail, View.ALPHA, 1f).setDuration(100)
            val emailEditTextLayout =
                ObjectAnimator.ofFloat(tilLoginEmail, View.ALPHA, 1f).setDuration(100)
            val passwordTextView =
                ObjectAnimator.ofFloat(tvLabelPassword, View.ALPHA, 1f).setDuration(100)
            val passwordEditTextLayout =
                ObjectAnimator.ofFloat(tilLoginPassword, View.ALPHA, 1f).setDuration(100)
            val login = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(100)

            AnimatorSet().apply {
                playSequentially(
                    title,
                    message,
                    emailTextView,
                    emailEditTextLayout,
                    passwordTextView,
                    passwordEditTextLayout,
                    login
                )
                startDelay = 100
            }.start()
        }
    }
}