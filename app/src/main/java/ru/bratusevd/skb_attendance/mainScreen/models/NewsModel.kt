package ru.bratusevd.skb_attendance.mainScreen.models

class NewsModel(
    private var images: String,
    private var description: String,
    private var header: String,
    private var dataText: String
): java.io.Serializable {
    fun getImage(): String {
        return images
    }

    fun getDescription(): String {
        return description
    }

    fun getHeader(): String {
        return header
    }

    fun getDataText(): String {
        return dataText
    }
}