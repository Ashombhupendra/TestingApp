package com.dbvertex.job.peresentation.navigate_to_page.discuss

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentAddQuestionFragBinding
import com.dbvertex.job.network.utils.ErrorshowSnackBar
import com.dbvertex.job.network.utils.FileUtils
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.network.utils.showSnackBar
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.dialog.MaterialAlertDialogBuilder


private const val STORAGE_PERMISSION_CODE = 114
private data class ImageData(val imageView: ImageView, var uri: Uri?)
class AddQuestion_frag : Fragment() {
    private lateinit var mBinding : FragmentAddQuestionFragBinding
    private val mDiscussViewModel by activityViewModels<DiscussViewModel>()
    private var index = -1
    private val images = mutableListOf<ImageData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentAddQuestionFragBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@AddQuestion_frag
            viewmodel = mDiscussViewModel
           frag = this@AddQuestion_frag


        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        bindProgressButton(mBinding.questionSubmit)

        images.add(ImageData(mBinding.image1, null))
        images.add(ImageData(mBinding.image2, null))
        images.add(ImageData(mBinding.image3, null))
        images.add(ImageData(mBinding.image4, null))
        images.add(ImageData(mBinding.image5, null))
       mBinding.addQuestionBack.setOnClickListener {
           hideKeyboard(JobApp.instance.applicationContext, it)
           findNavController().navigateUp()

       }
        mBinding.questionSubmit.setOnClickListener {
            hideKeyboard(JobApp.instance.applicationContext, it)
            if (mDiscussViewModel.mquestion.value.isNullOrEmpty()){
                mBinding.etQuestion.setError("Enter question")
            }else if (mDiscussViewModel.mdescription.value.isNullOrEmpty()){
                  mBinding.etQuestiondDes.setError("Enter question description")
            }else{
                submit()
                mDiscussViewModel.addquestionstate.observe(viewLifecycleOwner, Observer {state ->
                    when(state){
                        NetworkState.LOADING_STARTED ->{
                            mBinding.questionSubmit.apply {
                                showProgress()
                                isEnabled = false
                            }


                        }
                        NetworkState.LOADING_STOPPED ->{
                            mBinding.questionSubmit.apply {
                                hideProgress("Submit")
                                isEnabled = true
                            }
                        }
                        NetworkState.SUCCESS ->{
                            mDiscussViewModel.mquestion.value =  ""
                            mDiscussViewModel.mdescription.value =  ""
                            mDiscussViewModel.images.clear()
                            mBinding.questionSubmit.apply {
                                hideProgress("Submit")
                                isEnabled = true
                            }
                             showSnackBar("Discuss added!")
                            findNavController().navigateUp()
                        }
                        NetworkState.FAILED ->{
                            ErrorshowSnackBar("Something went wrong! Try again. ")
                            mBinding.questionSubmit.apply {
                                hideProgress("Submit")
                                isEnabled = true
                            }
                        }
                    }

                })
            }



        }

    }

    fun getImageFromGallery(index: Int) {
        this.index = index


           when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    //permission is already granted
                    getImage(index)
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    //telling user why the permission is required
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Permission Required")
                        .setMessage(R.string.image_permission_rationale)
                        .setNeutralButton("OK") { dialog, _ ->
                            requestStoragePermission()
                            dialog.dismiss()
                        }.setNegativeButton("No Thanks") { dialog, _ ->
                            //closing the rationale dialog
                            dialog.dismiss()
                        }.show()
                }
                else -> {
                    //user has not granted permission yet, so requesting for the permission
                    requestStoragePermission()
                }
            }




    }

    private fun requestStoragePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
            STORAGE_PERMISSION_CODE
        )
    }

    private fun getImage(index: Int) {

        this.index = index
        val intent = Intent(Intent.ACTION_PICK).apply {
            this.type = "image/*"
        }
        startActivityForResult(intent, 11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode ==11) {
            val uri = data?.data
            uri ?: return
            val selectedImageView = images[index]
            selectedImageView.uri = uri

            Log.d("Productdetails", selectedImageView.imageView.toString())
            Glide.with(requireContext()).load(uri).into(selectedImageView.imageView)
        }

    }

    fun submit() {
        images.forEach { imageData ->
            imageData.uri?.let { uri ->
                requireActivity().contentResolver.openInputStream(uri)?.let { inputStream ->
                    val name = FileUtils.getFile(requireContext(), uri).name
                    val bytes = inputStream.readBytes()
                    mDiscussViewModel.images.add(bytes to name)
                }

            }
        }

        if (mDiscussViewModel.mquestion.value.isNullOrEmpty()) {
            showSnackBar("Enter your question")
        } else if (mDiscussViewModel.mdescription.value.isNullOrEmpty()) {
            showSnackBar("Enter description")
        }else{
            mDiscussViewModel.postQuestion()
        }
    }


    private fun hasAtLeastOneImage(): Boolean {
        images.forEach {
            if (it.uri != null) {
                return true
            }
        }
        return false
    }

}