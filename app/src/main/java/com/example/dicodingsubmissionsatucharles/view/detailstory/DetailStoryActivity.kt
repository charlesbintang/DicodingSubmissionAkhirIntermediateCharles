package com.example.dicodingsubmissionsatucharles.view.detailstory

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.dicodingsubmissionsatucharles.R
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.ListStoryItem
import com.example.dicodingsubmissionsatucharles.databinding.ActivityDetailStoryBinding
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataListStoryItem = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, ListStoryItem::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_DATA)
        }

        if (dataListStoryItem != null) {
            with(binding) {
                Glide.with(this@DetailStoryActivity).load(dataListStoryItem.photoUrl)
                    .into(ivDetailPhoto)
                tvDetailName.text = dataListStoryItem.name
                tvDetailDescription.text = dataListStoryItem.description

                val date3339 = dataListStoryItem.createdAt.toString()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val instant = Instant.parse(date3339)
                    val date = Date.from(instant)
                    val atTime = getString(R.string.at_time)
                    tvDetailDate.text = SimpleDateFormat(
                        "dd MMMM yyyy '$atTime' HH:mm",
                        Locale.getDefault()
                    ).format(date)
                } else {
                    val inputFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val date = inputFormat.parse(date3339)
                    tvDetailDate.text = date?.let {
                        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(
                            it
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_post"
    }
}