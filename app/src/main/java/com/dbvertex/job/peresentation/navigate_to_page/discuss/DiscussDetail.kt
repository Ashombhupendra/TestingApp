package com.dbvertex.job.peresentation.navigate_to_page.discuss

import android.Manifest
import android.app.Dialog
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
import com.dbvertex.job.databinding.DialogDiscussImageViewBinding
import com.dbvertex.job.databinding.FragmentDiscussDetailBinding
import com.dbvertex.job.network.response.discuss.DiscusAnswerDTO
import com.dbvertex.job.network.utils.FileUtils
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.network.utils.showSnackBar
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import java.lang.Exception


data class Imagelist (
           val Imageview : ImageView
        )
private data class comentImageData(val imageView: ImageView, var uri: Uri?)
private const val STORAGE_PERMISSION_CODE = 114
class DiscussDetail : Fragment() {

         private lateinit var mBinding : FragmentDiscussDetailBinding
    private val mDiscussViewModel by activityViewModels<DiscussViewModel>()
    private lateinit var commentAdapter : DiscussanswerAdapter
    private var index = -1
    private val images = mutableListOf<comentImageData>()
    val listofimage = mutableListOf<Imagelist>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentDiscussDetailBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@DiscussDetail

            viewmodel = mDiscussViewModel




        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mBinding.ivBackDd.setOnClickListener {
            findNavController().navigateUp()
        }


        images.add(comentImageData(mBinding.image1, null))
        images.add(comentImageData(mBinding.image2, null))
        images.add(comentImageData(mBinding.image3, null))
        images.add(comentImageData(mBinding.image4, null))
        images.add(comentImageData(mBinding.image5, null))
        mDiscussViewModel.ddimages.observe(viewLifecycleOwner, Observer {

            if (it.isNullOrEmpty()){
                mBinding.imageContainer.visibility = View.GONE
            }else{
                mBinding.imageContainer.visibility = View.VISIBLE
                val imageViewList = listOf(
                    mBinding.discusImg1,
                    mBinding.discusImg2,
                    mBinding.discusImg3,
                    mBinding.discusImg4,
                    mBinding.discusImg5
                )
                it.forEachIndexed { index, url ->
                    Picasso.get().load(url.image).into(imageViewList[index])
                }
            }


        })
             mBinding.ivAttachCommentImg.setOnClickListener {
                 if (index < 4){

                     getImageFromGallery(index)
                 }else{
                     showSnackBar("Only five Images are allow")
                 }
             }

        mBinding.discusImg1.setOnClickListener {
            try {
                showDialog(mDiscussViewModel.ddimages.value!![0].image)
            }catch (e :Exception){ }
        }
        mBinding.discusImg2.setOnClickListener {
            try {
                showDialog(mDiscussViewModel.ddimages.value!![1].image)
            }catch (e :Exception){ }
        }
        mBinding.discusImg3.setOnClickListener {
            try {
                showDialog(mDiscussViewModel.ddimages.value!![2].image)
            }catch (e :Exception){}
        }
        mBinding.discusImg4.setOnClickListener {
            try {
                showDialog(mDiscussViewModel.ddimages.value!![3].image)
            }catch (e :Exception){}
        }
        mBinding.discusImg5.setOnClickListener {
            try {
                showDialog(mDiscussViewModel.ddimages.value!![4].image)
            }catch (e :Exception){}
        }
         mBinding.itemDiscusAnswerFollowUnfollow.setOnClickListener {
             if (mDiscussViewModel.ddFav.value ==true){
                 mDiscussViewModel.ddFav.value = false
             }else{
                 mDiscussViewModel.ddFav.value = true
             }
             mDiscussViewModel.setFavUnfavDiscuss(mDiscussViewModel.questionid.value.toString())
         }



        mBinding.ivCommnetSnd.setOnClickListener {
                       hideKeyboard(requireContext(),it)
            if (mBinding.edittextComment.text.isNullOrEmpty()){
                showSnackBar("Enter  Comment")
            }else{
                mBinding.ivCommnetSnd.apply {
                    isClickable = false
                }

               images.forEach { imagedata ->
                   imagedata.uri?.let {uri ->
                       requireActivity().contentResolver.openInputStream(uri)?.let { inputStream ->
                           val name = FileUtils.getFile(requireContext(), uri).name
                           val bytes = inputStream.readBytes()

                           mDiscussViewModel.ansImage.add(bytes to name)

                       }
                   }

               }

                mDiscussViewModel.sendAnswer(mBinding.edittextComment.text.toString())
                mDiscussViewModel.addcommentstate.observe(viewLifecycleOwner, Observer { state ->
                    when(state){
                        NetworkState.LOADING_STARTED ->{
                            mBinding.pbar.visibility = View.VISIBLE
                            mBinding.ivCommnetSnd.visibility = View.INVISIBLE
                            mBinding.ivCommnetSnd.isClickable = true

                        }
                        NetworkState.SUCCESS ->{
                            mDiscussViewModel.getSingleQuestion(mDiscussViewModel.questionid.value.toString())
                            mDiscussViewModel.et_comment.value = ""
                            mBinding.edittextComment.setText("")
                            images.forEach {
                                it.uri = null
                                it.imageView.setImageDrawable(null)
                            }
                            index = -1
                            mBinding.discussImageLayout.visibility = View.GONE
                            mBinding.pbar.visibility = View.INVISIBLE
                            mBinding.ivCommnetSnd.visibility = View.VISIBLE
                            mBinding.ivCommnetSnd.isClickable = true
                            mDiscussViewModel.addcommentstate.removeObservers(viewLifecycleOwner)
                        }
                        NetworkState.FAILED ->{
                            mBinding.pbar.visibility = View.GONE
                            mBinding.ivCommnetSnd.visibility = View.VISIBLE
                            mBinding.ivCommnetSnd.isClickable = true
                            mDiscussViewModel.addcommentstate.removeObservers(viewLifecycleOwner)
                        }
                    }

                })

            }
        }


        mDiscussViewModel.discussanswerlist.observe(viewLifecycleOwner, Observer {
             commentAdapter = DiscussanswerAdapter(it as ArrayList<DiscusAnswerDTO>)
            mBinding.ddCommentRv.adapter = commentAdapter

        })
    }

    private fun showDialog(image_url : String) {
        val mBinding = DialogDiscussImageViewBinding.inflate(LayoutInflater.from(requireActivity()))
        val builder = Dialog(requireActivity())
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        builder.setContentView(mBinding.root)
        builder.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.MATCH_PARENT);
        mBinding.imageViewCancel.setOnClickListener {
            builder.dismiss()
        }
        Glide.with(JobApp.instance.applicationContext).load(image_url).into(mBinding.imageViewId)


       /* builder.setCancelable(false)
        builder.setCanceledOnTouchOutside(false)*/
        builder.show()
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
            index++
            val uri = data?.data
            uri ?: return
            val selectedImageView = images[index]
            selectedImageView.uri = uri

            Log.d("Productdetails", selectedImageView.imageView.toString())
            mBinding.discussImageLayout.visibility = View.VISIBLE
            Glide.with(requireContext()).load(uri).into(selectedImageView.imageView)
        }

    }


}