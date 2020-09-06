package com.example.photoalarm.ui.customs

import android.view.View
import com.example.photoalarm.ui.interfaces.IOnBackPressFragment

abstract class PhotoAlarmFragment : androidx.fragment.app.Fragment() , IOnBackPressFragment{
    //EXTENSION FUNCTIONS
    fun View.showView(show: Boolean){
        if(show) this.visibility = View.VISIBLE
        else this.visibility = View.GONE
    }
}