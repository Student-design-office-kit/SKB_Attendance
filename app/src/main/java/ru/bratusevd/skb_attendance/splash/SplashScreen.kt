package ru.bratusevd.skb_attendance.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.login.LoginActivity
import ru.bratusevd.skb_attendance.mainScreen.MainScreenActivity
import ru.bratusevd.skb_attendance.models.LoginModel
import ru.bratusevd.skb_attendance.models.TokenModel
import ru.bratusevd.skb_attendance.services.network.NetworkServices

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val backgroundImage: ImageView = findViewById(R.id.SplashScreenImage)
        backgroundImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.side_slide))

        Handler().postDelayed(Runnable {
            if (!autoLogin()) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }

    private fun autoLogin(): Boolean {
        return if (getCheckFromPref()) {
            login(LoginModel(getMailFromPref(), getPassFromPref()))
            true
        } else false
    }

    fun getMailFromPref(): String {
        val sharedPref: SharedPreferences = getSharedPreferences("LoginPref", Context.MODE_PRIVATE)
        return sharedPref.getString("email", "").toString()
    }

    fun getPassFromPref(): String {
        val sharedPref: SharedPreferences = getSharedPreferences("LoginPref", Context.MODE_PRIVATE)
        return sharedPref.getString("pass", "").toString()
    }

    fun getCheckFromPref(): Boolean {
        val sharedPref: SharedPreferences = getSharedPreferences("LoginPref", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("remember", false)
    }

    private fun login(loginModel: LoginModel) {
        NetworkServices.getInstance().jsonApi.login(loginModel)
            .enqueue(object : Callback<ArrayList<TokenModel>> {
                override fun onResponse(
                    call: Call<ArrayList<TokenModel>>,
                    response: Response<ArrayList<TokenModel>>
                ) {
                    if (response.code() in 200..299) {
                        val intent = Intent(applicationContext, MainScreenActivity::class.java)
                        val tokenModel = TokenModel(
                            response.body()!![0].getId(),
                            response.body()!![0].getFirstName(),
                            response.body()!![0].getLastName(),
                            response.body()!![0].getPhoto(),
                            response.body()!![0].getSettings(),
                            response.body()!![0].getVisits(),
                            "Bearer " + response.body()!![1].getAccess(),
                            response.body()!![1].getAccess()
                        )
                        intent.putExtra("userInfo", tokenModel)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Неверный логин или пароль",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ArrayList<TokenModel>>, t: Throwable) {}
            })
    }
}