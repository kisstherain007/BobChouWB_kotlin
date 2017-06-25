package com.bobchouwb.utils

/**
 * Created by zhoubo on 2017/6/18.
 */
class Constant {

    companion object {

        /** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
        open val APP_KEY : String = "488109751"

        /**
         * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
         * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
         */
        val REDIRECT_URL : String = "https://api.weibo.com/oauth2/default.html"

        val SCOPE : String = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write"
    }

}
