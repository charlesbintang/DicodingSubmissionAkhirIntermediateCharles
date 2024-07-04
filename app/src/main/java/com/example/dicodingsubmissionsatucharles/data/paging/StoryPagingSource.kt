package com.example.dicodingsubmissionsatucharles.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dicodingsubmissionsatucharles.data.pref.UserPreference
import com.example.dicodingsubmissionsatucharles.data.retrofit.api.ApiService
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.ListStoryItem
import kotlinx.coroutines.flow.first

class StoryPagingSource(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) :
    PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val token = userPreference.getSession().first().token
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStory("Bearer $token", position, params.loadSize)
            val storyItems = responseData.listStory
            LoadResult.Page(
                data = storyItems,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (storyItems.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}