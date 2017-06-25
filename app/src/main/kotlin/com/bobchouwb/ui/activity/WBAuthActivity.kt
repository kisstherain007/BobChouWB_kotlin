package com.bobchouwb.ui.activity

import android.content.Intent
import android.os.Bundle
import com.bobchouwb.base.ui.activity.BaseActivity
import com.bobchouwb.utils.getAccessToken
import com.bobchouwb.utils.showToast
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler

/**
 * Created by zhoubo on 2017/6/18.
 */
class WBAuthActivity : BaseActivity() {

    private var mAccessToken: Oauth2AccessToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAccessToken = this.getAccessToken()

        if (mAccessToken == null || !mAccessToken!!.isSessionValid) {
            configWeiBoSDK()
        }else {
            skipActivity()
        }
    }


    fun configWeiBoSDK() {

        SsoHandler(this).authorize(object : WbAuthListener {

            override fun cancel() {
            }

            override fun onFailure(p0: WbConnectErrorMessage?) {
                this@WBAuthActivity.showToast("获取token失败")
            }

            override fun onSuccess(token: Oauth2AccessToken?) {

                this@WBAuthActivity.runOnUiThread {

                    mAccessToken = token
                    if (mAccessToken!!.isSessionValid) {
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(this@WBAuthActivity, mAccessToken)
                        this@WBAuthActivity.showToast(mAccessToken.toString())
                        skipActivity()
                    }
                }
            }
        })
    }

    fun skipActivity() {
        startActivity(Intent(WBAuthActivity@this, MainActivity::class.java))
    }
}