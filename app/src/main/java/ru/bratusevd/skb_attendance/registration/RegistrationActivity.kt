package ru.bratusevd.skb_attendance.registration

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.login.LoginActivity
import ru.bratusevd.skb_attendance.models.RegistrationModel
import ru.bratusevd.skb_attendance.services.network.NetworkServices
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var registrationButton: Button
    lateinit var haveAccText: TextView
    lateinit var mailDesText: TextView
    lateinit var passDesText: TextView
    lateinit var nameDesText: TextView
    lateinit var surnameDesText: TextView
    lateinit var mailEditText: EditText
    lateinit var passEditText: EditText
    lateinit var nameEditText: EditText
    lateinit var surnameEditText: EditText

    private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    private val VALID_PASS_REGEX: Pattern =
        Pattern.compile("^[a-zA-Z0-9_]{8,32}+$", Pattern.CASE_INSENSITIVE)

    private val VALID_NAME_REGEX: Pattern =
        Pattern.compile("^[А-Яа-я, .'-]{2,32}+$", Pattern.CASE_INSENSITIVE)

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.regist_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        findViews()
    }

    private fun findViews() {
        registrationButton = findViewById(R.id.registActivity_registrationButton)
        haveAccText = findViewById(R.id.registActivity_haveAccText)
        mailDesText = findViewById(R.id.rgistActivity_emailDescText)
        passDesText = findViewById(R.id.registActivity_passDescText)
        nameDesText = findViewById(R.id.rgistActivity_nameDescText)
        surnameDesText = findViewById(R.id.rgistActivity_surnameDescText)
        mailEditText = findViewById(R.id.registActivity_emailInput)
        passEditText = findViewById(R.id.registActivity_passwordInput)
        nameEditText = findViewById(R.id.registActivity_nameInput)
        surnameEditText = findViewById(R.id.registActivity_surnameInput)

        setOnClick()
    }

    private fun setOnClick() {
        registrationButton.setOnClickListener(this)
        haveAccText.setOnClickListener(this)
    }


    private fun register(registrationModel: RegistrationModel) {
        NetworkServices.getInstance().jsonApi.registration(registrationModel)
            .enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    if (response.code() in 200..299) {
                        Toast.makeText(
                            applicationContext,
                            "Регистрация успешна завершена",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Ошибка регистрации", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(applicationContext, "Ошибка регистрации ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.registActivity_registrationButton -> onRegistrationCLick()
            R.id.registActivity_haveAccText -> onHaveAccClick()
        }
    }

    private fun onHaveAccClick() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }

    private fun onRegistrationCLick() {
        if (checkRegistration()) {
            try {
                register(
                    RegistrationModel(
                        getText(mailEditText),
                        getText(passEditText),
                        getText(nameEditText).trim(),
                        getText(surnameEditText).trim(),
                        "",
                        ""
                    )
                )
            }catch (e: Exception){ }
        }
    }

    private fun validateMail(): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(getText(mailEditText))
        return matcher.find()
    }

    private fun validatePass(): Boolean {
        val matcher: Matcher = VALID_PASS_REGEX.matcher(getText(passEditText))
        return matcher.find()
    }

    private fun validateName(): Boolean {
        val matcher: Matcher = VALID_NAME_REGEX.matcher(getText(nameEditText))
        return matcher.find()
    }

    private fun validateSurName(): Boolean {
        val matcher: Matcher = VALID_NAME_REGEX.matcher(getText(surnameEditText))
        return matcher.find()
    }

    private fun visibilityDescription(description: TextView, isVisible: Int) {
        description.visibility = isVisible
    }

    private fun checkRegistration(): Boolean {
        val isMailCorrect: Boolean
        val isNameCorrect: Boolean
        val isPassCorrect: Boolean
        val isSurNameCorrect: Boolean

        if (!validatePass()) {
            isPassCorrect = false
            visibilityDescription(passDesText, View.VISIBLE)
        } else {
            isPassCorrect = true
            visibilityDescription(passDesText, View.INVISIBLE)
        }

        if (!validateName()) {
            isNameCorrect = false
            visibilityDescription(nameDesText, View.VISIBLE)
        } else {
            isNameCorrect = true
            visibilityDescription(nameDesText, View.INVISIBLE)
        }

        if (!validateSurName()) {
            isSurNameCorrect = false
            visibilityDescription(surnameDesText, View.VISIBLE)
        } else {
            isSurNameCorrect = true
            visibilityDescription(surnameDesText, View.INVISIBLE)
        }

        if (!validateMail()) {
            isMailCorrect = false
            visibilityDescription(mailDesText, View.VISIBLE)
        } else {
            isMailCorrect = true
            visibilityDescription(mailDesText, View.INVISIBLE)
        }

        return isMailCorrect && isPassCorrect && isNameCorrect && isSurNameCorrect
    }

    private fun getText(editText: EditText): String {
        return editText.text.toString()
    }

    override fun onBackPressed() {
    }

}