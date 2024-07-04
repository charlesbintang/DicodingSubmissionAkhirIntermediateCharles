package com.example.dicodingsubmissionsatucharles.data.pref

data class UserModel(
    val userID: String,
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
