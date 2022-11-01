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
        storyList.adapter = StoryAdapter(requireContext(), visitFilter(fillArray(), 3))
        setBarChart()
    }

    private fun setBarChart() {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        val timeModels: ArrayList<TimeModel> = visitFilter(fillArray(),2)
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

    private fun visitFilter(timeModels: ArrayList<TimeModel>, filterType: Int): ArrayList<TimeModel> {
        when(filterType){
            1->{
                return last7Days(timeModels)
            }
            2->{
                return last30Days(timeModels)
            }
            3->{
                return month(timeModels, "11")
            }
        }

        return timeModels
    }

    private fun last7Days(timeModels: ArrayList<TimeModel>): ArrayList<TimeModel> {
        val array: ArrayList<TimeModel> = ArrayList()
        if(timeModels.size <= 7) return timeModels

        var count = 0;
        timeModels.forEach{
            if(count == 7) return array
            array.add(TimeModel(it.getStartTime(), it.getDate(), it.getEndTime()))
            count++
        }
        return array
    }

    private fun month(timeModels: ArrayList<TimeModel>, monthNum: String): ArrayList<TimeModel> {
        val array: ArrayList<TimeModel> = ArrayList()

        var count = 0;
        timeModels.forEach{
            if(it.getDate().split(".")[1] == monthNum)
            array.add(TimeModel(it.getStartTime(), it.getDate(), it.getEndTime()))
            count++
        }
        return array
    }

    private fun last30Days(timeModels: ArrayList<TimeModel>): ArrayList<TimeModel> {
        val array: ArrayList<TimeModel> = ArrayList()
        if(timeModels.size <= 30) return timeModels

        var count = 0;
        timeModels.forEach{
            if(count == 30) return array
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
        res = (hours + (minutes.toFloat()/60))
        return res
    }

}