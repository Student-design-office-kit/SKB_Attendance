package ru.bratusevd.skb_attendance.mainScreen.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.models.NewsModel
import ru.bratusevd.skb_attendance.mainScreen.models.NewsResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(context: Context, newsList: ArrayList<NewsResponse>) : BaseAdapter() {

    private val mContext: Context = context
    private val mNewsList: ArrayList<NewsResponse> = newsList

    override fun getCount(): Int {
        return mNewsList.size
    }

    override fun getItem(position: Int): NewsResponse {
        return mNewsList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View? {
        var view: View? = convertView
        val newsModel: NewsResponse = getItem(position)
        val mInflater = LayoutInflater.from(mContext)
        if (view == null) {
            view = mInflater.inflate(R.layout.news_item, null)
        }

        try {
            Glide.with(mContext).load(newsModel.attachments[0].photo!!.sizes[4].url)
                .into(view?.findViewById(R.id.news_image) as ImageView)
            (view.findViewById(R.id.news_description) as TextView).text = newsModel.text
            (view.findViewById(R.id.news_header) as TextView).text = "СКБ КИТ"

            val sdf = SimpleDateFormat("dd.MM hh:mm")
            val date = Date((newsModel.date as Long) * 1000)
            (view.findViewById(R.id.dateText) as TextView).text = sdf.format(date)
        }catch (e: java.lang.Exception){

        }


        return view
    }

}