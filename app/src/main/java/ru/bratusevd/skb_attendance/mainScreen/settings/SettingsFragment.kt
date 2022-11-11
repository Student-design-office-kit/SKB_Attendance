package ru.bratusevd.skb_attendance.mainScreen.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Response
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.adapters.NewsAdapter
import ru.bratusevd.skb_attendance.mainScreen.models.NewsModel
import ru.bratusevd.skb_attendance.models.RegistrationModel
import ru.bratusevd.skb_attendance.models.TokenModel
import ru.bratusevd.skb_attendance.models.UserModel
import ru.bratusevd.skb_attendance.services.network.NetworkServices
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.security.auth.callback.Callback


class SettingsFragment : Fragment(), OnClickListener {

    private var root: View? = null
    private lateinit var tokenModel: TokenModel
    private lateinit var userModel: UserModel

    private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    private val VALID_PASS_REGEX: Pattern =
        Pattern.compile("^[a-zA-Z0-9]{8,32}+$", Pattern.CASE_INSENSITIVE)

    private val VALID_NAME_REGEX: Pattern =
        Pattern.compile("^[А-Яа-я, .'-]{2,32}+$", Pattern.CASE_INSENSITIVE)

    private lateinit var userImage: ImageView
    private lateinit var userName: EditText
    private lateinit var nameDesText: TextView
    private lateinit var mailDesText: TextView
    private lateinit var surnameDesText: TextView
    private lateinit var userSurname: EditText
    private lateinit var userEmail: EditText
    private lateinit var changePass: TextView
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_settings, container, false)
        tokenModel = arguments?.getSerializable("tokenModel") as TokenModel

        findViews()
        return root
    }

    private fun findViews() {
        userEmail = root!!.findViewById(R.id.settingsActivity_mailInput)
        userSurname = root!!.findViewById(R.id.settingsActivity_surnameInput)
        userName = root!!.findViewById(R.id.settingsActivity_nameInput)
        userImage = root!!.findViewById(R.id.settingsActivity_userImage)
        changePass = root!!.findViewById(R.id.settingsActivity_changePass)
        saveButton = root!!.findViewById(R.id.settingsActivity_saveButton)
        mailDesText = root!!.findViewById(R.id.settingsActivity_mailDescText)
        nameDesText = root!!.findViewById(R.id.settingsActivity_nameDescText)
        surnameDesText = root!!.findViewById(R.id.settingsActivity_surnameDescText)

        fillData()
        setOnClick()
    }

    private fun fillData() {
        userEmail.setText(getMailFromPref())
        userSurname.setText(tokenModel.getLastName())
        userName.setText(tokenModel.getFirstName())
    }

    private fun setOnClick() {
        changePass.setOnClickListener(this)
        saveButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.settingsActivity_saveButton -> {

                if (checkValidation()) {
                    userModel = UserModel(
                        tokenModel.getId(),
                        userEmail.text.toString(),
                        getPassFromPref(),
                        userName.text.toString(),
                        userSurname.text.toString(),
                        tokenModel.getPhoto(),
                        tokenModel.getSettings()
                    )

                    updateInfo(userModel)
                }
            }
            R.id.settingsActivity_changePass -> {
                Toast.makeText(context, "Эта функция временно не работает", Toast.LENGTH_SHORT).show()
            }
            R.id.settingsActivity_userImage -> {
                Toast.makeText(context, "Эта функция временно не работает", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkValidation(): Boolean {
        val isMailCorrect: Boolean
        val isNameCorrect: Boolean
        val isSurNameCorrect: Boolean

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

        return isMailCorrect && isNameCorrect && isSurNameCorrect
    }

    private fun validateMail(): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(getText(userEmail))
        return matcher.find()
    }

    private fun validateName(): Boolean {
        val matcher: Matcher = VALID_NAME_REGEX.matcher(getText(userName))
        return matcher.find()
    }

    private fun validateSurName(): Boolean {
        val matcher: Matcher = VALID_NAME_REGEX.matcher(getText(userSurname))
        return matcher.find()
    }

    private fun visibilityDescription(description: TextView, isVisible: Int) {
        description.visibility = isVisible
    }

    private fun getMailFromPref(): String {
        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences("LoginPref", Context.MODE_PRIVATE)
        return sharedPref.getString("email", "").toString()
    }

    private fun getPassFromPref(): String {
        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences("LoginPref", Context.MODE_PRIVATE)
        return sharedPref.getString("pass", "").toString()
    }

    private fun updateInfo(userModel: UserModel) {
        NetworkServices.getInstance().jsonApi.updateUser(tokenModel.getAccess(), userModel)
            .enqueue(object : Callback,
                retrofit2.Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    requireActivity().supportFragmentManager.popBackStackImmediate()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun getText(editText: EditText): String {
        return editText.text.toString()
    }
}