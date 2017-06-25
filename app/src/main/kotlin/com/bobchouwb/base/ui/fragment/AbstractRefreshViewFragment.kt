package com.bobchouwb.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bobchouwb.R
import com.bobchouwb.utils.inflate
import kotlinx.android.synthetic.main.comm_uiswiperefresh_lay.*

/**
 * Created by zhoubo on 2017/6/10.
 */
abstract class AbstractRefreshViewFragment : BaseFragment() {

    override fun inflateContentView(): Int = R.layout.comm_uiswiperefresh_lay

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefresh_layout.inflate(inflateRefreshChildView(), swipeRefresh_layout, true)
    }

    abstract fun inflateRefreshChildView(): Int
}