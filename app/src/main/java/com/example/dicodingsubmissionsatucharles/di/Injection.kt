package com.example.dicodingsubmissionsatucharles.di

import android.content.Context
import com.example.dicodingsubmissionsatucharles.data.pref.UserPreference
import com.example.dicodingsubmissionsatucharles.data.pref.dataStore
import com.example.dicodingsubmissionsatucharles.data.repository.UserRepository
import com.example.dicodingsubmissionsatucharles.data.retrofit.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}