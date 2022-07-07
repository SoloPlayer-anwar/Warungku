package com.testing.myapplication.httpnetwork

import com.testing.myapplication.response.Coin
import com.testing.myapplication.response.product.ProductResponse
import com.testing.myapplication.response.sign.SignResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface EndPoint {
    @FormUrlEncoded
    @POST("register")
    fun register(@Field("name") name:String,
                 @Field("email") email:String,
                 @Field("password") password:String,
                 @Field("password_confirmation") passwordConfirm: String): Observable<Coin<SignResponse>>

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email: String,
              @Field("password") password: String): Observable<Coin<SignResponse>>


    @Multipart
    @POST("createProduct")
    fun komentar    (@Query("name_warung") nameComment:String,
                     @Query("alamat_warung") statusComment: String,
                     @Query("lat") category:String,
                     @Query("long") comment:String,
                     @Part photo_warung: MultipartBody.Part):Observable<ProductResponse>


}