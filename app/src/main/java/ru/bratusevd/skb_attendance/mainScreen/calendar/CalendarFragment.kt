package ru.bratusevd.skb_attendance.mainScreen.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.adapters.StoryAdapter
import ru.bratusevd.skb_attendance.mainScreen.models.TimeModel
import ru.bratusevd.skb_attendance.mainScreen.models.UserModel
import ru.bratusevd.skb_attendance.models.TokenModel
import ru.bratusevd.skb_attendance.services.network.NetworkServices


class CalendarFragment : Fragment() {

    private var root: View? = null
    private lateinit var tokenModel: TokenModel
    private val APP_PREFERENCES = "visit"
    private lateinit var storyList: ListView
    private lateinit var filter_spinner: Spinner
    private lateinit var barChart: BarChart
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_calendar, container, false)
        tokenModel = arguments?.getSerializable("tokenModel") as TokenModel
        findViews()
        return root
    }

    private fun updateInfo() {
        NetworkServices.getInstance().jsonApi.getUserInfo(
            tokenModel.getAccess(),
            tokenModel.getId()
        ).enqueue(object :
            Callback<UserModel> {
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) {
                if (response.isSuccessful) {
                    tokenModel.setVisits(response.body()?.user!!.visits)
                    setAdapter()
                    mSwipeRefreshLayout.isRefreshing = false
                } else
                    Toast.makeText(
                        context,
                        response.message() + " " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun findViews() {
        storyList = root!!.findViewById(R.id.accountFragment_storyList)
        barChart = root!!.findViewById(R.id.barChart)
        filter_spinner = root!!.findViewById(R.id.filter_spinner)
        mSwipeRefreshLayout = root!!.findViewById(R.id.calendarFragment)
        mSwipeRefreshLayout.setOnRefreshListener {
            updateInfo()
        }
        setAdapter()
    }

    private fun setAdapter() {
        filter_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                storyList.adapter = StoryAdapter(requireContext(), visitFilter(fillArray()))
                setBarChart()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }

    private fun setBarChart() {
        val timeModels: ArrayList<TimeModel> = visitFilter(fillArray())
        barChart.data = refactorArray(timeModels)
        barChart.setDescription("")
        barChart.animateY(3000)
    }

    private fun visitFilter(
        timeModels: ArrayList<TimeModel>,
    ): ArrayList<TimeModel> {
        when (filter_spinner.selectedItem.toString()) {
            "7 дней" -> {
                return last7Days(timeModels)
            }
            "30 дней" -> {
                return last30Days(timeModels)
            }
            "Текущий месяц" -> {
                return month(timeModels, "11")
            }
        }

        return timeModels
    }

    private fun last7Days(timeModels: ArrayList<TimeModel>): ArrayList<TimeModel> {
        val array: ArrayList<TimeModel> = ArrayList()
        if (timeModels.size <= 7) return timeModels
        array.add(
            TimeModel(
                timeModels[0].getStartTime(),
                timeModels[0].getDate(),
                timeModels[0].getEndTime()
            )
        )
        var count = 1

        for (i in 1 until timeModels.size) {
            if (count == 7) return array
            array.add(
                TimeModel(
                    timeModels[i].getStartTime(),
                    timeModels[i].getDate(),
                    timeModels[i].getEndTime()
                )
            )
            if (timeModels[i].getDate() != timeModels[i - 1].getDate()) count++
        }
        return array
    }

    private fun month(timeModels: ArrayList<TimeModel>, monthNum: String): ArrayList<TimeModel> {
        val array: ArrayList<TimeModel> = ArrayList()

        timeModels.forEach {
            if (it.getDate().split(".")[1] == monthNum)
                array.add(TimeModel(it.getStartTime(), it.getDate(), it.getEndTime()))
        }
        return array
    }

    private fun refactorArray(timeModels: ArrayList<TimeModel>): BarData {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        var count = 0
        var tmp = try {
            resTimeToHours(calcResTime(timeModels[0].getEndTime(), timeModels[0].getStartTime()))
        } catch (e: Exception) {
            0f
        }

        for (i in 1 until timeModels.size) {
            if (timeModels[i].getDate() != timeModels[i - 1].getDate()) {
                entries.add(BarEntry(tmp, count))
                labels.add(timeModels[i - 1].getDate())
                count++
                tmp = resTimeToHours(
                    calcResTime(
                        timeModels[i].getEndTime(),
                        timeModels[i].getStartTime()
                    )
                )
            } else {
                tmp += resTimeToHours(
                    calcResTime(
                        timeModels[i].getEndTime(),
                        timeModels[i].getStartTime()
                    )
                )
            }
        }

        if (timeModels.size > 1 && count == 0) {
            entries.add(BarEntry(tmp, count))
            labels.add(timeModels[0].getDate())
        }

        if (timeModels.size == 1) {
            entries.add(BarEntry(tmp, 0))
            labels.add(timeModels[0].getDate())
        }

        try {
            if (timeModels[timeModels.size - 1].getDate() != timeModels[timeModels.size - 2].getDate()) {
                entries.add(BarEntry(tmp, count))
                labels.add(timeModels[timeModels.size - 1].getDate())
            }
        } catch (e: Exception) {
        }
        val barDataSet = BarDataSet(entries, "Hours")
        barDataSet.color = resources.getColor(R.color.progressbar_leftGradient)
        return BarData(labels, barDataSet)
    }

    private fun last30Days(timeModels: ArrayList<TimeModel>): ArrayList<TimeModel> {
        val array: ArrayList<TimeModel> = ArrayList()
        if (timeModels.size <= 30) return timeModels
        array.add(
            TimeModel(
                timeModels[0].getStartTime(),
                timeModels[0].getDate(),
                timeModels[0].getEndTime()
            )
        )
        var count = 1

        for (i in 1 until timeModels.size) {
            if (count == 30) return array
            array.add(
                TimeModel(
                    timeModels[i].getStartTime(),
                    timeModels[i].getDate(),
                    timeModels[i].getEndTime()
                )
            )
            if (timeModels[i].getDate() != timeModels[i - 1].getDate()) count++
        }
        return array
    }

    private fun fillArray(): ArrayList<TimeModel> {
        val timeModels: ArrayList<TimeModel> = ArrayList()
        try {
            timeModels.addAll(tokenModel.getVisits())
            timeModels.reverse()
        } catch (e: Exception) {
        }

        return timeModels
    }

    private fun calcResTime(curTime: String, startTime: String): Int {
        val hours =
            curTime.split(":".toRegex()).toTypedArray()[0].toInt() - startTime.split(":".toRegex())
                .toTypedArray()[0].toInt()
        var minutes =
            curTime.split(":".toRegex()).toTypedArray()[1].toInt() - startTime.split(":".toRegex())
                .toTypedArray()[1].toInt()
        minutes += hours * 60
        return minutes
    }

    private fun resTimeToHours(time: Int): Float {
        val res: Float
        val hours: Int = time / 60
        val minutes: Int = time % 60
        res = (hours + (minutes.toFloat() / 60))
        return res
    }
}