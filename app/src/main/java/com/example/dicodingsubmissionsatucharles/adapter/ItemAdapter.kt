package com.example.dicodingsubmissionsatucharles.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.ListStoryItem
import com.example.dicodingsubmissionsatucharles.databinding.ItemUserStoryBinding
import com.example.dicodingsubmissionsatucharles.utils.loadImage
import com.example.dicodingsubmissionsatucharles.view.detailstory.DetailStoryActivity

class ItemAdapter : PagingDataAdapter<ListStoryItem, ItemAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ItemUserStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvItemName.text = story.name
            story.photoUrl?.let { binding.ivItemPhoto.loadImage(it) }
            binding.tvItemDescription.text = story.description

            itemView.setOnClickListener {

                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_DATA, story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, PROFILE),
                        Pair(binding.tvItemName, NAME),
                        Pair(binding.tvItemDescription, DESCRIPTION),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemUserStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataStory = getItem(position)
        if (dataStory != null) {
            holder.bind(dataStory)
        }
    }

    companion object {
        const val PROFILE = "profile"
        const val NAME = "name"
        const val DESCRIPTION = "description"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}