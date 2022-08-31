package ru.bratusevd.skb_attendance.mainScreen.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.bratusevd.skb_attendance.R


class AccountFragment : Fragment() {

    private var root: View? = null

    fun AccountFragment() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_account, container, false)
        return root
    }
}