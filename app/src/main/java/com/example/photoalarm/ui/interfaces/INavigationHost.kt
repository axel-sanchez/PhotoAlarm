package com.example.photoalarm.ui.interfaces

import androidx.fragment.app.Fragment

interface INavigationHost {

    fun navigateTo(fragment: Fragment, addToBackstack: Boolean)

    fun replaceTo(fragment: Fragment, addToBackstack: Boolean)

    fun finish()
}
