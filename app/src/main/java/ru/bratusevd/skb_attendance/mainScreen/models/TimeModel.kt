package ru.bratusevd.skb_attendance.mainScreen.models

class TimeModel(private var startTime: String, private var date: String,
    private var endTime: String = "17:50",
    private var timeCounter: String = "3,5 часа"
){
    fun getStartTime(): String{
        return startTime
    }

    fun getDate(): String{
        return date
    }

    fun getEndTime(): String{
        return endTime
    }

    fun getTimeCounter(): String{
        return timeCounter
    }
}