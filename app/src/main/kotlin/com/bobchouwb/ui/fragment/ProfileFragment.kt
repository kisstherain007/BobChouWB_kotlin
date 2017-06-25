package com.bobchouwb.ui.fragment

import android.app.Fragment
import com.bobchouwb.R
import com.bobchouwb.base.ui.fragment.AbstractRefreshViewFragment

/**
 * Created by zhoubo on 2017/6/10.
 */
class ProfileFragment : AbstractRefreshViewFragment() {

    companion object {

        fun newInstance(): Fragment = ProfileFragment()
    }

    override fun inflateRefreshChildView(): Int = R.layout.comm_ui_test_lay
}