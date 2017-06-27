package com.bobchouwb.utils.common

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bobchouwb.R
import com.bobchouwb.utils.common.RangleTransForm
import com.squareup.picasso.Picasso

/**
 * Created by zhoubo on 2017/6/24.
 */

object ImageLoaderUtil {

    @JvmStatic
    @BindingAdapter("android:src")
    fun loadImage(imageView: ImageView, imageUrl: String?)
            = Picasso.with(imageView.context)
            .load(imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .into(imageView)

    @JvmStatic
    @BindingAdapter("android:circleSrc")
    fun loadCircleImage(imageView: ImageView, imageUrl: String?)
            = Picasso.with(imageView.context)
            .load(imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .transform(RangleTransForm(100))
            .into(imageView)
}