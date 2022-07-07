package com.testing.myapplication.response.sign


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose
    @SerializedName("created_at")
    val createdAt: Int,

    @Expose
    @SerializedName("email")
    val email: String,

    @Expose
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Any,

    @Expose
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("updated_at")
    val updatedAt: Int
)