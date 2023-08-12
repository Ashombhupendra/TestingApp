package com.dbvertex.job.peresentation.auth

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.util.Util
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentLoginBinding

import com.dbvertex.job.network.utils.hideKeyboard
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging


class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    var reference: DatabaseReference? = null
    var progressBar: ProgressDialog? = null

    private lateinit var mBinding: FragmentLoginBinding
    private val mAuthViewModel by activityViewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentLoginBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@LoginFragment
            viewModel = mAuthViewModel


        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindProgressButton(mBinding.loginSubmit)
        auth = FirebaseAuth.getInstance()
        progressBar = ProgressDialog(requireContext())



        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val currentToken = it.result
                currentToken?.let { token ->


                    Log.d("token", token)
                    mAuthViewModel.stoken.value = token


                }
            }
        }

        val onBackPressedCallback=object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        mBinding.loginSubmit.setOnClickListener {
            if (mAuthViewModel.phoneNum.value.isNullOrEmpty()) {
                mBinding.loginEmail.setError("Enter Mobile")
            } else if (mAuthViewModel.phoneNum.value.toString().length < 10) {
                mBinding.loginEmail.setError("Enter correct number")
            } else {

                mAuthViewModel.login()
                mAuthViewModel.loginNetworkState.observe(viewLifecycleOwner, Observer { state ->
                    when (state) {
                        NetworkState.LOADING_STARTED -> {
                            hideKeyboard(requireContext(), mBinding.loginSubmit)

                            mBinding.loginSubmit.apply {
                                showProgress()
                                isEnabled = false
                            }

                        }
                        NetworkState.LOADING_STOPPED -> {
                            progressBar!!.dismiss()
                            mBinding.loginSubmit.apply {
                                hideProgress("Send otp")
                                isEnabled = true
                            }

                        }
                        NetworkState.SUCCESS -> {
                            mBinding.loginSubmit.apply {
                                hideProgress("Send otp")
                                isEnabled = true
                            }
                            findNavController().navigate(R.id.otp_Fragment)
                            mAuthViewModel.loginNetworkState.removeObservers(requireActivity())

                        }

                        NetworkState.FAILED -> {
                            mBinding.loginSubmit.apply {
                                hideProgress("Send otp")
                                isEnabled = true
                            }

                        }
                    }
                })
            }

        }


    }


}