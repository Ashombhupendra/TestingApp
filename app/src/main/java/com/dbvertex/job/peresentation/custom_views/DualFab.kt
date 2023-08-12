package com.dbvertex.job.peresentation.custom_views

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.dbvertex.job.databinding.ViewDualFabBinding

class DualFab @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(
    context,
    attributeSet,
    defStyle
) {
    private val mAnimations = mutableListOf<ObjectAnimator>()
    private val duration = 300L
    private val translation_x = dpToPixel(-75f)
    private val translation_y = dpToPixel(-10f)
    var onGalleryFabClicked: ((View) -> Unit)? = null
    var onCameraFabClicked: ((View) -> Unit)? = null
    var onFabClicked: ((Boolean) -> Unit)? = null
    var isOpened = false
    private val mBinding = ViewDualFabBinding.inflate(LayoutInflater.from(context), this, true).apply {
            dualFabAdd.setOnClickListener {
                if (isOpened) {
                    closeFab()
                } else {
                    openFab()
                }
            }
            dualFabGallery.setOnClickListener {
                onGalleryFabClicked?.invoke(it)
            }
            dualFabCamera.setOnClickListener {
                onCameraFabClicked?.invoke(it)
            }
        }

    init {
        mAnimations.addAll(
            listOf(
                ObjectAnimator.ofFloat(mBinding.dualFabAdd, "rotation", 135f),
                ObjectAnimator.ofFloat(mBinding.dualFabCamera, "translationX", translation_x),
                ObjectAnimator.ofFloat(mBinding.dualFabCamera, "translationY", translation_y),
                ObjectAnimator.ofFloat(mBinding.dualFabCamera, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(mBinding.dualFabGallery, "translationX", translation_y),
                ObjectAnimator.ofFloat(mBinding.dualFabGallery, "translationY", translation_x),
                ObjectAnimator.ofFloat(mBinding.dualFabGallery, "alpha", 0f, 1f),
            )
        )
    }

    fun openFab() {
        mAnimations.forEach {
            it.duration = duration
            it.start()
        }
        isOpened = true
        onFabClicked?.invoke(isOpened)
    }

    fun closeFab() {
        mAnimations.forEach {
            it.reverse()
        }
        isOpened = false
        onFabClicked?.invoke(isOpened)
    }

    private fun dpToPixel(dp: Float): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}
