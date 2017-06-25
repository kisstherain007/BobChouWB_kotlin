package com.bobchouwb.utils

import android.app.Activity
import android.content.Context
import android.databinding.BindingAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bobchouwb.R
import com.squareup.picasso.Picasso

/**
 * Created by zhoubo on 2017/6/10.
 */

fun ViewGroup.inflate(layoutRes: Int, parent: ViewGroup, attachToBoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutRes, parent, attachToBoot)

fun Activity.showToast(msg: String) = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
