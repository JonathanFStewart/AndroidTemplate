package com.jonathanstewart.androidtemplate.test.base

import androidx.fragment.app.Fragment

abstract class FragmentTest<F : Fragment>(private val fragmentClass: Class<F>) :
    ActivityTest<TestActivity>(activityClass = TestActivity::class.java) {

    var fragment: F? = null

    fun startFragment() {
        if (fragment == null) {
            fragment = fragmentClass.newInstance()
            activity.supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment as Fragment).commitAllowingStateLoss()

            waitForUiThread()
        }
    }
}
