package com.bobchouwb.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bobchouwb.R
import com.bobchouwb.base.ui.activity.BaseActivity
import com.bobchouwb.data.user.UserInfoBean
import com.bobchouwb.databinding.CommMenuHeaderBinding
import com.bobchouwb.utils.getAccessToken
import com.bobchouwb.utils.net.RetrofitManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.comm_menu_header.*
import kotlinx.android.synthetic.main.comm_menu_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    var headerDataBinding: CommMenuHeaderBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUserHeader()
    }

    fun initUserHeader() {
        headerDataBinding = DataBindingUtil.bind<CommMenuHeaderBinding>(design_navigation_view.getHeaderView(0))
        getData()
    }

    fun getData() {

        var call = RetrofitManager.builder().service!!.showUsers(getAccessToken()!!.token, getAccessToken()!!.uid)

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
