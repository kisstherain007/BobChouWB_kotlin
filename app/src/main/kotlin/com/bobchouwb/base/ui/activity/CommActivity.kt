package com.bobchouwb.base.ui.activity

import android.app.Fragment
import android.os.Bundle
import com.bobchouwb.R

/**
 * Created by zhoubo on 2017/6/10.
 */
class CommActivity : BaseActivity() {

    var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//    fun addFragment(fragment : Fragment) = fragmentManager.beginTransaction().add(R.id.layout_container, fragment!!).commit()
//
//    fun removeFragment(fragment: Fragment) = fragmentManager.beginTransaction().remove(fragment!!)

    override fun onDestroy() {
        super.onDestroy()
//        removeFragment(currentFragment!!)
    }
}