package com.dbvertex.job.peresentation.photoshoot.contract

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPhotoshootContractBinding
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.response.photoshoot.ContractDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import kotlinx.coroutines.launch


class PhotoshootContract : Fragment(), onContractClick {

    private lateinit var mBinding: FragmentPhotoshootContractBinding
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            FragmentPhotoshootContractBinding.inflate(layoutInflater, container, false).apply {
                lifecycleOwner = this@PhotoshootContract
                viewmodel = mPhotoshootViewModel
            }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindProgressButton(mBinding.photoshootContractShare)
        /*mPhotoshootViewModel.photoshoot_id.observe(viewLifecycleOwner){
            if (it.isNullOrEmpty()){
                Toast.makeText(JobApp.instance.applicationContext, "Firstly Create Photoshoot", Toast.LENGTH_SHORT).show()

            }else{
                mPhotoshootViewModel.getContract()
            }
        }*/

        mPhotoshootViewModel.getContractList().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                val adapter = Contract_adapter(it, this)
                mBinding.contractRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }

        /*mBinding.photoshootContractPreview.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("preview_type", "contract")
            bundle.putString("preview_link", "http://thephototribe.in/welcome/contract/${mPhotoshootViewModel.photoshoot_id.value}")
           findNavController().navigate(R.id.previewContract, bundle)
        }*/

    }

    private fun shareonMail(contractID: String) {
        lifecycleScope.launch {
            val result = PhotoShootRepository.getContractsharelink(contractID)
            when (result) {
                is ResultWrapper.Success -> {
                    mBinding.photoshootContractShare.apply {
                        hideProgress("Share to mail")
                        isEnabled = true
                    }
                    sendEmail("", "Contract", result.response.toString())
                }
                is ResultWrapper.Failure -> {
                    temp_showToast(result.errorMessage)
                    mBinding.photoshootContractShare.apply {
                        hideProgress("Share to mail")
                        isEnabled = true
                    }
                }
            }
        }
    }

    private fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Log.d("exception", e.toString())
        }

    }

    override fun NavtoContractDetail(contractDTO: ContractDTO) {
        mPhotoshootViewModel.contract_id.value = contractDTO.id
        mPhotoshootViewModel.getContract(contractDTO.id)
        findNavController().navigate(R.id.edit_Contract)
    }

    override fun PreviewCOntract(contractDTO: ContractDTO) {
        if (mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
            showSnackBar("Firstly Create PhotoShoot")
        }else{
            val bundle = Bundle()
            bundle.putString("preview_type", "contract")
            bundle.putString(
                "preview_link",
                "http://thephototribe.in/welcome/contracts/${contractDTO.contract_code}"
            )
            findNavController().navigate(R.id.previewContract, bundle)
        }


    }

    override fun SharetoContract(contractDTO: ContractDTO) {
        if (mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
            showSnackBar("Firstly Create PhotoShoot")
        }else{
            shareonMail(contractID = contractDTO.id)
        }

    }
}