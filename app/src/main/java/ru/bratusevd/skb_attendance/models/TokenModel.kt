package ru.bratusevd.skb_attendance.models

import com.google.gson.annotations.SerializedName
import ru.bratusevd.skb_attendance.mainScreen.models.TimeModel


class TokenModel(
    @SerializedName("id")
    private var id: String,
    @SerializedName("userName")
    private var userName: String,
    @SerializedName("userLastName")
    private var userLastName: String,
    @SerializedName("userPhoto")
    private var userPhoto: String,
    @SerializedName("userSetting")
    private var userSetting: String,
    @SerializedName("visits")
    private var visits: ArrayList<TimeModel>,
    @SerializedName("accessToken")
    private var access: String,
    @SerializedName("refreshToken")
    private var refresh: String
) : java.io.Serializable {

    fun getAccess(): String {
        return access
    }

    fun setVisits(timeModels : ArrayList<TimeModel>){
        visits = timeModels
    }

    fun getFirstName(): String {
        return userName;
    }

    fun getLastName(): String {
        return userLastName;
    }

    fun getSettings(): String {
        return userSetting;
    }

    fun getPhoto(): String {
        return userPhoto;
    }

    fun getId(): String {
        return id;
    }

    fun getName(): String {
        return "$userLastName $userName"
    }


    fun getVisits(): ArrayList<TimeModel> {
        return visits
    }
}