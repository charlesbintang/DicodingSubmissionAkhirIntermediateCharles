package com.example.dicodingsubmissionsatucharles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingsubmissionsatucharles.data.repository.UserRepository
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.FileUploadResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddNewStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _addNewStoryMessage = MutableLiveData<String>()
    val addNewStoryMessage: LiveData<String> get() = _addNewStoryMessage

    fun upload(file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val token = repository.getSession().first().token
                val uploaded = repository.uploadStory("Bearer $token", file, description)

                _isLoading.value = false
                _addNewStoryMessage.value = uploaded.message
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                val errorMessage = errorResponse.message

                _addNewStoryMessage.value = errorMessage
                _isLoading.value = false
            } catch (e: Exception) {
                _addNewStoryMessage.value = e.message ?: "An unexpected error occurred"
                _isLoading.value = false
            }
        }
    }
}