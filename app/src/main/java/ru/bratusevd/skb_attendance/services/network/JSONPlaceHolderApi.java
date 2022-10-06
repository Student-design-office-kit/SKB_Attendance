package ru.bratusevd.skb_attendance.services.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.bratusevd.skb_attendance.models.LoginModel;
import ru.bratusevd.skb_attendance.models.TokenModel;
import ru.bratusevd.skb_attendance.models.javaLoginModel;

public interface JSONPlaceHolderApi {
    @POST("/registration")
    Call<Void> registration();

    @POST("login")
    Call<TokenModel> login(@Body javaLoginModel loginModel);
}
