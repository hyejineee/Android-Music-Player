package com.hyejineee.flochallenge.util

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.hyejineee.flochallenge.R
import jp.wasabeef.glide.transformations.MaskTransformation

@BindingAdapter("loadImage")
fun ImageView.loadImage(imageUrl: String?) {

    val multi = MultiTransformation<Bitmap>(
        CircleCrop(),
        MaskTransformation(R.drawable.album_mask)
    )

    Glide.with(this.getContext())
        .load(imageUrl)
        .apply(RequestOptions.bitmapTransform(multi))
        .into(this)
}

@BindingAdapter("splashImage")
fun splashImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.getContext())
        .load(imageUrl)
        .centerCrop()
        .into(view)
}
