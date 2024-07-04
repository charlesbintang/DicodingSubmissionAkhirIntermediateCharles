package com.example.dicodingsubmissionsatucharles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingsubmissionsatucharles.data.pref.UserModel
import com.example.dicodingsubmissionsatucharles.data.repository.UserRepository
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {


    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginMessage = MutableLiveData<String>()
    val loginMessage: LiveData<String> get() = _loginMessage


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = repository.login(email, password)

                _isLoading.value = false
                _loginResponse.value = response

                if (!response.error) {
                    saveSession(
                        UserModel(
                            response.loginResult.userId,
                            response.loginResult.name,
                            email,
                            response.loginResult.token,
                            true
                        )
                    )
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                val errorMessage = errorResponse.message

                _loginMessage.value = errorMessage
                _isLoading.value = false
            } catch (e: Exception) {
                _loginMessage.value = e.message ?: "An unexpected error occurred"
                _isLoading.value = false
            }
        }
    }

    private fun saveSession(userModel: UserModel) {
        viewModelScope.launch {
            try {
                repository.saveSession(userModel)
            } catch (e: Exception) {
                _loginMessage.value = e.message ?: "An unexpected error occurred"
            }
        }
    }
}