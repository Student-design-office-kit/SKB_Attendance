package ru.bratusevd.skb_attendance.mainScreen.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Spinner
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
    private lateinit var filter_spinner: Spinner
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
        barChart = root!!.findViewById(R.id.barChart)
        filter_spinner = root!!.findViewById(R.id.filter_spinner)
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

        var count = 0;
        timeModels.forEach {
            if (count == 7) return array
            array.add(TimeModel(it.getStartTime(), it.getDate(), it.getEndTime()))
            count++
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
        var entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        var tmpTime: Float =
            resTimeToHours(calcResTime(timeModels[0].getEndTime(), timeModels[0].getEndTime()))
        var count = 0;

        for (i in 1 until timeModels.size) {
            if (timeModels[i].getDate() == timeModels[i - 1].getDate()) {
                tmpTime += resTimeToHours(
                    calcResTime(
                        timeModels[i].getEndTime(),
                        timeModels[i].getStartTime()
                    )
                )
            } else {
                entries.add(BarEntry(tmpTime, count))
                labels.add(timeModels[i].getDate().split(".202")[0])
                tmpTime = resTimeToHours(
                    calcResTime(
                        timeModels[i].getEndTime(),
                        timeModels[i].getStartTime()
                    )
                )
                count++
            }
        }

        val barDataSet = BarDataSet(entries, "Hours")
        barDataSet.color = resources.getColor(R.color.progressbar_leftGradient)
        return BarData(labels, barDataSet)
    }

    private fun last30Days(timeModels: ArrayList<TimeModel>): ArrayList<TimeModel> {
        val array: ArrayList<TimeModel> = ArrayList()
        if (timeModels.size <= 30) return timeModels

        var count = 0;
        timeModels.forEach {
            if (count == 30) return array
            array.add(TimeModel(it.getStartTime(), it.getDate(), it.getEndTime()))
            count++
        }
        return array
    }

    private fun fillArray(): ArrayList<TimeModel> {
        //val timeModels: ArrayList<TimeModel> = tokenModel.getVisits()
        val timeModels: ArrayList<TimeModel> = ArrayList()
        timeModels.add(TimeModel("10:35", "01.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "01.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "02.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "03.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "04.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "05.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "06.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "07.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "08.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "09.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "10.05.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "01.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "01.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "02.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "03.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "04.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "05.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "06.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "07.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "08.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "09.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "10.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "11.09.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "01.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "02.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "03.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "04.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "05.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "06.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "07.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "08.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "09.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "10.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "11.11.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "01.12.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "02.12.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "03.12.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "04.12.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "05.12.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "06.12.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "07.12.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "08.12.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "09.12.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "10.12.2022", "11:45"))
        timeModels.add(TimeModel("10:35", "11.12.2022", "11:45"))
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
        res = (hours + (minutes.toFloat() / 60))
        return res
    }

}