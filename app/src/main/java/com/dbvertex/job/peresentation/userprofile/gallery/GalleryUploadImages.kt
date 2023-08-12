package com.dbvertex.job.peresentation.userprofile.gallery

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.DialogDiscussImageViewBinding
import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.launch
import java.io.File

class GalleryUploadImages : Fragment(), onRVItemClick {
    val REQUEST_CODE = 200
    val list = ArrayList<GalleryModel>()
    val imagelist = MutableLiveData<List<GalleryModel>>()
    private lateinit var adapter: GalleryAdapter
    private lateinit var upload  : TextView
    val images: MutableList<Pair<ByteArray, String>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_upload_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         val back = view.findViewById<ImageView>(R.id.gallery_back)
        back.setOnClickListener {
            findNavController().navigateUp()
        }
        val recyclerview = view.findViewById<RecyclerView>(R.id.up_galleryimages_rv)
        adapter = GalleryAdapter(list, requireContext(), this)
        recyclerview.adapter = adapter


        val text  = view.findViewById<ImageView>(R.id.add_more_images)
        text.setOnClickListener {
            checkPermession()
        }

         upload  = view.findViewById<TextView>(R.id.upload_image)
        upload.setOnClickListener {
               submitimages()
        }
    }

    fun submitimages(){
        list.forEach {
            it.imageView.let { uri ->
                requireActivity().contentResolver.openInputStream(uri)?.let { inputStream ->
                   // val name = FileUtils.getFile(requireContext(), uri).name

                    val wrapper = ContextWrapper(JobApp.instance.applicationContext)
                    var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
                    file = File(file, "P_${System.currentTimeMillis()}.jpg")

                    val bytes = inputStream.readBytes()

                    images.add(bytes to file.name)

                }
            }
        }
        lifecycleScope.launch {

            upload.apply {
                showProgress()
                isClickable = false
            }
            val result = FreelancerProfileRepository.uploadImages(images)
            when(result){
                is ResultWrapper.Success ->{
                    upload.apply {
                        hideProgress("Upload Image")
                        isClickable = true
                    }
                    findNavController().navigate(R.id.profileFragment)
                    Log.d("uploadimages", result.response.toString())
                }
                is ResultWrapper.Failure ->{
                    Log.d("uploadimages", result.errorMessage.toString())
                    upload.apply {
                        hideProgress("Retry")
                        isClickable = true
                    }

                }
            }
        }
    }

    private fun openGalleryForImages() {
        if (Build.VERSION.SDK_INT < 19) {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(
                Intent.createChooser(intent, "Choose Pictures"), REQUEST_CODE
            )
        } else { // For latest versions API LEVEL 19+
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){

            // if multiple images are selected
            if (data?.getClipData() != null) {
                val count = data.clipData!!.itemCount

                for (i in 0..count - 1) {
                    var imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    list.add(
                        GalleryModel(
                            "",imageUri
                        )
                    )
                }

                  adapter.notifyDataSetChanged()

            } else if (data?.getData() != null) {
                // if single image is selected

                val imageUri: Uri? = data.data
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview
                list.add(GalleryModel("",imageUri!!))
                imagelist.postValue(list)
                  adapter.notifyDataSetChanged()
            }
        }


    }


    fun checkPermession() {
        Dexter.withContext(requireContext()).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        openGalleryForImages()
                    } else {
                        showRationalDialogForPermissions()
                    }


                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()

    }
    private fun showRationalDialogForPermissions() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        // set alert dialog message text color
        alertDialog.setTitle("Need Permissions")
        val message =
            SpannableString("This app needs permission to use this feature. You can grant them in app settings.")
        message.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            message.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton(
            "GO TO SETTINGS"
        ) { _, _ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", "com.pahadi.uncle", null)
                intent.data = uri
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
        alertDialog.setNegativeButton(
            "CANCEL"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    override fun onReclyclerItemClicklistner(galleryModel: GalleryModel, itemView: View,position: Int) {
        list.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    override fun onImageCLick(galleryModel: GalleryModel) {
        showDialog(galleryModel.imageView)
    }

    private fun showDialog(image_url : Uri) {
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

}