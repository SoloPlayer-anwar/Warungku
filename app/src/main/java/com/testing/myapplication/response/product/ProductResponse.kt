package com.testing.myapplication.response.product


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

data class Data(
    @Expose
    @SerializedName("alamat_warung")
    val alamatWarung: String,

    @Expose
    @SerializedName("created_at")
    val createdAt: Int,

    @Expose
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("lat")
    val lat: Double,

    @Expose
    @SerializedName("long")
    val long: Double,

    @Expose
    @SerializedName("name_warung")
    val nameWarung: String,

    @Expose
    @SerializedName("photo_warung")
    val photoWarung: String,

    @Expose
    @SerializedName("updated_at")
    val updatedAt: Int
)

data class ProductResponse(
    @Expose
    @SerializedName("data")
    val `data`: List<Data>,

    @Expose
    @SerializedName("meta")
    val meta: Meta
)