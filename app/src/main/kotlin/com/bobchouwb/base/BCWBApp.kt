package com.bobchouwb.base

import android.app.Application
import com.bobchouwb.utils.Constant
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import java.io.File

/**
 * Created by zhoubo on 2017/6/10.
 */

class BCWBApp : Application() {

    companion object InstanceFactory{

        private var instance: BCWBApp? = null
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        WbSdk.install(this, AuthInfo(baseContext, Constant.APP_KEY, Constant.REDIRECT_URL, Constant.SCOPE))
    }

}