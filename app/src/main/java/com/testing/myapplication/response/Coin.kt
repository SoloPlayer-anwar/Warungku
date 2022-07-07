package com.testing.myapplication.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Coin<T>(
    @Expose
    @SerializedName("data")
    val `data`: T? = null,

    @Expose
    @SerializedName("meta")
    val meta: Meta
)