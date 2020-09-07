package com.example.photoalarm.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.photoalarm.R
import com.example.photoalarm.ui.customs.PhotoAlarmFragment
import com.example.photoalarm.ui.interfaces.INavigationHost

class MainActivity: AppCompatActivity(), INavigationHost {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {

            var fragment: Fragment = MainFragment()

            replaceTo(fragment, false)
        }
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment)
        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun replaceTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun finish() {
        supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container) as PhotoAlarmFragment

        if (f.childFragmentManager.backStackEntryCount > 1) {
            f.childFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}