package com.dbvertex.job.peresentation.photoshoot.presaved

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.databinding.FragmentPhotoshootPresavedDetailBinding
import com.dbvertex.job.network.response.photoshoot.PresavedDTO
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import com.google.gson.Gson


class Photoshoot_Presaved_detail : Fragment() {

    private lateinit var mBinding : FragmentPhotoshootPresavedDetailBinding
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPhotoshootPresavedDetailBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@Photoshoot_Presaved_detail
            viewmodel = mPhotoshootViewModel
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mBinding.backOfflinePodcast.setOnClickListener {

            findNavController().navigateUp()
        }

        try {
            val presaved =requireArguments().getString("presaved")
            if (!presaved.isNullOrBlank()){
                val presaveddto = Gson().fromJson(presaved, PresavedDTO::class.java)
                mPhotoshootViewModel.presavedTitle.value = presaveddto.message
                mPhotoshootViewModel.presavedDescription.value = presaveddto.description
                mBinding.photoshootPresaveDSave.visibility = View.GONE
            }
        }catch (e :Exception){
            mBinding.photoshootPresaveDSave.visibility = View.VISIBLE

            mBinding.photoshootPresaveDSave.setOnClickListener {
                if (mPhotoshootViewModel.presavedTitle.value.isNullOrEmpty() ){
                    mBinding.photoshootPresavedDTitle.setError("Enter Title")
                }else if (mPhotoshootViewModel.presavedDescription.value.isNullOrEmpty()){
                    mBinding.photoshootPresavedDDescription.setError("Enter Description")
                }else{
                    mPhotoshootViewModel.savePresavedMessage()
                }
            }
        }

        mBinding.photoshootPresaveDShare.setOnClickListener {
            if (mPhotoshootViewModel.presavedTitle.value.isNullOrEmpty() ){
                mBinding.photoshootPresavedDTitle.setError("Enter Title")
            }else if (mPhotoshootViewModel.presavedDescription.value.isNullOrEmpty()){
                mBinding.photoshootPresavedDDescription.setError("Enter Description")
            }else{

                if (!mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
                sendEmail("",
                    mPhotoshootViewModel.presavedTitle.value.toString(),
                    mPhotoshootViewModel.presavedDescription.value.toString()
                ) }else{
                    showSnackBar("Firstly create Photoshoot")
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
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Log.d("exception", e.toString())
        }

    }



}