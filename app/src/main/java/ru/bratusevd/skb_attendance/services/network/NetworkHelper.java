package ru.bratusevd.skb_attendance.services.network;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bratusevd.skb_attendance.models.LoginModel;
import ru.bratusevd.skb_attendance.models.TokenModel;
import ru.bratusevd.skb_attendance.models.javaLoginModel;

public class NetworkHelper {

    public void register(){
        NetworkServices.getInstance().getJSONApi().registration().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void login(){
        javaLoginModel loginModel = new javaLoginModel("mail123", "pass123");
        NetworkServices.getInstance().getJSONApi().login(loginModel).enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                int tmp = response.code();
                Log.d("token", response.code()+"");
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Log.d("token", t.getMessage());
            }
        });
    }
}
