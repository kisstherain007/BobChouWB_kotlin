package com.bobchouwb.base.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bobchouwb.R
import com.bobchouwb.utils.inflate

/**
 * Created by zhoubo on 2017/6/10.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentView = inflater!!.inflate(R.layout.comm_ui_fragment_base_lay, null) as ViewGroup
        if (inflateToobarView() != -1) parentView.inflate(inflateToobarView(), parentView, true)
        parentView.inflate(inflateContentView(), parentView, true)
        return parentView
    }

    abstract fun inflateContentView(): Int

    open fun inflateToobarView(): Int = -1
}