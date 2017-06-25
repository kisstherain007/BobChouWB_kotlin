package com.bobchouwb.utils.api

import com.bobchouwb.data.Timeline
import com.bobchouwb.data.user.UserInfoBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by zhoubo on 2017/6/18.
 */

interface WBAPIService {

    /**
     * 根据用户ID获取用户信息
     */
    @GET("users/show.json")
    fun showUsers(@Query("access_token") access_token: String,
                  @Query("uid") uid: String) : Call<UserInfoBean>

    @GET("statuses/public_timeline.json")
    fun get_public_timeline(@Query("access_token") access_token: String) : Call<Timeline>
}