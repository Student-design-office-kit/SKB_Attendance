package ru.bratusevd.skb_attendance.mainScreen

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import ru.bratusevd.skb_attendance.R
import ru.bratusevd.skb_attendance.mainScreen.account.AccountFragment
import ru.bratusevd.skb_attendance.mainScreen.calendar.CalendarFragment
import ru.bratusevd.skb_attendance.mainScreen.news.NewsFragment

class MainScreenActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawer: DrawerLayout

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this@MainScreenActivity)

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

            R.id.item_drawer_logout ->{
                drawer.closeDrawer(GravityCompat.START)
                finish()
                return true
            }
            R.id.item_drawer_profile ->{
                val fragment = AccountFragment()
                fragment.arguments = passData()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainScreenActivity_Container, fragment)
                    .addToBackStack("").commit()
                drawer.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.item_drawer_settings ->{
                Toast.makeText(applicationContext, "Этот пункт ещё в разработке", Toast.LENGTH_SHORT).show()
                drawer.closeDrawer(GravityCompat.START)
                return true
            }
        }
        return false
    }

    private fun passData(): Bundle {
        val bundle = Bundle()
        bundle.putSerializable("tokenModel", intent.getSerializableExtra("userInfo"))
        return bundle
    }

    override fun onBackPressed() {

    }
}