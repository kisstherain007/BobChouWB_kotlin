package com.bobchouwb.utils

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bobchouwb.R
import com.squareup.picasso.Picasso

/**
 * Created by zhoubo on 2017/6/24.
 */

object ImageLoaderUtil {

    @JvmStatic
    @BindingAdapter("android:src")
    fun loadImage(imageView: ImageView, imageUrl: String?) = Picasso.with(imageView.context).load(imageUrl).placeholder(R.mipmap.ic_launcher).into(imageView)
}