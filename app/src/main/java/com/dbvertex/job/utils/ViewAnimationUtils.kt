package com.dbvertex.job.utils

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation


object  ViewAnimationUtils {
    fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targtetHeight: Int = v.getMeasuredHeight()
        v.getLayoutParams().height = 0
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {

                    if (interpolatedTime == 1f)
                    {
                        v.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT
                        v.requestLayout()
                    }
                    else{
                        v.getLayoutParams().height =  (targtetHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }

            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.setDuration(
            350
        )
        Handler().postDelayed(Runnable {
            v.setVisibility(View.VISIBLE)
        },350 )
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {

                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.setDuration(350)
        Handler().postDelayed(Runnable {
            v.visibility = View.GONE
        },350 )
        v.startAnimation(a)
    }
}