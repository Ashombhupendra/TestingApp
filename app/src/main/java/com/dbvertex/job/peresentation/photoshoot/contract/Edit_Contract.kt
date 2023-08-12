package com.dbvertex.job.peresentation.photoshoot.contract

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.databinding.FragmentEditContractBinding
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.utils.*
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kotlinx.coroutines.launch


class Edit_Contract : Fragment() {

     private lateinit var mBinding : FragmentEditContractBinding
     private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEditContractBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@Edit_Contract
            viewmodel = mPhotoshootViewModel
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                bindProgressButton(mBinding.savePhotoshoot)

        mBinding.backOfflinePodcast.setOnClickListener {
               findNavController().navigateUp()
        }


         mBinding.savePhotoshoot.setOnClickListener {
             hideKeyboard(JobApp.instance.applicationContext, it)
                if (mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
                      showSnackBar("Firstly Create PhotoShoot")
                }else{
                    mBinding.savePhotoshoot.apply {
                        showProgress()
                        isEnabled  = false
                        saveContract()
                    }
                }
         }


    }
    private fun saveContract() {
        lifecycleScope.launch {
            val result = PhotoShootRepository.saveContract(mPhotoshootViewModel.contract_id.value.toString(), mPhotoshootViewModel.contractdes.value.toString())
            when(result){
                is ResultWrapper.Success ->{
                    mBinding.savePhotoshoot.apply {
                        hideProgress("Save")
                        isEnabled  = true
                    }
                    showSnackBar("Successfull saved")
                          findNavController().navigateUp()
                }
                is ResultWrapper.Failure ->{
                    ErrorshowSnackBar("${result.errorMessage}")
                    mBinding.savePhotoshoot.apply {
                        hideProgress("Save")
                        isEnabled  = true
                    }
                }
            }
        }
    }

}