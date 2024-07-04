package com.example.dicodingsubmissionsatucharles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingsubmissionsatucharles.data.repository.UserRepository
import com.example.dicodingsubmissionsatucharles.data.retrofit.response.ListStoryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _getStoriesLocationMessage = MutableLiveData<String>()
    val getStoriesLocationMessage: LiveData<String> get() = _getStoriesLocationMessage

    private val _getStoriesListLocation = MutableLiveData<List<ListStoryItem>>()
    val getStoriesListLocation: LiveData<List<ListStoryItem>> get() = _getStoriesListLocation

    init {
        getStoriesWithLocation()
    }

    private fun getStoriesWithLocation() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val token = repository.getSession().first().token
                val response = repository.getStoriesWithLocation("Bearer $token")

                _getStoriesListLocation.value = response.listStory
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _getStoriesLocationMessage.value = e.message ?: "Unknown error"
            }
        }
    }
}