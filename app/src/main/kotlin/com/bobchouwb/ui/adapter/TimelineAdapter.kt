package com.bobchouwb.ui.adapter

import com.bobchouwb.R
import com.bobchouwb.data.Classify
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by zhoubo on 2017/6/25.
 */
class TimelineAdapter : BaseQuickAdapter<Classify, BaseViewHolder> {

    constructor(layoutResId: Int, data: List<Classify>) : super(layoutResId, data)

    override fun convert(helper: BaseViewHolder?, item: Classify?) {

        helper!!.setText(R.id.classify_title_textView, item!!.title)
    }
}