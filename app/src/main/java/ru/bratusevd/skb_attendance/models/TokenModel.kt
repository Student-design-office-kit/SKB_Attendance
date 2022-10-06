package ru.bratusevd.skb_attendance.models

class TokenModel(
    private var access: String,
    private var refresh: String){

    fun getAccess(): String {
        return access
    }

}