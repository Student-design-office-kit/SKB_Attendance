package ru.bratusevd.skb_attendance.models

class UserModel(
    val userId: String,
    val email: String,
    val password: String,
    val userName: String,
    val userLastName: String,
    val userPhoto: String = "",
    val userSetting: String = ""
)