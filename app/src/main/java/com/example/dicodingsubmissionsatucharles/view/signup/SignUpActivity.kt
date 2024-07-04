package com.example.dicodingsubmissionsatucharles.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.dicodingsubmissionsatucharles.R
import com.example.dicodingsubmissionsatucharles.databinding.ActivitySignUpBinding
import com.example.dicodingsubmissionsatucharles.view.login.LoginActivity
import com.example.dicodingsubmissionsatucharles.viewmodel.SignUpViewModel
import com.example.dicodingsubmissionsatucharles.viewmodel.ViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val signUpViewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
        setupAction()
        playAnimation()
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
        supportActionBar?.hide()
    }

    private fun setupAction() {

        with(binding) {
            btnSignUp.setOnClickListener {
                val username = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()

                when {
                    username.isEmpty() -> {
                        edRegisterName.error = getString(R.string.name_validation)
                    }

                    email.isEmpty() -> {
                        edRegisterEmail.error = getString(R.string.email_validation)
                    }

                    password.isEmpty() -> {
                        edRegisterPassword.error = getString(R.string.password_validation)
                    }

                    else -> {
                        observeLoading()
                        signUp(username, email, password)
                        signUpResponse(email)
                        setupView()
                    }
                }
            }
        }
    }

    private fun signUpResponse(email: String) {
        signUpViewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.awesome)
                    val message = getString(R.string.account_created_message, email)
                    setMessage(message)
                    setPositiveButton(getString(R.string.next)) { _, _ ->
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            } else {
                signUpMessage()
            }
        }
    }

    private fun signUpMessage() {
        signUpViewModel.signUpMessage.observe(this) { message ->
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.failed))
                setMessage(message)
                create()
                show()
            }
            val intent =
                Intent(this, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun signUp(username: String, email: String, password: String) {
        signUpViewModel.signUp(username, email, password)
    }

    private fun observeLoading() {
        signUpViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun playAnimation() {
        with(binding) {

            ObjectAnimator.ofFloat(ivSignUpImage, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

            val title = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, 1f).setDuration(100)
            val nameTextView =
                ObjectAnimator.ofFloat(tvLabelName, View.ALPHA, 1f).setDuration(100)
            val nameEditTextLayout =
                ObjectAnimator.ofFloat(tilSignUpName, View.ALPHA, 1f).setDuration(100)
            val emailTextView =
                ObjectAnimator.ofFloat(tvLabelEmail, View.ALPHA, 1f).setDuration(100)
            val emailEditTextLayout =
                ObjectAnimator.ofFloat(tilSignUpEmail, View.ALPHA, 1f).setDuration(100)
            val passwordTextView =
                ObjectAnimator.ofFloat(tvLabelPassword, View.ALPHA, 1f).setDuration(100)
            val passwordEditTextLayout =
                ObjectAnimator.ofFloat(tilSignUpPassword, View.ALPHA, 1f).setDuration(100)
            val signup = ObjectAnimator.ofFloat(btnSignUp, View.ALPHA, 1f).setDuration(100)


            AnimatorSet().apply {
                playSequentially(
                    title,
                    nameTextView,
                    nameEditTextLayout,
                    emailTextView,
                    emailEditTextLayout,
                    passwordTextView,
                    passwordEditTextLayout,
                    signup
                )
                startDelay = 100
            }.start()
        }
    }
}