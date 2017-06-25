package com.bobchouwb.utils.net

import android.util.Log
import com.bobchouwb.base.BCWBApp
import com.bobchouwb.utils.api.WBAPIService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * Created by zhoubo on 2017/6/18.
 */
class RetrofitManager {

    val TAG = "RetrofitManager"

    //短缓存有效期为10分钟
    val CACHE_STALE_SHORT = 60 * 10
    //长缓存有效期为7天
    val CACHE_STALE_LONG="60 * 60 * 24 * 7"
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_LONG
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    val CACHE_CONTROL_NETWORK = "max-age=0"

    val BASE_URL = "https://api.weibo.com/2/"

    var mOkHttpClient: OkHttpClient? = null

    var service : WBAPIService? = null

    init {

        initOkHttpclient()

        var retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(WBAPIService::class.java)
    }

    fun initOkHttpclient() {

        if (mOkHttpClient == null) {
            var httpLog = HttpLoggingInterceptor()
            httpLog.level = HttpLoggingInterceptor.Level.BODY
            var cacheDir : File = BCWBApp.instance().cacheDir
            Log.d(TAG, "cacheDir : " + cacheDir.path)
            val cache = Cache(cacheDir, (1024 * 1024 * 100).toLong()) //100Mb
            mOkHttpClient = OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(httpLog)
                    .build()
        }
    }

    companion object {

        fun builder() : RetrofitManager = RetrofitManager()
    }
}