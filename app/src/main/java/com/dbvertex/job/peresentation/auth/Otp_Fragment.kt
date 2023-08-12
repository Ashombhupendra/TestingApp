package com.dbvertex.job.peresentation.auth

import `in`.aabhasjindal.otptextview.OTPListener
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentOtpBinding
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.utils.SharedPrefrenceHelper


class Otp_Fragment : Fragment() {
    private val mAuthViewModel by activityViewModels<AuthViewModel>()
    private lateinit var mBinding: FragmentOtpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentOtpBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@Otp_Fragment
            viewModel = mAuthViewModel
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val image = view.findViewById<ImageView>(R.id.close_otp)
        image.setOnClickListener {
            hideKeyboard(requireContext(), it)
            findNavController().navigate(R.id.loginFragment)

        }
        mBinding.resendOtp.setOnClickListener {
            mAuthViewModel.login()
        }
        mBinding.loginSubmit.setOnClickListener {
            if (mBinding.otpView.otp.isNullOrEmpty()) {
                mBinding.incorrectOtpMessage.visibility = View.VISIBLE
            } else if (mBinding.otpView.otp.toString().length < 4) {
                mBinding.incorrectOtpMessage.visibility = View.VISIBLE
            } else {
                mBinding.incorrectOtpMessage.visibility = View.GONE
            }
        }
        mBinding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                mBinding.incorrectOtpMessage.visibility = View.GONE
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onOTPComplete(otp: String) {
                if (otp == mAuthViewModel.verifyotp.value.toString()) {


                    hideKeyboard(requireContext(), mBinding.otpView)


                    val navOptions = NavOptions.Builder().setPopUpTo(R.id.signUp, true).build()


                    findNavController().navigate(R.id.signUp, null, navOptions)


                    if (mAuthViewModel.status.value.equals("complete")) {
                        if (mAuthViewModel.otherconfirmornot.value == false) {
                            findNavController().navigate(R.id.otherUserConfirmation)
                        } else {
                            val navOptions =
                                NavOptions.Builder().setPopUpTo(R.id.otp_Fragment, true).build()
                            findNavController().navigate(R.id.jobBoardMain, null, navOptions)
                            SharedPrefrenceHelper.isLoggedIn = true
                            SharedPrefrenceHelper.isSignupComleted = true
                        }

                    } else if (mAuthViewModel.status.value.equals("signup")) {
                        val navOptions =
                            NavOptions.Builder().setPopUpTo(R.id.otp_Fragment, true).build()
                        findNavController().navigate(R.id.signUp, null, navOptions)
                    } else {
                        if (mAuthViewModel.status.value.equals("freelancer")) {
                            val navOptions =
                                NavOptions.Builder().setPopUpTo(R.id.otp_Fragment, true).build()
                          //  findNavController().navigate(R.id.frrelancerFragment, null, navOptions)
                            findNavController().navigate(R.id.jobBoardMain, null, navOptions)
                            SharedPrefrenceHelper.isSignupComleted = true
                        } else if (mAuthViewModel.status.value.equals("company")) {
                            val navOptions =
                                NavOptions.Builder().setPopUpTo(R.id.otp_Fragment, true).build()
                            findNavController().navigate(R.id.companyFragment, null, navOptions)
                            SharedPrefrenceHelper.isSignupComleted = true
                        } else {

                            val navOptions =
                                NavOptions.Builder().setPopUpTo(R.id.otp_Fragment, true).build()
                            findNavController().navigate(R.id.otherSignup, null, navOptions)
                            SharedPrefrenceHelper.isSignupComleted = true
                        }
                    }

                } else {

                    mBinding.incorrectOtpMessage.visibility = View.VISIBLE
                }
            }
        }
    }
}