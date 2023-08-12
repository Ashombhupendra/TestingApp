package com.dbvertex.job.peresentation.photoshoot

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dbvertex.job.MainActivity
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPhotoShootPosesBinding
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.response.photoshoot.getPhotoshootPosesImagesDTO
import com.dbvertex.job.network.utils.*
import com.dbvertex.job.peresentation.photoshoot.poses.PhotoshootPosesPaginAdapter
import com.dbvertex.job.peresentation.photoshoot.poses.onPhotoshootPosesClick
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class PhotoShootPoses : Fragment(), onPhotoshootPosesClick {
          private lateinit var mBinding : FragmentPhotoShootPosesBinding
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()
    private var job: Job? = null
    private val CAMERA_INTENT_CODE = 101
    private val GALLERY_INTENT_CODE = 100
    private var from_poses : Boolean = false
   private  val adapter = PhotoshootPosesPaginAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPhotoShootPosesBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@PhotoShootPoses
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        job?.cancel()
        job = lifecycleScope.launch {
            mPhotoshootViewModel.getPhotoShootPosesImages().collectLatest {
                adapter.submitData(it)
            }
        }
        Glide.with(view).load(R.raw.progress_gif)
            .into(mBinding.posesLoading)
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                Log.d("adapteri", adapter.itemCount.toString())
                if (adapter.itemCount == 0 ){
                    mBinding.noPosesFound.visibility = View.VISIBLE
                }else{
                    mBinding.noPosesFound.visibility = View.GONE
                }

                handler.postDelayed(this, 500)
            }
        }, 500)
        mPhotoshootViewModel.posesadapterrefresh.observe(viewLifecycleOwner){
            if (it){


                Thread.sleep(1000)
                adapter.refresh()

                mBinding.posesLoading.visibility = View.GONE
                 mPhotoshootViewModel.posesadapterrefresh.value = false

            }
        }

        mBinding.dualFabAdd.setOnClickListener {
            if (mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
                Toast.makeText(JobApp.instance.applicationContext, "Firstly Create Photoshoot", Toast.LENGTH_SHORT).show()
            }else{
                if (mBinding.addPosesContaine.isVisible){
                    mBinding.dualFabAdd.apply {
                        rotation = 0f
                    }
                    mBinding.addPosesContaine.visibility = View.GONE
                }else{
                    mBinding.dualFabAdd.apply {
                        rotation = 135f
                    }
                    mBinding.addPosesContaine.visibility = View.VISIBLE
                }
            }

        }

        mBinding.posesFromCamera.setOnClickListener {
            permission(1)
            mBinding.addPosesContaine.visibility = View.GONE
            mBinding.dualFabAdd.apply {
                rotation = 0f
            }
        }
        mBinding.posesFormGallery.setOnClickListener {
            permission(0)
            mBinding.addPosesContaine.visibility = View.GONE
            mBinding.dualFabAdd.apply {
                rotation = 0f
            }
        }

        mBinding.posesFromPoses.setOnClickListener {
            from_poses = true
            MainActivity.posesaddingprocessenable.value = true
            findNavController().navigate(R.id.posesHome)
            mBinding.addPosesContaine.visibility = View.GONE
            mBinding.dualFabAdd.apply {
                rotation = 0f
            }
        }




        mBinding.photoshootPosesRv.layoutManager = GridLayoutManager(context, 2)
        mBinding.photoshootPosesRv.adapter = adapter
    }

//deleting
    override fun onClick(photoshootPosesImagesDTO: getPhotoshootPosesImagesDTO) {
        lifecycleScope.launch {
            val result = PhotoShootRepository.deletePosesImage(photoshootPosesImagesDTO.id)

            when(result ){
                is ResultWrapper.Success ->{
                 //   mPhotoshootViewModel.getPhotoShootPosesImages()
                    adapter.refresh()
                }
                is ResultWrapper.Failure ->{
                       temp_showToast("${result.errorMessage}")
                }
            }
        }
    }
    private fun getCameraImage(){

        val cameraIntent = Intent("android.media.action.IMAGE_CAPTURE")


        startActivityForResult(cameraIntent, CAMERA_INTENT_CODE)

    }
    private fun getGalleryImage(){
        val gallery = Intent(Intent.ACTION_PICK).apply {
            this.type = "image/*"
        }
        startActivityForResult(gallery, GALLERY_INTENT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_INTENT_CODE && data!= null ){
            val uri =getImageUri(data.extras!!.get("data") as Bitmap)
            uri ?: return

            requireActivity().contentResolver.openInputStream(uri)?.let { inputStream ->
                val name = FileUtils.getFile(requireContext(), uri).name
                val bytes = inputStream.readBytes()
                mPhotoshootViewModel.PosesImage = bytes to name
                mPhotoshootViewModel.postPosesImage()
                mBinding.posesLoading.visibility = View.VISIBLE

            }

        }else if (requestCode == GALLERY_INTENT_CODE && data!= null) {
            val uri = data.data
            uri ?: return

            requireActivity().contentResolver.openInputStream(uri)?.let { inputStream ->
                val name = FileUtils.getFile(requireContext(), uri).name
                val bytes = inputStream.readBytes()

                mPhotoshootViewModel.PosesImage = bytes to name

                 mPhotoshootViewModel.postPosesImage()
                mBinding.posesLoading.visibility = View.VISIBLE

            }

        }

    }
    fun getImageUri(inImage: Bitmap): Uri? {

        val wrapper = ContextWrapper(JobApp.instance.applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "P_${System.currentTimeMillis()}.jpg")

        try{
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        return Uri.fromFile(file)
    }

    private fun permission(type : Int){
        Dexter.withContext(JobApp.instance.applicationContext)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                    if (report.areAllPermissionsGranted()) {
                         if (type ==1){
                             getCameraImage()
                         }else{
                             getGalleryImage()
                         }
                    } else {
                        showRationalDialogForPermissions()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                    token?.continuePermissionRequest()
                    showRationalDialogForPermissions()
                }
            }).check()
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
                val uri = Uri.fromParts("package", "com.app.phototribe", null)
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


    override fun onPause() {
        super.onPause()
        Log.d("poseslist", "Pause")

    }

    override fun onResume() {
        super.onResume()
        Log.d("poseslist", "Resume")

    }
}