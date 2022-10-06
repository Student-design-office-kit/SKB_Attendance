package ru.bratusevd.skb_attendance.services.network;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bratusevd.skb_attendance.models.LoginModel;
import ru.bratusevd.skb_attendance.models.RegistrationModel;
import ru.bratusevd.skb_attendance.models.TokenModel;
import ru.bratusevd.skb_attendance.models.VisitModel;

public class NetworkHelper {

    public void register(){
        RegistrationModel registrationModel = new RegistrationModel("mail@mail.ru",
                "password", "Denis", "Bratusev", "", "");
        NetworkServices.getInstance().getJSONApi().registration(registrationModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void login(){
        LoginModel loginModel = new LoginModel("email5@mail.ru", "password");
        NetworkServices.getInstance().getJSONApi().login(loginModel).enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
            }
        });
    }

    public void checkIn(){
        VisitModel visitModel = new VisitModel("6246f8e3d9450d1330d4ef77", "06.10.2022",
                "15:00", "16:00");
        NetworkServices.getInstance().getJSONApi().checkIn("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYzM2VjYzA0M2RjNTVlMDA4ZWViZWJkYiIsInVzZXJOYW1lIjoibmFtZSIsInVzZXJMYXN0TmFtZSI6Imxhc3ROYW1lIiwidXNlclBob3RvIjoicGhvdG8iLCJ1c2VyU2V0dGluZyI6InNldHRpbmdzIiwidmlzaXRzIjpbXSwiaWF0IjoxNjY1MDYwMDg0LCJleHAiOjE2NjUwNjU0ODR9.ST5nDEG2xonul6LJuPsQt83jj7mk_mFBMC07c68tfik", visitModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
