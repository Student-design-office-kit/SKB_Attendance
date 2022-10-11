package ru.bratusevd.skb_attendance.services.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import ru.bratusevd.skb_attendance.models.LoginModel
import ru.bratusevd.skb_attendance.models.RegistrationModel
import ru.bratusevd.skb_attendance.models.TokenModel
import ru.bratusevd.skb_attendance.models.VisitModel

interface JSONPlaceHolderApi {
    @POST("registration")
    fun registration(@Body registrationModel: RegistrationModel): Call<Void>

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