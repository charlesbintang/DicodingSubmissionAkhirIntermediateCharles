package com.example.dicodingsubmissionsatucharles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingsubmissionsatucharles.data.repository.UserRepository
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _signUpMessage = MutableLiveData<String>()
    val signUpMessage: LiveData<String> = _signUpMessage

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val signUpResponse = repository.signUp(name, email, password)

                _isLoading.value = false
                _isSuccess.value = true
                _signUpMessage.value = signUpResponse.message.toString()
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = errorResponse.message
                _signUpMessage.value = errorMessage ?: "An unexpected error occurred"
                _isSuccess.value = false
                _isLoading.value = false
            } catch (e: Exception) {
                _signUpMessage.value = e.message ?: "An unexpected error occurred"
                _isSuccess.value = false
                _isLoading.value = false
            }
        }
    }
}