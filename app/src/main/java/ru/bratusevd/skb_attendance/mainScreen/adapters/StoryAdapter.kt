package ru.bratusevd.skb_attendance.mainScreen.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ProgressBar
import android.widget.TextView
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.models.TimeModel
import java.util.*


class StoryAdapter(context: Context, timeList: ArrayList<TimeModel>) : BaseAdapter()  {

    private val mContext: Context = context
    private val mTimeList: ArrayList<TimeModel> = timeList

    override fun getCount(): Int {
        return mTimeList.size
    }

    override fun getItem(position: Int): TimeModel {
        return mTimeList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var view: View? = convertView
        val timeModel: TimeModel = getItem(position)

        val currentTime: Date = Calendar.getInstance().time
        //var curTime: String = currentTime.hours.toString() + ":" + currentTime.minutes
        /*if(timeModel.getEndTime() != null)*/var curTime = timeModel.getEndTime()
        val mInflater = LayoutInflater.from(mContext)
        if (view == null) {
            view = mInflater.inflate(R.layout.story_item, null)
        }
        (view!!.findViewById(R.id.storyItem_progressBar) as ProgressBar).progress =
            calcResTime(curTime, timeModel.getStartTime())
        (view.findViewById(R.id.storyItem_date) as TextView).text = timeModel.getDate()

        return view
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

}