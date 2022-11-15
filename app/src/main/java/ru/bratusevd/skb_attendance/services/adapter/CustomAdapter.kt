package ru.bratusevd.skb_attendance.services.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.bratusevd.skb_attendance.R

class CustomAdapter(context: Context, var custom_layout_id: Int, var items_list: List<String>) :
    ArrayAdapter<String?>(context, custom_layout_id, items_list) {
    override fun getCount(): Int {
        return items_list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        if (v == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(custom_layout_id, null)
        }

        val imageView = v!!.findViewById<ImageView>(R.id.item_avatar)
        val item = items_list[position]
        Glide.with(context)
            .load(item)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
        return v
    }
}