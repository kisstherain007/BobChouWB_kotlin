package com.bobchouwb.ui.activity

import android.os.Bundle
import com.bobchouwb.R
import com.bobchouwb.base.ui.activity.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
