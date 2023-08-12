package com.dbvertex.job.peresentation.settings

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentContactUsBinding
import com.dbvertex.job.network.repository.AboutUsRepository
import com.dbvertex.job.network.utils.*
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kotlinx.coroutines.launch

class Contact_Us : Fragment() {
       private lateinit var mBinding : FragmentContactUsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentContactUsBinding.inflate(layoutInflater, container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            bindProgressButton(mBinding.contactUsSubmit)
        mBinding.contactUsBack.setOnClickListener {
            findNavController().navigateUp()
        }
         //  At this point the view is not scrolling!
       /* mBinding.scrollviewtv.setSelected(true);*/


        mBinding.contactUsSubmit.setOnClickListener {
            if (mBinding.contactUsName.text.isNullOrEmpty()){
                 mBinding.contactUsName.setError("Enter your name")
            }else if (!isValidEmail(mBinding.contactUsEmail.text.toString())){
                mBinding.contactUsEmail.setError("Enter valid email address")
            }else if (mBinding.contactUsFeedback.text.isNullOrEmpty()){
                mBinding.contactUsFeedback.setError("Enter your valueable feedback")
            }else{
                hideKeyboard(requireActivity(),it)
                mBinding.contactUsSubmit.apply {
                    isClickable = false
                    showProgress()
                }
                submitdata()
            }
        }
    }

    private  fun submitdata(){
         lifecycleScope.launch {
             val result =  AboutUsRepository.postContactUs(
                 mBinding.contactUsName.text.toString(),
             mBinding.contactUsEmail.text.toString(),
             mBinding.contactUsFeedback.text.toString())
             when(result){
                 is ResultWrapper.Success ->{
                     mBinding.contactUsSubmit.apply {
                         isClickable = true
                         hideProgress("SUBMIT")
                     }
                      if (result.response){
                          alertdialog("Alert", "Your request is successfull sent.We are contact you soon.")
                      }

                 }
                 is ResultWrapper.Failure ->{
                     mBinding.contactUsSubmit.apply {
                         isClickable = true
                         hideProgress("SUBMIT")
                     }
                     temp_showToast(result.errorMessage)
                 }
             }
         }
    }


    private fun alertdialog (title : String, message : String){
        val builder = AlertDialog.Builder(requireActivity(),R.style.MyDatePickerStyle)
        builder.setTitle("$title")
        builder.setMessage("$message")
        builder.setPositiveButton("YES"){dialog, which ->
             dialog.cancel()
             findNavController().navigateUp()

        }

        // Set other dialog properties
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


}