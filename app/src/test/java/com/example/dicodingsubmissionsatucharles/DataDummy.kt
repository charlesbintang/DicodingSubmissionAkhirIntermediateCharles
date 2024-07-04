package com.example.dicodingsubmissionsatucharles

import com.example.dicodingsubmissionsatucharles.data.retrofit.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                createdAt = "$i",
                name = "Story $i",
                description = "This is story number $i",
                lon = 123.456 + i * 0.001,
                id = i.toString(),
                lat = 45.678 - i * 0.001
            )
            items.add(story)
        }
        return items
    }
}