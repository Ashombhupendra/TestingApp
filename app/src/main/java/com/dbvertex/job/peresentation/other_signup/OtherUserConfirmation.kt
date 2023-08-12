package com.dbvertex.job.peresentation.other_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.DialogOtherUserConfirmationBinding
import com.dbvertex.job.utils.SharedPrefrenceHelper


class OtherUserConfirmation : DialogFragment() {

          private lateinit var mBinding : DialogOtherUserConfirmationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = DialogOtherUserConfirmationBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@OtherUserConfirmation
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.alertDialogOk.setOnClickListener {
            SharedPrefrenceHelper.isLoggedIn = false
            SharedPrefrenceHelper.isSignupComleted = false

            findNavController().navigate(R.id.loginFragment)
            dismiss()
        }
    }

}