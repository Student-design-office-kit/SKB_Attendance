package ru.bratusevd.skb_attendance.models

class LoginModel(
    private var email: String,
    private var password: String){

    fun getMail(): String {
        return email
    }

    fun getPass(): String {
        return password
    }
}