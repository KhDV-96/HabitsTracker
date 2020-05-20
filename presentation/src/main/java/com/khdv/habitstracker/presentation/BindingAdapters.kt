package com.khdv.habitstracker.presentation

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.khdv.habitstracker.R

@BindingAdapter("circleImageUrl")
fun bindCircleImage(imageView: ImageView, imageUrl: String) {
    val uri = imageUrl.toUri().buildUpon().scheme("https").build()
    Glide.with(imageView.context)
        .load(uri)
        .placeholder(R.mipmap.ic_launcher_round)
        .error(R.drawable.ic_error_white_24dp)
        .transform(CircleCrop())
        .into(imageView)
}