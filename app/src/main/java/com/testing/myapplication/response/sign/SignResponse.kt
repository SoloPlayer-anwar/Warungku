package com.testing.myapplication.response.sign


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.testing.myapplication.response.sign.User

data class SignResponse(
    @Expose
    @SerializedName("access_token")
    val accessToken: String,
    @Expose
    @SerializedName("token_type")
    val tokenType: String,
    @Expose
    @SerializedName("user")
    val user: User
)