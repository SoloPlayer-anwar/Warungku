package com.testing.myapplication.httpnetwork

import com.testing.myapplication.response.Coin
import com.testing.myapplication.response.create.CreateResponse
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
    fun create (@Query("name_warung") name_warung:String,
                @Query("alamat_warung") alamat_warung: String,
                @Query("lat") lat:Double,
                @Query("long") long:Double,
                @Part photo_warung: MultipartBody.Part):Observable<Coin<CreateResponse>>


    @GET("product")
    fun getHome():Observable<ProductResponse>

    @POST("deleteProduct/{id}")
    fun deleteProduct (@Path(value = "id") id:Int): Observable<Coin<CreateResponse>>

    @Multipart
    @POST("updateProduct/{id}")
    fun updateProduct(@Path(value = "id")id:Int,
                      @Query("name_warung") nameWarung:String,
                      @Query("alamat_warung") alamatWarung:String,
                      @Query("lat") lat:Double,
                      @Query("long") long:Double,
                      @Part photo_warung: MultipartBody.Part):Observable<Coin<CreateResponse>>
}