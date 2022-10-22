package ru.bratusevd.skb_attendance.mainScreen

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.account.AccountFragment
import ru.bratusevd.skb_attendance.mainScreen.calendar.CalendarFragment
import ru.bratusevd.skb_attendance.mainScreen.news.NewsFragment
import ru.bratusevd.skb_attendance.models.TokenModel

class MainScreenActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        bottomNavigationView = findViewById(R.id.mainScreenActivity_NavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@MainScreenActivity)
        bottomNavigationView.selectedItemId = R.id.navigation_news

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_news -> {
                val fragment = NewsFragment()
                fragment.arguments = passData()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainScreenActivity_Container, fragment)
                    .addToBackStack("").commit()
                return true
            }
            R.id.navigation_calendar -> {
                val fragment = CalendarFragment()
                fragment.arguments = passData()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainScreenActivity_Container, fragment)
                    .addToBackStack("").commit()
                return true
            }
            R.id.navigation_account -> {
                val fragment = AccountFragment()
                fragment.arguments = passData()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainScreenActivity_Container, fragment)
                    .addToBackStack("").commit()
                return true
            }
        }
        return false
    }

    private fun passData(): Bundle{
        val bundle = Bundle()
        bundle.putSerializable("tokenModel", intent.getSerializableExtra("userInfo"))
        return bundle
    }

    override fun onBackPressed() {

    }
}