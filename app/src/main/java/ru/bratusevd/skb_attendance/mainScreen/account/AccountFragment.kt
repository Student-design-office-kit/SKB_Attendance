package ru.bratusevd.skb_attendance.mainScreen.account

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.adapters.StoryAdapter
import ru.bratusevd.skb_attendance.mainScreen.models.TimeModel
import ru.bratusevd.skb_attendance.models.TokenModel
import ru.bratusevd.skb_attendance.models.VisitModel
import ru.bratusevd.skb_attendance.services.codeInput.Verification
import ru.bratusevd.skb_attendance.services.network.NetworkServices
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccountFragment : Fragment() {

    private var root: View? = null

    private lateinit var codeInput: ImageView
    private lateinit var userImage: ImageView
    private lateinit var userName: TextView

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
        codeInput = root?.findViewById(R.id.accountFragment_scanner)!!
        userImage = root!!.findViewById(R.id.accountFragment_userImage)
        userName = root!!.findViewById(R.id.accountFragment_userName)

        userName.text = tokenModel.getName()
        setUserImage()
        setOnClick()
    }

    private fun setUserImage() {
        Glide.with(this)
            .load("https://s1.1zoom.ru/big0/94/345461-admin.jpg")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(userImage);
    }

    private fun setOnClick() {
        onInputCodeClick()
        onUserImageClick()
    }

    private fun onUserImageClick() {

    }

    private fun onInputCodeClick() {
        codeInput.setOnClickListener {
            val promptsView = View.inflate(context, R.layout.code_input_bottom_dialog, null)
            val alertDialog: AlertDialog = AlertDialog.Builder(context)
                .setView(promptsView)
                .create()

            var code: String
            promptsView.findViewById<Button>(R.id.codeSuccess_button).setOnClickListener {
                code = promptsView.findViewById<Verification>(R.id.code_inputText).phoneCode.toString()
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
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {}
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun writeVisit() {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm")
        val currentVisit = LocalDateTime.now().format(formatter)
        var visit: String = readVisit()
        if (!visit.equals("")) {
            var curDate = currentVisit.split("/")[0]
            var lastDate = visit.split("/")[0]

            if (!curDate.equals(lastDate)) {
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

    private fun verificationCode(token: String, alertDialog: AlertDialog, code: String) {
        NetworkServices.getInstance().jsonApi.getCode(token).enqueue(object : Callback<String> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                if (response?.body().toString() == code) {
                    writeVisit()
                    Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                    alertDialog.cancel()
                    alertDialog.dismiss()
                } else {
                    Toast.makeText(context, "Попробуйте другой код", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {}

        })
    }
}