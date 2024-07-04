package com.example.dicodingsubmissionsatucharles.view.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingsubmissionsatucharles.R
import com.example.dicodingsubmissionsatucharles.adapter.ItemAdapter
import com.example.dicodingsubmissionsatucharles.databinding.ActivityMainBinding
import com.example.dicodingsubmissionsatucharles.view.addnewstory.AddNewStoryActivity
import com.example.dicodingsubmissionsatucharles.view.login.LoginActivity
import com.example.dicodingsubmissionsatucharles.view.maps.MapsActivity
import com.example.dicodingsubmissionsatucharles.view.welcome.WelcomeActivity
import com.example.dicodingsubmissionsatucharles.viewmodel.MainViewModel
import com.example.dicodingsubmissionsatucharles.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeLoading()
        getSession()
        listStory()
        userMenu()
        mainMessage()

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddNewStoryActivity::class.java))
        }
    }

    private fun getSession() {
        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                binding.tvGreeting.text = getString(R.string.greeting, user.name)
            }
        }
    }

    private fun userMenu() {
        with(binding) {
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_language -> {
                        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                        true
                    }

                    R.id.menu_logout -> {
                        AlertDialog.Builder(this@MainActivity).apply {
                            setTitle(getString(R.string.logout))
                            setMessage(getString(R.string.logout_message))
                            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                                dialog.dismiss()
                                logout()

                                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                        true
                    }

                    R.id.menu_map -> {
                        val intent = Intent(this@MainActivity, MapsActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun logout() = mainViewModel.logout()

    private fun listStory() {
        val itemAdapter = ItemAdapter()
        mainViewModel.listStory.observe(this) { listStory ->
            if (listStory != null) {
                showLoading(false)
                showNoStoriesText(false)
                itemAdapter.submitData(lifecycle, listStory)
                with(binding) {
                    rvStories.adapter = itemAdapter
                    rvStories.layoutManager = LinearLayoutManager(this@MainActivity)
                }
            } else {
                showLoading(true)
                showNoStoriesText(true)
            }
        }
    }

    private fun observeLoading() {
        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun showNoStoriesText(isEmptyStories: Boolean) {
        if (isEmptyStories) {
            with(binding) {
                tvEmptyStories.visibility = View.VISIBLE
                rvStories.visibility = View.INVISIBLE
            }
        } else {
            with(binding) {
                tvEmptyStories.visibility = View.GONE
                rvStories.visibility = View.VISIBLE
            }
        }
    }

    private fun mainMessage() {
        mainViewModel.mainMessage.observe(this) { error ->
            if (!error.isNullOrEmpty() && error != "Stories fetched successfully") {
                showError(error)
            }
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}