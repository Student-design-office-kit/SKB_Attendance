package ru.bratusevd.skb_attendance.mainScreen.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.adapters.NewsAdapter
import ru.bratusevd.skb_attendance.mainScreen.models.NewsModel


class NewsFragment : Fragment() {

    private var root: View? = null
    private lateinit var listView: ListView

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

    private fun setAdapter() {
        listView.adapter = NewsAdapter(requireContext(), getNews())
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                val promptsView = View.inflate(context, R.layout.fragment_news_item, null)
                val newsModel: NewsModel = getNews()[p2]
                promptsView.findViewById<TextView>(R.id.news_fragment_dateText).text =
                    newsModel.getDataText()
                promptsView.findViewById<TextView>(R.id.news_fragment_description).text =
                    newsModel.getDescription()
                promptsView.findViewById<TextView>(R.id.news_fragment_header).text =
                    newsModel.getHeader()
                Glide.with(requireContext()).load(newsModel.getImage()).into(
                    promptsView.findViewById(R.id.news_fragment_image)
                )
                val alertDialog = MaterialAlertDialogBuilder(requireContext())
                    .setView(promptsView)
                    .create()

                alertDialog.show()
            }
    }

    private fun getNews(): ArrayList<NewsModel> {
        val newsModels: ArrayList<NewsModel> = ArrayList()

        newsModels.add(
            NewsModel(
                "https://s0.rbk.ru/v6_top_pics/media/img/0/97/756654223631970.jpg",
                "Крымский мост ещё целый",
                "Лаборатория мобильной разработки",
                "Вчера"
            )
        )
        newsModels.add(
            NewsModel(
                "https://mtdata.ru/u24/photo0102/20903724875-0/original.jpg",
                "Крымский мост ещё целый",
                "Лаборатория мобильной разработки",
                "Вчера"
            )
        )
        newsModels.add(
            NewsModel(
                "https://travel-or-die.ru/wp-content/uploads/2021/08/chto-vy-uvidite-vo-vremya-ekskursii-na-Krymskij-most-Krymskij-most.jpg",
                "Крымский мост ещё целый",
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
                "https://s.poembook.ru/theme/ff/81/f7/2c72489a34437b11ee4392aff1b5dc3c76738a3c.jpeg",
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