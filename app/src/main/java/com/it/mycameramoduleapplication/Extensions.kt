package com.it.mycameramoduleapplication

import android.content.Context

import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.it.mycameramoduleapplication.application.GlideApp


fun ImageView.loadImg(url: String, activity: Context) {
    GlideApp.with(activity)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .thumbnail(0.1f)
            .placeholder(R.drawable.ic_user_profile_holder)
            .dontAnimate()
            .into(this)


}
