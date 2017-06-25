package com.bobchouwb.base.ui.fragment

import android.os.Build
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bobchouwb.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.comm_ui_recyclerview_lay.*

/**
 * Created by zhoubo on 2017/6/10.
 */
abstract class AbstractRecyclerViewFragment : BaseFragment() {

    override fun inflateContentView(): Int = R.layout.comm_ui_recyclerview_lay

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecyclerView()
    }

    fun configRecyclerView() {

        recycler_view!!.setHasFixedSize(true)
        recycler_view!!.adapter = configRecyclerViewAdapter()
        recycler_view!!.layoutManager = configRecyclerViewLayoutManager()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            recycler_view.addItemDecoration(configDividerItemDecoration()?: DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        else
            recycler_view.addItemDecoration(configDividerItemDecoration()?: DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    abstract fun configRecyclerViewAdapter(): BaseQuickAdapter<*, *> // 适配器
    abstract fun configRecyclerViewLayoutManager(): LinearLayoutManager // 布局样式
    fun configDividerItemDecoration(): RecyclerView.ItemDecoration? = null // 分割线

    fun getAdapter(): BaseQuickAdapter<*, *> = recycler_view.adapter as BaseQuickAdapter<*, *>
}