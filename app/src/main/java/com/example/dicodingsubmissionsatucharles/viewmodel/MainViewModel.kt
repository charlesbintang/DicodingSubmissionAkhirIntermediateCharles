package com.example.dicodingsubmissionsatucharles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dicodingsubmissionsatucharles.data.pref.UserModel
import com.example.dicodingsubmissionsatucharles.data.repository.UserRepository
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val listStory: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)

    private val _mainMessage = MutableLiveData<String>()
    val mainMessage: LiveData<String> get() = _mainMessage

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.logout()
                _isLoading.value = false
            } catch (e: Exception) {
                _mainMessage.value = e.message ?: "An unexpected error occurred"
            }
        }
    }
}