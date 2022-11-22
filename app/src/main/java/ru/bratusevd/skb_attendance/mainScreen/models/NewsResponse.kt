package ru.bratusevd.skb_attendance.mainScreen.models

import com.google.gson.annotations.SerializedName

class NewsResponse(
    @SerializedName("date") var date: Long? = null,
    @SerializedName("text") var text: String? = null,
    @SerializedName("attachments") var attachments: ArrayList<Attachments> = arrayListOf(),
) {
    class Attachments(
        @SerializedName("type") var type: String? = null,
        @SerializedName("photo") var photo: PhotoNews? = PhotoNews()
    ) {
        class PhotoNews(
            @SerializedName("sizes") var sizes: ArrayList<Sizes> = arrayListOf(),
        ) {
            class Sizes(
                @SerializedName("height") var height: Int? = null,
                @SerializedName("type") var type: String? = null,
                @SerializedName("width") var width: Int? = null,
                @SerializedName("url") var url: String? = null
            )
        }

    }
}