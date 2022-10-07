package ru.bratusevd.skb_attendance.mainScreen.models

import com.google.gson.annotations.SerializedName

class TimeModel(
    @SerializedName("startTime") private var startTime: String,
    @SerializedName("date") private var date: String,
    @SerializedName("endTime") private var endTime: String = "17:50"
) : java.io.Serializable {
    fun getStartTime(): String {
        return startTime
    }

    fun getDate(): String {
        return date
    }

    fun getEndTime(): String {
        return endTime
    }
}