package com.example.dicodingsubmissionsatucharles.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dicodingsubmissionsatucharles.databinding.ActivityWelcomeBinding
import com.example.dicodingsubmissionsatucharles.view.login.LoginActivity
import com.example.dicodingsubmissionsatucharles.view.signup.SignUpActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
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
            btnLogin.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
            }
            btnSignUp.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, SignUpActivity::class.java))
            }
        }
    }

    private fun playAnimation() {
        with(binding) {
            ObjectAnimator.ofFloat(ivWelcomeImage, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

            val login = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(100)
            val signup = ObjectAnimator.ofFloat(btnSignUp, View.ALPHA, 1f).setDuration(100)
            val title = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, 1f).setDuration(100)
            val desc = ObjectAnimator.ofFloat(tvDescription, View.ALPHA, 1f).setDuration(100)

            val together = AnimatorSet().apply {
                playTogether(login, signup)
            }

            AnimatorSet().apply {
                playSequentially(title, desc, together)
                start()
            }
        }
    }
}