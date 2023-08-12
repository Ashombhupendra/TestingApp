package com.dbvertex.job.peresentation.settings

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.dbvertex.job.MainActivity
import com.dbvertex.job.databinding.DialogLogoutBinding
import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch


class Logout : DialogFragment() {

    lateinit var binding : DialogLogoutBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
         binding = DialogLogoutBinding.inflate(layoutInflater, null, false)
        binding.frag = this
        return MaterialAlertDialogBuilder(requireContext()).setView(binding.root).create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         bindProgressButton(binding.logoutBtn)

    }
    fun logout() {

              binding.logoutBtn.apply {
                     showProgress()
                     isEnabled = false
                 }
          auth = FirebaseAuth.getInstance()
          FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val currentToken = it.result
                currentToken?.let { token ->
                    Log.d("tokensdsd", token)

                     finallogout(token)
                }
            }else{
                Log.d("tokensdsd", it.toString())
            }
        }
    }

    fun finallogout(token: String) {
        lifecycleScope.launch {
            val result = FreelancerProfileRepository.user_logout(token)
            when(result){
                is ResultWrapper.Success ->{
                    if (result.response){
                        val activity = requireActivity()
                        if (activity is MainActivity) {
                            activity.logouts()
                            dismiss()

                        }
                        binding.logoutBtn.apply {
                            hideProgress("Logout")
                            isEnabled = true
                        }
                    }else{
                        binding.logoutBtn.apply {
                            hideProgress("Logout")
                            isEnabled = true
                        }
                        temp_showToast("Something went wrong. Try again....")
                    }
                }
                is ResultWrapper.Failure ->{
                    binding.logoutBtn.apply {
                        hideProgress("Logout")
                        isEnabled = true
                    }
                    temp_showToast("Something went wrong. Try again....")
                }
            }

        }
    }
}