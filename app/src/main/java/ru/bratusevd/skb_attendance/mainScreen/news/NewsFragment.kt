package ru.bratusevd.skb_attendance.mainScreen.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.adapters.NewsAdapter
import ru.bratusevd.skb_attendance.mainScreen.models.NewsRequest
import ru.bratusevd.skb_attendance.mainScreen.models.NewsResponse
import ru.bratusevd.skb_attendance.models.TokenModel
import ru.bratusevd.skb_attendance.services.network.NetworkServices
import java.util.Collections.swap


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
        getNews()
        return root
    }

    private fun getNews() {
        NetworkServices.getInstance().jsonApi.getNews(NewsRequest(177747188, 100)).enqueue(
            object : Callback<ArrayList<NewsResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<NewsResponse>>,
                    response: Response<ArrayList<NewsResponse>>
                ) {
                    if (response.code() in 200..299) {
                        val newsList = response.body() as ArrayList<NewsResponse>
                        listView.adapter = NewsAdapter(
                            requireContext(),
                            newsList
                        )
                    }
                }

                override fun onFailure(call: Call<ArrayList<NewsResponse>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }
}