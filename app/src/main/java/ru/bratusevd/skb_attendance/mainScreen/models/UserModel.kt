package ru.bratusevd.skb_attendance.mainScreen.models

import com.google.gson.annotations.SerializedName

class UserModel(
    @SerializedName("user") var user: User? = User()
) {
    class User(
        @SerializedName("_id") var Id: String? = null,
        @SerializedName("userName") var userName: String? = null,
        @SerializedName("userLastName") var userLastName: String? = null,
        @SerializedName("userPhoto") var userPhoto: String? = null,
        @SerializedName("userSetting") var userSetting: String? = null,
        @SerializedName("visits") var visits: ArrayList<TimeModel> = arrayListOf()
    )
}