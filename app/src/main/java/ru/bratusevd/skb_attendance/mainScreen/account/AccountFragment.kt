package ru.bratusevd.skb_attendance.mainScreen.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.account.utils.QR_Scanner
import ru.bratusevd.skb_attendance.mainScreen.adapters.StoryAdapter
import ru.bratusevd.skb_attendance.mainScreen.models.TimeModel

class AccountFragment : Fragment() {

    private var root: View? = null

    private lateinit var qrScanner: ImageView
    private lateinit var userImage: ImageView
    private lateinit var userName: TextView
    private lateinit var storyList: ListView

    private val SCANNER_REQUEST = 1

    fun AccountFragment() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_account, container, false)
        findViews()
        return root
    }

    private fun findViews() {
        qrScanner = root?.findViewById(R.id.accountFragment_scanner)!!
        userImage = root!!.findViewById(R.id.accountFragment_userImage)
        userName = root!!.findViewById(R.id.accountFragment_userName)
        storyList = root!!.findViewById(R.id.accountFragment_storyList)

        setAdapter()
        setOnClick()
    }

    private fun setAdapter() {
        storyList.adapter = StoryAdapter(requireContext(), fillArray())
    }

    private fun fillArray(): ArrayList<TimeModel> {
        val timeModels: ArrayList<TimeModel> = ArrayList()
        timeModels.add(TimeModel("10:20", "04.03.22"))
        timeModels.add(TimeModel("12:20", "04.03.22"))
        timeModels.add(TimeModel("13:20", "04.03.22"))
        timeModels.add(TimeModel("11:20", "04.03.22"))
        timeModels.add(TimeModel("9:20", "04.03.22"))
        timeModels.add(TimeModel("7:20", "04.03.22"))
        timeModels.add(TimeModel("9:20", "04.03.22"))
        timeModels.add(TimeModel("8:20", "04.03.22"))
        timeModels.add(TimeModel("11:20", "04.03.22"))
        timeModels.add(TimeModel("10:20", "04.03.22"))
        timeModels.add(TimeModel("13:20", "04.03.22"))
        timeModels.add(TimeModel("14:20", "04.03.22"))
        timeModels.add(TimeModel("10:20", "04.03.22"))
        timeModels.add(TimeModel("9:20", "04.03.22"))
        timeModels.add(TimeModel("12:20", "04.03.22"))
        return timeModels
    }

    private fun setOnClick() {
        onScannerClick()
        onUserImageClick()
    }

    private fun onUserImageClick() {

    }

    private fun onScannerClick() {
        qrScanner.setOnClickListener {
            startActivityForResult(
                Intent(
                    context,
                    QR_Scanner::class.java
                ), SCANNER_REQUEST
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 3) {
            val link = data!!.getStringExtra("link")
            checkIn(link.toString())
        }
    }

    private fun checkIn(link: String) {

    }
}