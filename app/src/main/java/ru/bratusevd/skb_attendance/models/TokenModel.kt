package ru.bratusevd.skb_attendance.models

class TokenModel(
    private var id: String,
    private var userName: String,
    private var userLastName: String,
    private var userPhoto: String,
    private var userSetting: String,
    private var visits: ArrayList<String>,
    private var access: String,
    private var refresh: String
) : java.io.Serializable{

    fun getAccess(): String {
        return access
    }

    fun getId(): String {
        return id;
    }

    fun getName(): String {
        return "$userLastName $userName"
    }

    fun getVisits(): ArrayList<String>{
        return visits
    }
}