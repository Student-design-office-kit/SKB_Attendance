package ru.bratusevd.skb_attendance.mainScreen.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.adapters.NewsAdapter
import ru.bratusevd.skb_attendance.mainScreen.models.NewsModel


class NewsFragment : Fragment() {

    private var root: View? = null
    private lateinit var listView: ListView

    fun NewsFragment() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_news, container, false)
        listView = root?.findViewById(R.id.news_listView) as ListView
        setAdapter()
        return root
    }

    private fun setAdapter(){
        listView.adapter = NewsAdapter(requireContext(), getNews())
    }

    private fun getNews(): ArrayList<NewsModel>{
        val newsModels: ArrayList<NewsModel> = ArrayList()

        newsModels.add(
            NewsModel(
                "https://avatars.mds.yandex.net/i?id=72b92e65a2d34a17cef9fd326d88557c-4611905-images-thumbs&n=13",
                "Произошло, что-то невероятное",
                "Лаборатория мобильной разработки",
                "Вчера"
            )
        )
        newsModels.add(
            NewsModel(
                "https://avatars.mds.yandex.net/i?id=72b92e65a2d34a17cef9fd326d88557c-4611905-images-thumbs&n=13",
                "Произошло, что-то невероятное",
                "Лаборатория мобильной разработки",
                "Вчера"
            )
        )
        newsModels.add(
            NewsModel(
                "https://avatars.mds.yandex.net/i?id=72b92e65a2d34a17cef9fd326d88557c-4611905-images-thumbs&n=13",
                "Произошло, что-то невероятное",
                "Лаборатория мобильной разработки",
                "Вчера"
            )
        )
        newsModels.add(
            NewsModel(
                "https://avatars.mds.yandex.net/i?id=72b92e65a2d34a17cef9fd326d88557c-4611905-images-thumbs&n=13",
                "Произошло, что-то невероятное",
                "Лаборатория мобильной разработки",
                "Вчера"
            )
        )
        newsModels.add(
            NewsModel(
                "https://avatars.mds.yandex.net/i?id=72b92e65a2d34a17cef9fd326d88557c-4611905-images-thumbs&n=13",
                "Произошло, что-то невероятное",
                "Лаборатория мобильной разработки",
                "Вчера"
            )
        )
        newsModels.add(
            NewsModel(
                "https://avatars.mds.yandex.net/i?id=72b92e65a2d34a17cef9fd326d88557c-4611905-images-thumbs&n=13",
                "Произошло, что-то невероятное",
                "Лаборатория мобильной разработки",
                "Вчера"
            )
        )
        return newsModels
    }
}