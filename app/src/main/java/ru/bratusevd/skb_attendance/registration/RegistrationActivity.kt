package ru.bratusevd.skb_attendance.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.login.LoginActivity
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var registrationButton: Button
    lateinit var haveAccText: TextView
    lateinit var mailDesText: TextView
    lateinit var passDesText: TextView
    lateinit var nameDesText: TextView
    lateinit var mailEditText: EditText
    lateinit var passEditText: EditText
    lateinit var nameEditText: EditText

    private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    private val VALID_PASS_REGEX: Pattern =
        Pattern.compile("^[a-zA-Z0-9]{8,32}+$", Pattern.CASE_INSENSITIVE)

    private val VALID_NAME_REGEX: Pattern =
        Pattern.compile("^[А-Яа-я, .'-]{2,32}+$", Pattern.CASE_INSENSITIVE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.regist_activity)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        findViews()
    }

    private fun findViews() {
        registrationButton = findViewById(R.id.registActivity_registrationButton)
        haveAccText = findViewById(R.id.registActivity_haveAccText)
        mailDesText = findViewById(R.id.rgistActivity_emailDescText)
        passDesText = findViewById(R.id.registActivity_passDescText)
        nameDesText = findViewById(R.id.rgistActivity_nameDescText)
        mailEditText = findViewById(R.id.registActivity_emailInput)
        passEditText = findViewById(R.id.registActivity_passwordInput)
        nameEditText = findViewById(R.id.registActivity_nameInput)

        setOnClick()
    }

    private fun setOnClick() {
        registrationButton.setOnClickListener(this)
        haveAccText.setOnClickListener(this)
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
        if (checkRegistration()){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
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

    private fun visibilityDescription(description: TextView, isVisible: Int) {
        description.visibility = isVisible
    }

    private fun checkRegistration(): Boolean {
        val isMailCorrect: Boolean
        val isNameCorrect: Boolean
        val isPassCorrect: Boolean

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

        if (!validateMail()) {
            isMailCorrect = false
            visibilityDescription(mailDesText, View.VISIBLE)
        } else {
            isMailCorrect = true
            visibilityDescription(mailDesText, View.INVISIBLE)
        }

        return isMailCorrect && isPassCorrect && isNameCorrect
    }

    private fun getText(editText: EditText): String {
        return editText.text.toString()
    }

    override fun onBackPressed() {
    }

}