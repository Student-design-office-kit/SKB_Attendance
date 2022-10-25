package ru.bratusevd.skb_attendance.mainScreen.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.adapters.StoryAdapter
import ru.bratusevd.skb_attendance.mainScreen.models.TimeModel
import ru.bratusevd.skb_attendance.models.TokenModel

class CalendarFragment : Fragment() {

    private var root: View? = null
    private lateinit var tokenModel: TokenModel
    private val APP_PREFERENCES = "visit"
    private lateinit var storyList: ListView
    private lateinit var barChart: BarChart

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

    private fun findViews() {
        storyList = root!!.findViewById(R.id.accountFragment_storyList)
        barChart= root!!.findViewById(R.id.barChart)
        setAdapter()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAdapter() {
        storyList.adapter = StoryAdapter(requireContext(), fillArray())
        setBarChart()
    }

    private fun setBarChart() {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        val timeModels: ArrayList<TimeModel> = fillArray()
        var i = 0;
        timeModels.forEach{
            entries.add(BarEntry(resTimeToHours(calcResTime(it.getEndTime(), it.getStartTime())), i))
            labels.add(it.getDate().split(".202")[0])
            i++
        }

        val barDataSet = BarDataSet(entries, "Hours")
        val data = BarData(labels, barDataSet)
        barChart.data = data
        barChart.setDescription("")
        barDataSet.color = resources.getColor(R.color.progressbar_leftGradient)
        barChart.animateY(3000)
    }

    private fun fillArray(): ArrayList<TimeModel> {
        val timeModels: ArrayList<TimeModel> = tokenModel.getVisits()
        timeModels.reverse()
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
        var res: Float
        val hours: Int = time / 60
        val minutes: Int = time % 60
        res = (hours + (minutes.toFloat()/60))
        return res
    }

}