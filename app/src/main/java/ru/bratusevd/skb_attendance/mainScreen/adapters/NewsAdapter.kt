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

class NewsAdapter(context: Context, newsList: ArrayList<NewsModel>) : BaseAdapter() {

    private val mContext: Context = context
    private val mNewsList: ArrayList<NewsModel> = newsList

    override fun getCount(): Int {
        return mNewsList.size
    }

    override fun getItem(position: Int): NewsModel {
        return mNewsList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var view: View? = convertView
        val newsModel: NewsModel = getItem(position)
        val mInflater = LayoutInflater.from(mContext)
        if (view == null) {
            view = mInflater.inflate(R.layout.news_item, null)
        }

        Glide.with(mContext).load(newsModel.getImage())
            .into(view?.findViewById(R.id.news_image) as ImageView)
        (view.findViewById(R.id.news_description) as TextView).text = newsModel.getDescription()
        (view.findViewById(R.id.news_header) as TextView).text = newsModel.getHeader()
        (view.findViewById(R.id.dateText) as TextView).text = newsModel.getDataText()


        return view
    }

}