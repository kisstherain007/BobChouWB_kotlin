package com.bobchouwb.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.bobchouwb.R
import com.bobchouwb.base.ui.fragment.AbstractTableViewFragment
import com.bobchouwb.data.Classify
import com.bobchouwb.data.user.UserInfoBean
import com.bobchouwb.databinding.CommMenuHeaderBinding
import com.bobchouwb.ui.adapter.ClassifyAdapter
import com.bobchouwb.utils.getAccessToken
import com.bobchouwb.utils.net.RetrofitManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by zhoubo on 2017/6/10.
 */
class MenuFragment : AbstractTableViewFragment() {

    var headerDataBinding: CommMenuHeaderBinding? = null

    override fun inflateFragmentHeaderView(): Int = R.layout.comm_menu_header

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserHeader()
    }

    fun initUserHeader() {
        headerDataBinding = DataBindingUtil.bind<CommMenuHeaderBinding>(fragmentHeaderView)
        getData()
    }

    fun initData(): List<Classify> {

        var list = mutableListOf<Classify>()

        for (index in 0..10) {

            list.add(Classify("$index", "序号：$index"))
        }

        return list
    }

    override fun configRecyclerViewAdapter(): BaseQuickAdapter<Classify, BaseViewHolder> = ClassifyAdapter(R.layout.menu_classify_item_lay, initData())

        fun getData() {

        var call = RetrofitManager.builder().service!!.showUsers(activity.getAccessToken()!!.token, activity.getAccessToken()!!.uid)

        call.enqueue(object : Callback<UserInfoBean> {

            override fun onResponse(call: Call<UserInfoBean>?, response: Response<UserInfoBean>?) {
                Log.d("ktr", response!!.body()!!.name)
                Log.d("ktr", response!!.body()!!.avatar_large)
                headerDataBinding!!.user = response!!.body()!!
            }

            override fun onFailure(call: Call<UserInfoBean>?, t: Throwable?) {
                Log.d("ktr", "onFailure $t")
            }
        })
    }
}