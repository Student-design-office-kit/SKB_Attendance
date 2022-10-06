package ru.bratusevd.skb_attendance.models

class RegistrationModel(
    private val email: String,
    private val password: String,
    private val userName: String,
    private val userLastName: String,
    private val userPhoto: String = "",
    private val userSetting: String = ""
){
}