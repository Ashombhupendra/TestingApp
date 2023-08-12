package com.dbvertex.job.network.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dbvertex.job.BuildConfig
import com.dbvertex.job.JobApp
import com.dbvertex.job.data.SpecialisationItem
import com.dbvertex.job.network.response.SpecialisationDTO
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.view.Gravity
import android.view.animation.OvershootInterpolator
import android.widget.Button

import android.widget.FrameLayout


/**
 * Encapsulates different Exception Catch blocks.
 *
 * A general purpose function that takes a functional block, in which the api call is made.
 * It wraps the result from the call in one of the sub classes of the sealed super class called ResultWrapper.
 */
suspend fun <T> safelyCallApi(apiCall: suspend () -> T): ResultWrapper<out T> {
    return withContext(Dispatchers.IO) {
        try {
            val result = apiCall()
            ResultWrapper.Success(result)
        } catch (throwable: Throwable) {
            val message = when (throwable) {
                is IOException,
                is UnknownHostException -> "Error Connecting to server."
                is SocketTimeoutException -> "Error while connecting to internet."
                is HttpException ->
                    when (throwable.code()) {
                        408 -> "Request Timed Out.\nPlease Check your internet Connection"
                        400 -> "Request Failed, Please check all the entries"
                        500 -> "A Server Error Occurred.\nTry restarting app.\nIf problem persists contact us via email."
                        504 -> "Not connected to Internet"
                        404 -> NOT_FOUND //do not change this constant
                        else -> "An Unexpected Error occurred while completing request."
                    }
                else -> "An Unexpected Error occurred."
            }
            PhotoTribeApplog(throwable.message, throwable)
            val code=when (throwable){
                is HttpException->{
                    throwable.code()
                }
                else->{
                    0
                }
            }

            ResultWrapper.Failure(message,code)
        }
    }


}


fun PhotoTribeApplog(message: String?, throwable: Throwable? = null) {
    if (BuildConfig.DEBUG) {
        Log.d("Phototibe", message ?: "an unknown error occurred", throwable)
    }
}
fun hideKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

@Throws(ParseException::class)
fun formatDateFromDateString(
    inputDateFormat: String?, outputDateFormat: String?,
    inputDate: String?
): String? {
    val mParsedDate: Date
    val mOutputDateString: String
    val mInputDateFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())
    val mOutputDateFormat = SimpleDateFormat(outputDateFormat, Locale.getDefault())
    mParsedDate = mInputDateFormat.parse(inputDate)
    mOutputDateString = mOutputDateFormat.format(mParsedDate)
    return mOutputDateString
}


fun toSpecialisationItem(specialisationDTO: SpecialisationDTO) = SpecialisationItem(
               specialisationDTO.name
)


 suspend fun temp_showToast(text: String) = withContext(Dispatchers.Main) {
    Toast.makeText(
        JobApp.instance,
        text,
        Toast.LENGTH_LONG
    ).show() //todo remove this toast
}


fun Fragment.showSnackBar(text: String) {
   val snack =  Snackbar.make(requireView(), Html.fromHtml("<font color=\"#ffffff\">$text</font>"), Snackbar.LENGTH_SHORT)
    snack.view.setBackgroundColor(Color.parseColor("#223A54"))
    val params = snack.view!!.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    snack.view.layoutParams = params

    snack.show()
}

fun Fragment.ErrorshowSnackBar(text: String) {
    val snack =  Snackbar.make(requireView(),  Html.fromHtml("<font color=\"#ffffff\">$text</font>"), Snackbar.LENGTH_SHORT)
    snack.view.setBackgroundColor(Color.parseColor("#f22929"))
    val params = snack.view!!.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    snack.view.layoutParams = params
    snack.show()
}


fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(date)
}


/**
 * returns string of format mm:ss
 */
fun getDurationString(duration: Int, padMinutes: Boolean = false): String {
    val minutes: Int = duration / 60
    val seconds = duration % 60
    return if (padMinutes)
        String.format("%02d:%02d", minutes, seconds)
    else String.format("%d:%02d", minutes, seconds)
}


fun Fragment.startCircularRevealAnim(view: View, x: Int, y: Int, viewHeight: Float) {
    view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onLayoutChange(
            v: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            v.removeOnLayoutChangeListener(this)
            val anim: Animator =
                ViewAnimationUtils.createCircularReveal(v, x, y, dpToPixel(28f), viewHeight)
            anim.duration =1000
            anim.interpolator = AccelerateInterpolator()
            anim.start()
        }
    })
}

fun Fragment.pixelsToDp(px: Float): Float {
    return px / (requireContext().resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Fragment.dpToPixel(dp: Float): Float {
    return dp * (requireContext().resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.dpToPixel(dp: Float): Float {
    return dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}
fun getStatusBarHeight(resources: Resources): Int {
    var result = 0
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun isValidEmail(email: String): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun Fragment.getWindowSize(): Point {
    val point = Point()
    requireActivity().windowManager.defaultDisplay.getSize(point)
    return point
}

fun setButtonAnimation(view : View){
    val scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.8f, 1f)
    val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.8f, 1f)
    val rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 45f, 0f)
    AnimatorSet().apply {
        duration = 800
        interpolator = OvershootInterpolator()
        playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
        start()
    }
}



