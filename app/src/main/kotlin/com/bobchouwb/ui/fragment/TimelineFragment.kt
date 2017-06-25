package com.bobchouwb.ui.fragment

import com.bobchouwb.R
import com.bobchouwb.base.ui.fragment.AbstractTableViewFragment
import com.bobchouwb.data.Classify
import com.bobchouwb.ui.adapter.ClassifyAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by zhoubo on 2017/6/25.
 */
class TimelineFragment : AbstractTableViewFragment() {

    fun initData(): List<Classify> {

        var list = mutableListOf<Classify>()

        for (index in 0..10) {

            list.add(Classify("$index", "序号：$index"))
        }

        return list
    }

    override fun configRecyclerViewAdapter(): BaseQuickAdapter<Classify, BaseViewHolder> = ClassifyAdapter(R.layout.menu_classify_item_lay, initData())

    override fun inflateToobarView(): Int = R.layout.comm_ui_toolbar_lay
}