package com.example.dicodingsubmissionsatucharles.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.dicodingsubmissionsatucharles.data.paging.StoryPagingSource
import com.example.dicodingsubmissionsatucharles.data.pref.UserModel
import com.example.dicodingsubmissionsatucharles.data.pref.UserPreference
import com.example.dicodingsubmissionsatucharles.data.retrofit.api.ApiService
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.FileUploadResponse
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.ListStoryItem
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.LoginResponse
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.SignUpResponse
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference, private val apiService: ApiService
) {
    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun signUp(name: String, email: String, password: String): SignUpResponse {
        return apiService.register(name, email, password)
    }

    suspend fun saveSession(user: UserModel) = userPreference.saveSession(user)

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                StoryPagingSource(apiService, userPreference)
            }
        ).liveData
    }

    suspend fun logout() = userPreference.logout()

    suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): FileUploadResponse {
        return apiService.uploadStory(token, file, description)
    }

    suspend fun getStoriesWithLocation(token: String): StoryResponse {
        return apiService.getStoriesWithLocation(token)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}