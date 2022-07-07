package com.testing.myapplication.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Meta(
    @Expose
    @SerializedName("code")
    val code: Int,

    @Expose
    @SerializedName("message")
    val message: String,

    @Expose
    @SerializedName("status")
    val status: String
)