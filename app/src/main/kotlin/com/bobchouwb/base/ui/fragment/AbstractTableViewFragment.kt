package com.bobchouwb.base.ui.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.bobchouwb.R
import com.bobchouwb.utils.inflate
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.comm_menu_header.*
import kotlinx.android.synthetic.main.comm_ui_recyclerview_lay.*
import kotlinx.android.synthetic.main.comm_ui_recyclerview_swiperefresh_lay.*
import kotlinx.android.synthetic.main.comm_ui_tableview_lay.*

/**
 * Created by zhoubo on 2017/6/11.
 */
abstract class AbstractTableViewFragment : AbstractRefreshRecyclerViewFragment() {

    val TABLEVIEW_LIST_TYPE: Int = 0 // 列表类型
    val TABLEVIEW_GRID_TYPE: Int = 1 // 宫格类型

    var tableView_grid_default_spanCount = 2 // 宫格默认列数
    var mIsCanRefresh: Boolean = false // 是否支持刷新
    var mIsCanLoadMore: Boolean = false // 是否可以加载更多

    var recyclerViewHeaderVew: View? = null
    var fragmentHeaderView: View? = null

    override fun inflateContentView(): Int = R.layout.comm_ui_tableview_lay

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addFragmentHeaderView(inflateFragmentHeaderView())
        addRecyclerViewHeaderView(inflateRecyclerViewHeaderView())

        swipeRefreshLayout.isEnabled = canRefresh()
        getAdapter().setEnableLoadMore(canLoadMore())
    }

    open fun inflateFragmentHeaderView(): Int = -1
    open fun inflateRecyclerViewHeaderView(): Int = -1

    private fun addFragmentHeaderView(layoutRes: Int) {
        if (layoutRes == -1) return
        tableView_top_container.inflate(layoutRes, tableView_top_container, true)
        fragmentHeaderView = menu_header_layout
    }

    private fun addRecyclerViewHeaderView(layoutRes: Int){
        if (layoutRes == -1) return
        if (recycler_view.adapter is BaseQuickAdapter<*, *>) {
            recyclerViewHeaderVew = recycler_view.inflate(layoutRes, recycler_view)
            (recycler_view.adapter as BaseQuickAdapter<*, *>).addHeaderView(recycler_view.inflate(layoutRes, recycler_view))
        }
    }

    override fun configRecyclerViewLayoutManager(): LinearLayoutManager =

        when(tableViewDisplayType()) {
            TABLEVIEW_LIST_TYPE -> LinearLayoutManager(activity)
            TABLEVIEW_GRID_TYPE -> GridLayoutManager(activity, tableViewGridSpanCount())
            else -> LinearLayoutManager(activity)
        }

    fun tableViewGridSpanCount(): Int = tableView_grid_default_spanCount

    open fun tableViewDisplayType(): Int = TABLEVIEW_LIST_TYPE

    fun canRefresh(): Boolean = mIsCanRefresh
    fun canLoadMore(): Boolean = mIsCanLoadMore
}