package com.bobchouwb.utils

import android.content.Context
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken

/**
 * Created by zhoubo on 2017/6/24.
 */

fun Context.getAccessToken() : Oauth2AccessToken? = AccessTokenKeeper.readAccessToken(applicationContext)

