package com.dbvertex.job.peresentation.userprofile

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
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.dbvertex.job.MainActivity
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.data.UserEntity
import com.dbvertex.job.databinding.FragmentProfileBinding

import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.utils.FileUtils
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.peresentation.userprofile.gallery.ProfileGallery
import com.dbvertex.job.peresentation.userprofile.updateprofile.UpdateProfileViewModel
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class ProfileFragment : Fragment() {
    private lateinit var mBinding: FragmentProfileBinding

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private val CAMERA_INTENT_CODE = 101

    private val GALLERY_INTENT_CODE = 100
    lateinit var bottomSheetDialog: BottomSheetDialog
    var profilemain: Boolean = true
    private val mUpdateProfileViewmodel by activityViewModels<UpdateProfileViewModel>()

    companion object {
        var profileid: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@ProfileFragment
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //getHeader()
        Log.d("User", SharedPrefrenceHelper.user.toString())
        mUpdateProfileViewmodel.loading.observe(viewLifecycleOwner) {
            if (it == true) {
                //mBinding.proBar.visibility=View.VISIBLE
            } else {
                //  mBinding.proBar.visibility=View.GONE

            }
        }









        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)
        val adapter = ProfilePagerAdapter(this)

        viewPager.adapter = adapter

        mBinding.profileCancel.setOnClickListener {

            findNavController().navigateUp()
        }
        // mUpdateProfileViewmodel.getUserPersonaldetail(SharedPrefrenceHelper.user.userid.toString())
        mBinding.pro.visibility = View.VISIBLE
        Handler().postDelayed(object : Runnable {
            override fun run() {
                mBinding.pro.visibility = View.GONE

                mBinding.myname.text =
                    "Name: " + mUpdateProfileViewmodel.firstname.value.toString() + " " + mUpdateProfileViewmodel.lastname.value.toString()
                mBinding.myadd.text = "Address: " + mUpdateProfileViewmodel.address.value.toString()
                mBinding.myabt.text =
                    "User Name: " + mUpdateProfileViewmodel.username.value.toString()

                // mUpdateProfileViewmodel.getProfessionalSingleUser()
                mBinding.mynub.text = SharedPrefrenceHelper.user.userid.toString()
                mBinding.mobile.text = mUpdateProfileViewmodel.mobile.value.toString()
                if (mUpdateProfileViewmodel.profile_pic != null) {
                    Log.d("profileimage", mUpdateProfileViewmodel.profile_pic.value.toString())
                    Glide.with(this@ProfileFragment).load(mUpdateProfileViewmodel.profile_pic.value)
                        .into(mBinding.userProfileImg)
                }
                if (mUpdateProfileViewmodel.profile_back != null) {
                    Log.d("profileimage", mUpdateProfileViewmodel.profile_back.value.toString())
                    Glide.with(requireContext()).load(mUpdateProfileViewmodel.profile_back.value)
                        .into(mBinding.proBg)
                }

            }
        }, 2000)



        mBinding.userProfileUploadImage.setOnClickListener {

            permission()
            mUpdateProfileViewmodel.pro_type.value = "profile"
            profilemain = true
        }

        mBinding.profileBackgroudEdit.setOnClickListener {
            permission()
            mUpdateProfileViewmodel.pro_type.value = "banner"
            profilemain = false
        }
        mBinding.proCalendar.visibility = View.GONE
        mBinding.proCalendar.setOnClickListener {
            findNavController().navigate(R.id.userSchedule)
        }

        MainActivity.profileid.value = SharedPrefrenceHelper.user.userid.toString()

        mUpdateProfileViewmodel.profiledeleted.observe(viewLifecycleOwner) {
            if (it) {
                // getHeader()
            }
        }


        mBinding.proMenu.setOnClickListener {
            val popup = PopupMenu(context, it)

            popup.apply {
                inflate(R.menu.profile_menu)

                setOnMenuItemClickListener {
                    onMenuClick(it.itemId)

                    true
                }
                show()
            }

        }
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            if (position == 0)
                tab.text = "About me"
        }.attach()
    }

    private fun onMenuClick(itemId: Int) {

        when (itemId) {
            R.id.edit_personal -> {
                // Toast.makeText(requireContext(), "Personal Profile", Toast.LENGTH_SHORT).show()
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.profileFragment, true)
                findNavController().popBackStack(R.id.profileFragment, true)
                findNavController().navigate(R.id.updatePersonalPro)
            }
            R.id.edit_professional -> {
                // Toast.makeText(requireContext(), "Professional Profile", Toast.LENGTH_SHORT).show()
                val user = SharedPrefrenceHelper.user
                Log.d("usershared", user.toString())
                if (user.registertype.equals("company")) {
                    findNavController().navigate(R.id.companyFragment)
                } else if (user.registertype.equals("other")) {
                    findNavController().navigate(R.id.otherSignup)
                } else {
                    findNavController().navigate(R.id.updatefreelencerpro)

                }
            }
            R.id.edit_gallery -> {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build()

                findNavController().navigate(R.id.galleryUploadImages, null, navOptions)
            }
        }

    }

    inner class ProfilePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = 3


        override fun createFragment(position: Int) = when (position) {
            0 -> {
                ProfileAboutme()
            }
            1 -> {
                ProfileGallery()
            }
            else -> {
                ProfileReviews()
            }

        }
    }

    private fun getHeader() {
        lifecycleScope.launch {
            val result = FreelancerProfileRepository.getHeader(
                SharedPrefrenceHelper.user.userid.toString(),
                "0"
            )
            when (result) {
                is ResultWrapper.Success -> {
                    SharedPrefrenceHelper.user = UserEntity(
                        result.response.user_id,
                        result.response.first_name + " " + result.response.last_name,
                        SharedPrefrenceHelper.user.registertype,
                        SharedPrefrenceHelper.user.phonenumber,
                        result.response.profile_pic,
                        result.response.verified
                    )
                    mBinding.freeProHeaderName.text =
                        "${result.response.first_name} ${result.response.last_name}"

                    if (result.response.category.isNullOrEmpty()) {
                        mBinding.freeCategoryName.visibility = View.GONE
                    } else {
                        mBinding.freeCategoryName.text = "${result.response.category}"

                    }
                    if (result.response.verified) {
                        mBinding.verifiedProfile.visibility = View.VISIBLE
                    } else {
                        mBinding.verifiedProfile.visibility = View.GONE
                    }
                    Picasso.get().load(result.response.profile_pic).into(mBinding.userProfileImg)
                    Picasso.get().load(result.response.profile_back).into(mBinding.proBg)

                    if (!result.response.profile_back.equals("http://thephototribe.in/assets/profile_back.jpg")) {
                        mUpdateProfileViewmodel.checkiamgetypeback.value = true
                    } else if (!result.response.profile_pic.equals("http://thephototribe.in/assets/thephototribeuser.png")) {
                        mUpdateProfileViewmodel.checkiamgetype.value = true
                    } else {
                        mUpdateProfileViewmodel.checkiamgetype.value = false
                    }

                    mUpdateProfileViewmodel.firstname.value = result.response.first_name
                    mUpdateProfileViewmodel.lastname.value = result.response.last_name
                    mUpdateProfileViewmodel.username.value = result.response.username
                }
                is ResultWrapper.Failure -> {
                    temp_showToast("failed")
                    Log.d("newflow", "error on profile page")
                }
            }
        }
    }


    private fun bottomDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())


        val bottomsheetview = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.bottom_sheet_dialog,
                view?.findViewById(R.id.bottom_sheet)
            ) as LinearLayout

        val buttoncamera = bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_camera)
        val buttonGallery = bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_gallery)

        val buttonaudio = bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_audio)
        val bottom_sheet_audio_tv =
            bottomsheetview.findViewById<TextView>(R.id.bottom_sheet_audio_tv)

        val bottom_sheet_document_tv =
            bottomsheetview.findViewById<TextView>(R.id.bottom_sheet_document_tv)
        val buttondocument = bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_document)

        val bottomSheet_vedio = bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_vedio)
        val bottom_sheet_vedio_tv =
            bottomsheetview.findViewById<TextView>(R.id.bottom_sheet_vedio_tv)

        val bottomSheet_delete_profile =
            bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_delete_profile)
        val bottomSheet_delete_profile_tv =
            bottomsheetview.findViewById<TextView>(R.id.bottomSheet_delete_profile_tv)




        bottomSheet_delete_profile_tv.visibility = View.VISIBLE
        bottomSheet_delete_profile.visibility = View.VISIBLE



        bottomSheet_vedio.visibility = View.GONE
        bottom_sheet_vedio_tv.visibility = View.GONE
        bottom_sheet_audio_tv.visibility = View.GONE
        buttondocument.visibility = View.INVISIBLE
        buttonaudio.visibility = View.GONE
        bottom_sheet_document_tv.visibility = View.INVISIBLE

        bottomSheetDialog.setContentView(bottomsheetview)
        bottomSheetDialog.show()

        bottomSheet_delete_profile.setOnClickListener {

            mUpdateProfileViewmodel.removeProfileImage()
            bottomSheetDialog.dismiss()
        }

        buttoncamera.setOnClickListener {
            getCameraImage()
            bottomSheetDialog.dismiss()
        }
        buttonGallery.setOnClickListener {

            getGalleryImage()
            bottomSheetDialog.dismiss()
        }


    }

    private fun getCameraImage() {
        val cameraIntent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivityForResult(cameraIntent, CAMERA_INTENT_CODE)
    }

    private fun getGalleryImage() {
        val gallery = Intent(Intent.ACTION_PICK).apply { this.type = "image/*" }
        startActivityForResult(gallery, GALLERY_INTENT_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_INTENT_CODE && data != null) {
            val uri = data.data
            uri ?: return

            requireActivity().contentResolver.openInputStream(uri)?.let { inputStream ->
                val name = FileUtils.getFile(requireContext(), uri).name
                val bytes = inputStream.readBytes()


                mUpdateProfileViewmodel.Image = bytes to name

                mUpdateProfileViewmodel.UploadProfileImage()
                if (profilemain) mBinding.userProfileImg.setImageURI(uri)
                else mBinding.proBg.setImageURI(uri)
                mUpdateProfileViewmodel.uploadprofilestate.observe(
                    viewLifecycleOwner,
                    Observer { state ->
                        when (state) {
                            NetworkState.SUCCESS -> {
                                val user = SharedPrefrenceHelper.user
                                SharedPrefrenceHelper.user = UserEntity(
                                    user.userid,
                                    phonenumber = user.phonenumber,
                                    userprofile = uri.toString(),
                                    username = user.username,
                                    registertype = user.registertype,
                                    userVerfiied = user.userVerfiied

                                )
                            }

                        }


                    })


            }


        } else if (requestCode == CAMERA_INTENT_CODE && data != null) {
            val uri = getImageUri(data.extras!!.get("data") as Bitmap)
            uri ?: return

            requireActivity().contentResolver.openInputStream(uri)?.let { inputStream ->
                val name = FileUtils.getFile(requireContext(), uri).name
                val bytes = inputStream.readBytes()

                mUpdateProfileViewmodel.Image = bytes to name

                mUpdateProfileViewmodel.UploadProfileImage()
                if (profilemain) mBinding.userProfileImg.setImageURI(uri)
                else mBinding.proBg.setImageURI(uri)

                mUpdateProfileViewmodel.uploadprofilestate.observe(
                    viewLifecycleOwner,
                    Observer { state ->
                        when (state) {
                            NetworkState.SUCCESS -> {
                                val user = SharedPrefrenceHelper.user
                                SharedPrefrenceHelper.user = UserEntity(
                                    user.userid,
                                    phonenumber = user.phonenumber,
                                    userprofile = uri.toString(),
                                    username = user.username,
                                    registertype = user.registertype,
                                    userVerfiied = user.userVerfiied

                                )
                            }


                        }


                    })

            }


        }
    }


    fun getImageUri(inImage: Bitmap): Uri? {

        val wrapper = ContextWrapper(JobApp.instance.applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "P_${System.currentTimeMillis()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.fromFile(file)
    }

    private fun permission() {
        Dexter.withContext(JobApp.instance.applicationContext)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                    if (report.areAllPermissionsGranted()) {
                        bottomDialog()
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

    override fun onDestroy() {
        super.onDestroy()

    }
}