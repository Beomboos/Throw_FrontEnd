package com.example.deamhome.presentation.main.qr

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("setImageBitmap")
fun setImageBitmap(imageView: ImageView, bitmap: Bitmap?) {
    imageView.setImageBitmap(bitmap)
}