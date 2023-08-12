package com.dbvertex.job.network.utils

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dbvertex.job.R
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("til_error")
fun handleError(til: TextInputLayout, errorMessage: String?) {
    til.error = errorMessage
}

@BindingAdapter("tv_error")
fun handleErrorTv(tv: TextView, errorMessage: String?) {
    tv.text = errorMessage ?: ""
}

@BindingAdapter("image_url")
fun loadImageFromUrl(iv: ImageView, imageUrl: String?) {
    if (imageUrl == null || imageUrl.isEmpty()) {
        iv.setImageResource(R.drawable.profile_image_bg)
    } else {
        Glide.with(iv.context).load(imageUrl).into(iv)
    }
}

@BindingAdapter("stroke_color_string")
fun setStroke(materialCardView: MaterialCardView, colorString: String) {
    materialCardView.strokeColor = Color.parseColor(colorString)
}