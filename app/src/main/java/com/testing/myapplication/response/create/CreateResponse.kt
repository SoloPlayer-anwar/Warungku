package com.testing.myapplication.response.create


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateResponse(
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