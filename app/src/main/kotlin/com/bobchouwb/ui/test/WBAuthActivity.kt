///*
// * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.bobchouwb.ui.test
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.text.TextUtils
//import android.text.method.ScrollingMovementMethod
//import android.view.View
//import android.view.View.OnClickListener
//import android.widget.TextView
//import android.widget.Toast
//
//import com.bobchouwb.R
//import com.sina.weibo.sdk.auth.AccessTokenKeeper
//import com.sina.weibo.sdk.auth.Oauth2AccessToken
//import com.sina.weibo.sdk.auth.WbConnectErrorMessage
//import com.sina.weibo.sdk.auth.sso.SsoHandler
//import com.sina.weibo.sdk.exception.WeiboException
//import com.sina.weibo.sdk.net.RequestListener
//
//import java.text.SimpleDateFormat
//
///**
// * 该类主要演示如何进行授权、SSO登陆。
//
// * @author SINA
// * *
// * @since 2013-09-29
// */
//class WBAuthActivity : Activity() {
//    /** 显示认证后的信息，如 AccessToken  */
//    private var mTokenText: TextView? = null
//    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能   */
//    private var mAccessToken: Oauth2AccessToken? = null
//    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效  */
//    private var mSsoHandler: SsoHandler? = null
//
//    /**
//     * @see {@link Activity.onCreate}
//     */
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_auth)
//
//        // 获取 Token View，并让提示 View 的内容可滚动（小屏幕可能显示不全）
//        mTokenText = findViewById(R.id.token_text_view) as TextView
//        val hintView = findViewById(R.id.obtain_token_hint) as TextView
//        hintView.movementMethod = ScrollingMovementMethod()
//        // 创建微博实例
//
//
//        mSsoHandler = SsoHandler(this@WBAuthActivity)
//
//
//        // SSO 授权, 仅客户端
//        findViewById(R.id.obtain_token_via_sso).setOnClickListener {
//            mSsoHandler!!.authorizeClientSso(object : com.sina.weibo.sdk.auth.WbAuthListener {
//                override fun onSuccess(oauth2AccessToken: Oauth2AccessToken) {
//
//                }
//
//                override fun cancel() {
//
//                }
//
//                override fun onFailure(wbConnectErrorMessage: WbConnectErrorMessage) {
//
//                }
//            })
//        }
//
//        // SSO 授权, 仅Web
//        findViewById(R.id.obtain_token_via_web).setOnClickListener { mSsoHandler!!.authorizeWeb(SelfWbAuthListener()) }
//
//        // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
//        findViewById(R.id.obtain_token_via_signature).setOnClickListener { mSsoHandler!!.authorize(SelfWbAuthListener()) }
//
//        // 用户登出
//        findViewById(R.id.logout).setOnClickListener {
//            AccessTokenKeeper.clear(applicationContext)
//            mAccessToken = Oauth2AccessToken()
//            updateTokenView(false)
//        }
//
//        //更新token
//        findViewById(R.id.refresh).setOnClickListener {
//            if (!TextUtils.isEmpty(mAccessToken!!.refreshToken)) {
//                AccessTokenKeeper.refreshToken(Constants.APP_KEY, this@WBAuthActivity, object : RequestListener {
//                    override fun onComplete(response: String) {
//
//                    }
//
//                    override fun onWeiboException(e: WeiboException) {
//
//                    }
//                })
//            }
//        }
//
//        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
//        // 第一次启动本应用，AccessToken 不可用
//        mAccessToken = AccessTokenKeeper.readAccessToken(this)
//        if (mAccessToken!!.isSessionValid) {
//            updateTokenView(true)
//        }
//    }
//
//    /**
//     * 当 SSO 授权 Activity 退出时，该函数被调用。
//
//     * @see {@link Activity.onActivityResult}
//     */
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // SSO 授权回调
//        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
//        if (mSsoHandler != null) {
//            mSsoHandler!!.authorizeCallBack(requestCode, resultCode, data)
//        }
//
//    }
//
//
//    private inner class SelfWbAuthListener : com.sina.weibo.sdk.auth.WbAuthListener {
//        override fun onSuccess(token: Oauth2AccessToken) {
//            this@WBAuthActivity.runOnUiThread {
//                mAccessToken = token
//                if (mAccessToken!!.isSessionValid) {
//                    // 显示 Token
//                    updateTokenView(false)
//                    // 保存 Token 到 SharedPreferences
//                    AccessTokenKeeper.writeAccessToken(this@WBAuthActivity, mAccessToken)
//                    Toast.makeText(this@WBAuthActivity,
//                            R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//        override fun cancel() {
//            Toast.makeText(this@WBAuthActivity,
//                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show()
//        }
//
//        override fun onFailure(errorMessage: WbConnectErrorMessage) {
//            Toast.makeText(this@WBAuthActivity, errorMessage.errorMessage, Toast.LENGTH_LONG).show()
//        }
//    }
//
//    /**
//     * 显示当前 Token 信息。
//
//     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
//     */
//    private fun updateTokenView(hasExisted: Boolean) {
//        val date = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
//                java.util.Date(mAccessToken!!.expiresTime))
//        val format = getString(R.string.weibosdk_demo_token_to_string_format_1)
//        mTokenText!!.text = String.format(format, mAccessToken!!.token, date)
//
//        var message = String.format(format, mAccessToken!!.token, date)
//        if (hasExisted) {
//            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message
//        }
//        mTokenText!!.text = message
//    }
//
//    companion object {
//
//        private val TAG = "weibosdk"
//    }
//}