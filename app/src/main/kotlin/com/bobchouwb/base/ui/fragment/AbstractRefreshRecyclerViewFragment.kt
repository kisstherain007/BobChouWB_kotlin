package com.bobchouwb.base.ui.fragment

import com.bobchouwb.R

/**
 * Created by zhoubo on 2017/6/10.
 */
abstract class AbstractRefreshRecyclerViewFragment : AbstractRecyclerViewFragment() {

    override fun inflateContentView(): Int = R.layout.comm_ui_recyclerview_swiperefresh_lay
}