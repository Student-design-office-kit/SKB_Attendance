package ru.bratusevd.skb_attendance.login

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.MainScreenActivity
import ru.bratusevd.skb_attendance.registration.RegistrationActivity
import ru.bratusevd.skb_attendance.services.network.NetworkHelper
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var loginButton: Button
    lateinit var registrationButton: Button
    lateinit var forgotText: TextView
    lateinit var mailDesText: TextView
    lateinit var passDesText: TextView
    lateinit var mailEditText: EditText
    lateinit var passEditText: EditText

    private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    private val VALID_PASS_REGEX: Pattern =
        Pattern.compile("^[a-zA-Z0-9]{8,32}+$", Pattern.CASE_INSENSITIVE)

    private val timeToGetCode: Int = 15

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        findViews()
    }

    private fun findViews() {
        loginButton = findViewById(R.id.loginActivity_loginButton)
        registrationButton = findViewById(R.id.loginActivity_registrationButton)
        forgotText = findViewById(R.id.loginActivity_forgotText)
        mailDesText = findViewById(R.id.loginActivity_emailDescText)
        passDesText = findViewById(R.id.loginActivity_passDescText)
        mailEditText = findViewById(R.id.loginActivity_emailInput)
        passEditText = findViewById(R.id.loginActivity_passwordInput)

        setOnClick()
    }

    private fun setOnClick() {
        loginButton.setOnClickListener(this)
        registrationButton.setOnClickListener(this)
        forgotText.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.loginActivity_registrationButton -> onRegistrationCLick()
            R.id.loginActivity_loginButton -> onLoginCLick()
            R.id.loginActivity_forgotText -> onForgotCLick()
        }
    }

    private fun onForgotCLick() {
        createCodeBottomSheetDialog().show()
    }

    @SuppressLint("InflateParams")
    private fun createCodeBottomSheetDialog(): BottomSheetDialog {
        val sheetDialog = BottomSheetDialog(this)
        val view: View = layoutInflater.inflate(R.layout.forgot_pass_dialog_code, null, false)
        val code_input = view.findViewById<EditText>(R.id.code_input)
        val timer = view.findViewById<TextView>(R.id.timerText)
        val description = view.findViewById<TextView>(R.id.recover_pas_des)

        setTimer(timeToGetCode * 1000, timer)
        timer.setOnClickListener { setTimer(timeToGetCode * 1000, timer) }
        view.findViewById<View>(R.id.forgot_confButton).setOnClickListener {
            if (code_input.length() < 4) {
                visibilityDescription(description, View.VISIBLE)
            } else {
                checkCode(code_input.text.toString(), description, sheetDialog)
            }
        }
        sheetDialog.setContentView(view)
        sheetDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        return sheetDialog
    }

    @SuppressLint("InflateParams")
    private fun createChangePassBottomSheetDialog(): BottomSheetDialog {
        val sheetDialog = BottomSheetDialog(this)
        val view: View = layoutInflater.inflate(R.layout.change_pass_dialog, null, false)
        val pass_input = view.findViewById<EditText>(R.id.change_pass_input)
        val passRep_input = view.findViewById<EditText>(R.id.change_password_repeat)
        val description = view.findViewById<TextView>(R.id.change_pass_des)
        val rep_description = view.findViewById<TextView>(R.id.change_passRepDescription)
        val button_conf = view.findViewById<TextView>(R.id.change_confButton)

        button_conf.setOnClickListener {
            if (pass_input.text.toString() == passRep_input.text.toString() && validatePass(
                    pass_input
                )
            ) {
                sheetDialog.dismiss()
            } else {
                if (!validatePass(pass_input)) visibilityDescription(description, View.VISIBLE)
                else visibilityDescription(description, View.INVISIBLE)
                if (pass_input.text.toString() == passRep_input.text.toString()) visibilityDescription(
                    rep_description,
                    View.VISIBLE
                ) else visibilityDescription(rep_description, View.INVISIBLE)
            }
        }

        sheetDialog.setContentView(view)
        sheetDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        return sheetDialog
    }

    private fun checkCode(code: String, description: TextView, sheetDialog: BottomSheetDialog) {
        val codeFromServer = "1234"
        if (code == codeFromServer) {
            sheetDialog.cancel()
            createChangePassBottomSheetDialog().show()
        } else {
            visibilityDescription(description, View.VISIBLE)
        }
    }

    private fun setTimer(timeMs: Int, timer: TextView) {
        object : CountDownTimer(timeMs.toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                timer.isEnabled = false
                timer.text = resources.getString(R.string.time_to_code) + " 0:" + String.format(
                    "%02d",
                    millisUntilFinished / 1000
                )
            }

            override fun onFinish() {
                timer.text = resources.getString(R.string.new_code)
                timer.isEnabled = true
            }
        }.start()
    }

    private fun onLoginCLick() {
        if (checkLogin()) startActivity(Intent(applicationContext, MainScreenActivity::class.java))
    }

    private fun onRegistrationCLick() {
        startActivity(Intent(applicationContext, RegistrationActivity::class.java))
        finish()
    }

    private fun validateMail(): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(getText(mailEditText))
        return matcher.find()
    }

    private fun validatePass(passInput: EditText): Boolean {
        val matcher: Matcher = VALID_PASS_REGEX.matcher(getText(passInput))
        return matcher.find()
    }

    private fun visibilityDescription(description: TextView, isVisible: Int) {
        description.visibility = isVisible
    }

    private fun checkLogin(): Boolean {
        val isMailCorrect: Boolean
        val isPassCorrect: Boolean

        if (!validatePass(passEditText)) {
            isPassCorrect = false
            visibilityDescription(passDesText, View.VISIBLE)
        } else {
            isPassCorrect = true
            visibilityDescription(passDesText, View.INVISIBLE)
        }

        if (!validateMail()) {
            isMailCorrect = false
            visibilityDescription(mailDesText, View.VISIBLE)
        } else {
            isMailCorrect = true
            visibilityDescription(mailDesText, View.INVISIBLE)
        }

        return isMailCorrect && isPassCorrect
    }

    private fun getText(editText: EditText): String {
        return editText.text.toString()
    }

    override fun onBackPressed() {

    }
}