package com.dbvertex.job.peresentation.custom_views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.dbvertex.job.R

class LikeView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(mContext, attrs, defStyle) {
    private lateinit var mCountTV: TextView
    private lateinit var mIcon: ImageView
    private var liked: Boolean = false
    private var likeCount: Int = 0
    var onLikeClickListener: ((liked: Boolean) -> Unit)? = null
    var color: Int = 0

    fun setLike_count(count: Int) {
        likeCount = count
        setValues()
    }

    fun setLiked(like: Boolean) {
        liked = like
        setValues()
    }

    init {
        orientation = HORIZONTAL
        inflateLayout()
        getAttributes(attrs)
        findViews()
        setValues()

        setOnClickListener {
            if (liked) { //already liked, now we have to unlike
                likeCount--
            } else { //not liked, we have to like it
                likeCount++
                animateLikeIcon()
            }
            liked = !liked
            setValues()
            onLikeClickListener?.invoke(liked)
        }
    }

    private fun animateLikeIcon() {
        val scaleXAnimator = ObjectAnimator.ofFloat(mIcon, "scaleX", 1f, 2f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(mIcon, "scaleY", 1f, 2f, 1f)
        val rotationAnimator = ObjectAnimator.ofFloat(mIcon, "rotation", 0f, 45f, 0f)
        AnimatorSet().apply {
            duration = 800
            interpolator = OvershootInterpolator()
            playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
            start()
        }
    }

    private fun inflateLayout() {
        val layoutInflater = LayoutInflater.from(mContext)
        layoutInflater.inflate(R.layout.view_like, this, true)
    }

    private fun findViews() {
        mCountTV = findViewById(R.id.likeCountTV)
        mIcon = findViewById(R.id.like_icon)
    }

    private fun setValues() {
        mCountTV.apply {
            text = likeCount.toString()
            setTextColor(color)
        }

        mIcon.apply {
            setImageResource(getImageResource(liked))
            imageTintList = ColorStateList.valueOf(color)
        }
    }

    private fun getAttributes(attrs: AttributeSet?) {
        val typedArray = mContext.theme.obtainStyledAttributes(attrs, R.styleable.LikeView, 0, 0)
       // likeCount = typedArray.getInt(R.styleable.LikeView_like_count, 0)
        color = typedArray.getInt(R.styleable.LikeView_color, 0)
        liked = typedArray.getBoolean(R.styleable.LikeView_liked, false)
        typedArray.recycle()
    }

    private fun getImageResource(liked: Boolean) =
        if (liked)
            R.drawable.ic_baseline_favorite_24
        else
            R.drawable.ic_baseline_favorite_border_24
}
