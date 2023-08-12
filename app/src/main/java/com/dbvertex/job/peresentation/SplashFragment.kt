package com.dbvertex.job.peresentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging


class SplashFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null
    var mreference: DatabaseReference? = null
    var currentUser: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val currentToken = it.result
                currentToken?.let { token ->

                    Log.d("token", token)
                    // mAuthViewModel.stoken.value = token

                }
            }
        }
        val spalshlogo = view.findViewById<ImageView>(R.id.splash_logo)
        val aniRotate: Animation = AnimationUtils.loadAnimation(
            JobApp.instance.applicationContext,
            R.anim.rotate_clockwise
        )
        //splash_logo.startAnimation(aniRotate)
        Handler().postDelayed({

                if (!SharedPrefrenceHelper.isLoggedIn) {
                    //this look like opposite
                    //here we want to go to job dashboard not sign up 2 page
                    if (SharedPrefrenceHelper.isSignupComleted) {
                        val user = SharedPrefrenceHelper.user
                        if (user.registertype.equals("freelancer")) {
                            val navOptions =
                                NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
                            findNavController().navigate(R.id.jobBoardMain, null, navOptions)
                        }
//                        else if (user.registertype.equals("company")) {
//                            val navOptions =
//                                NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
//
//                            findNavController().navigate(R.id.companyFragment, null, navOptions)
//                        }
                        else {
                            val navOptions =
                                NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()

                        //    findNavController().navigate(R.id.otherSignup, null, navOptions)
                            findNavController().navigate(R.id.loginFragment, null, navOptions)
                        }

                    } else {
                        val navOptions =
                            NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()

                        findNavController().navigate(R.id.loginFragment, null, navOptions)
                    }
                } else {
                    val navOptions =
                        NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
                         findNavController().navigate(R.id.jobBoardMain, null, navOptions)
                }




        }, 3000) // 3000 is the delayed time in milliseconds.


    }
}