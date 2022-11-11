package ru.bratusevd.skb_attendance.services.network

import retrofit2.Call
import retrofit2.http.*
import ru.bratusevd.skb_attendance.models.*

interface JSONPlaceHolderApi {
    @POST("registration")
    fun registration(@Body registrationModel: RegistrationModel): Call<Void>

    @PUT("updateUser")
    fun updateUser(@Header("Authorization") token: String, @Body userModel: UserModel): Call<Void>

    @POST("login")
    fun login(@Body loginModel: LoginModel): Call<ArrayList<TokenModel>>

    @GET("code")
    fun getCode(@Header("Authorization") token: String): Call<String>

    @POST("uploadVisit")
    fun checkIn(
        @Header("Authorization") token: String,
        @Body visitModel: VisitModel
    ): Call<Void>
}