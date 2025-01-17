package com.example.dicodingsubmissionsatucharles.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

