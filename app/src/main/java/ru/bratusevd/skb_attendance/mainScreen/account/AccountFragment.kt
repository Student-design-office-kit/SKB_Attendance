package ru.bratusevd.skb_attendance.mainScreen.account

import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.BoringLayout
import android.util.Log
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.models.TokenModel
import ru.bratusevd.skb_attendance.models.VisitModel
import ru.bratusevd.skb_attendance.services.codeInput.Verification
import ru.bratusevd.skb_attendance.services.network.NetworkServices
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccountFragment : Fragment() {

    private var root: View? = null
    private var avatarURLs = arrayOf(
        "https://drive.google.com/uc?export=download&id=1K0jiBEixCLCYsYM3eDKOg-YyzmnzNwSI",
        "https://drive.google.com/uc?export=download&id=1ZrSF9q76vg5fkRt08dJRh5XYSD7S3056",
        "https://drive.google.com/uc?export=download&id=1u0JqPdtgBAYjeR7XLrbqeHE87kM9OxU3",
        "https://drive.google.com/uc?export=download&id=14ibrIOx_qmQPP1dmMo_Jhk3465WVy-Fq",
        "https://drive.google.com/uc?export=download&id=1Bv5KW28_7AFh-7Ra7_h5pj_q2KM2Ngv3",
        "https://drive.google.com/uc?export=download&id=1A5gfPCRNwPJNd9tkjLMZ6qwyk_BjtIRp",
        "https://drive.google.com/uc?export=download&id=1sovv7OYZqzWaaihyfgi8Kh4NiBgoJSt-",
        "https://drive.google.com/uc?export=download&id=1ScRplrYsxSEqIWwdz3ATFWjWsC5VXfG_"
    )

    private lateinit var codeInput: Button
    private lateinit var userImage: ImageView
    private lateinit var userName: TextView
    private lateinit var userPost: TextView
    private lateinit var userStatus: TextView
    private lateinit var test: LinearLayout
    private lateinit var verificationCode: Verification

    private lateinit var tokenModel: TokenModel
    private val APP_PREFERENCES = "visit"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_account, container, false)
        tokenModel = arguments?.getSerializable("tokenModel") as TokenModel
        findViews()
        return root
    }

    private fun findViews() {
        codeInput = root?.findViewById(R.id.accountFragment_codeInput)!!
        userImage = root!!.findViewById(R.id.accountFragment_userImage)
        userName = root!!.findViewById(R.id.accountFragment_userName)
        userStatus = root!!.findViewById(R.id.user_status)
        userPost = root!!.findViewById(R.id.laboratory)
        userStatus.isVisible = readStatus()
        userName.text = tokenModel.getName()
        val tmp: String = tokenModel.getSettings()
        userPost.text = try {
            tmp.replace(";", "\n")
        } catch (e: Exception) {
            "Студент"
        }

        setUserImage()
        setOnClick()
    }

    private fun setUserImage() {
        val photoPosition = try {
            Integer.parseInt(tokenModel.getPhoto())
        } catch (e: Exception) {
            0
        }
        Glide.with(this)
            .load(avatarURLs[photoPosition])
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(userImage);
    }

    private fun setOnClick() {
        onInputCodeClick()
        onUserImageClick()
    }

    private fun onUserImageClick() {}

    private fun onInputCodeClick() {
        codeInput.setOnClickListener {
            val promptsView = View.inflate(context, R.layout.code_input_bottom_dialog, null)
            val alertDialog: AlertDialog = AlertDialog.Builder(context)
                .setView(promptsView)
                .create()

            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            var code: String
            promptsView.findViewById<Button>(R.id.codeSuccess_button).setOnClickListener {
                verificationCode = promptsView.findViewById(R.id.code_inputText)
                test = promptsView.findViewById(R.id.testLayout)
                code = verificationCode.phoneCode
                verificationCode(tokenModel.getAccess(), alertDialog, code)
            }
            alertDialog.show()
        }
    }

    private fun checkIn(visitModel: VisitModel, token: String) {
        NetworkServices.getInstance().jsonApi.checkIn(token, visitModel)
            .enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    Log.d("checkIn", response.code().toString())
                    writeStatus(false)
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {}
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun writeVisit() {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm")
        val currentVisit = LocalDateTime.now().format(formatter)
        var visit: String = readVisit()
        if (visit != "") {
            var curDate = currentVisit.split("/")[0]
            var lastDate = visit.split("/")[0]

            if (curDate != lastDate) {
                val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
                    APP_PREFERENCES, MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                editor.clear();
                editor.putString("visit", currentVisit)
                editor.apply()
            } else {
                var visitModel = VisitModel(
                    tokenModel.getId(),
                    lastDate,
                    visit.split("/")[1],
                    currentVisit.split("/")[1]
                )

                checkIn(visitModel, tokenModel.getAccess())
                val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
                    APP_PREFERENCES, MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                editor.clear().apply()
            }

        } else {
            val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            editor.putString("visit", currentVisit)
            editor.apply()
        }
    }

    private fun readVisit(): String {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            APP_PREFERENCES, MODE_PRIVATE
        )
        return sharedPreferences.getString("visit", "").toString()
    }

    private fun readStatus(): Boolean {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            APP_PREFERENCES, MODE_PRIVATE
        )
        return sharedPreferences.getBoolean("status", false)
    }

    private fun writeStatus(status: Boolean) {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            APP_PREFERENCES, MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean("status", status)
        editor.apply()
    }

    private fun verificationCode(token: String, alertDialog: AlertDialog, code: String) {
        NetworkServices.getInstance().jsonApi.getCode(token).enqueue(object : Callback<String> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                if ((response?.body().toString() == code) || (("0" + response?.body()
                        .toString()) == code)
                ) {
                    writeVisit()
                    writeStatus(true)
                    Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                    alertDialog.cancel()
                    alertDialog.dismiss()
                } else {
                    test.startAnimation(
                        TranslateAnimation(
                            0f, 50f,
                            0f, 0f
                        )
                            .apply {
                                duration = 300
                            })
                    test.startAnimation(
                        TranslateAnimation(
                            50f, -50f,
                            0f, 0f
                        )
                            .apply {
                                duration = 150
                            })
                    vibratePhone()
                    Toast.makeText(context, "Попробуйте другой код", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {}

        })
    }

    fun Fragment.vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}