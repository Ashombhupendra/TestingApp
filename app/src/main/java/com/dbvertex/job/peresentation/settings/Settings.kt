package com.dbvertex.job.peresentation.settings

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.data.UserEntity
import com.dbvertex.job.databinding.FragmentSettingsBinding
import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.peresentation.userprofile.updateprofile.UpdateProfileViewModel
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class Settings : Fragment() {
    private lateinit var mBinding: FragmentSettingsBinding
    private val mUpdateProfileViewmodel by activityViewModels<UpdateProfileViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSettingsBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@Settings

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //getHeader()
        mBinding.pbar.visibility = View.VISIBLE
        mUpdateProfileViewmodel.getUserPersonaldetail(SharedPrefrenceHelper.user.userid.toString())
        Handler().postDelayed(object : Runnable {
            override fun run() {
                if (mUpdateProfileViewmodel.profile_pic != null) {

                    if (mUpdateProfileViewmodel.profile_pic.toString() == "http://thephototribe.in/assets/thephototribeuser.png") {
                        Glide.with(requireContext()).load(R.drawable.user)
                            .into(mBinding.settingProfileIi)

                    } else {
                        Log.d("profileimage", mUpdateProfileViewmodel.profile_pic.value.toString())
                        Glide.with(requireContext()).load(mUpdateProfileViewmodel.profile_pic.value)
                            .into(mBinding.settingProfileIi)

                    }
                }
                if (mUpdateProfileViewmodel.profile_back != null) {
                    Log.d("profileimage", mUpdateProfileViewmodel.profile_back.value.toString())
                    Glide.with(requireContext()).load(mUpdateProfileViewmodel.profile_back.value)
                        .into(mBinding.settingProfileI)
                }
                mBinding.pbar.visibility = View.GONE
            }
        }, 2000)

        mBinding.settingLogout.setOnClickListener {

            findNavController().navigate(R.id.logout)
        }
        mBinding.profileCancel.setOnClickListener {

            mBinding.settingProfileI.animate().translationY(500f)

            Handler().postDelayed(object : Runnable {
                override fun run() {
                    findNavController().navigateUp()
                }
            }, 1000)

        }

        mBinding.settingProfileIi.setOnClickListener {


            findNavController().navigate(R.id.profileFragment)
        }
        mBinding.proMenu.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
        mBinding.termsCondition.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 3)
            findNavController().navigate(R.id.webviewFragment, bundle)
        }
        mBinding.settingAboutus.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 1)
            findNavController().navigate(R.id.webviewFragment, bundle)
        }
        mBinding.settingPrivacy.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 2)
            findNavController().navigate(R.id.webviewFragment, bundle)
        }
        mBinding.settingFeedback.setOnClickListener {
            findNavController().navigate(R.id.contact_Us)
        }
        mBinding.settingTellusfriend.setOnClickListener {
            shareApp()
        }
    }

    private fun getHeader() {
        lifecycleScope.launch {
            val result = FreelancerProfileRepository.getHeader(
                SharedPrefrenceHelper.user.userid.toString(), "0"
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
                    Picasso.get().load(result.response.profile_back).into(mBinding.settingProfileI)
                    Picasso.get().load(result.response.profile_pic).into(mBinding.settingProfileIi)
                    mBinding.settingUsername.text =
                        "${result.response.first_name} ${result.response.last_name}"

                    if (result.response.verified) {
                        // mBinding.verifiedProfile.visibility =View.VISIBLE
                        // mBinding.verifiedProfileTv.visibility =View.VISIBLE
                    } else {
                        // mBinding.verifiedProfile.visibility =View.GONE
                        // mBinding.verifiedProfileTv.visibility =View.GONE
                    }
                    if (result.response.category.isNullOrEmpty()) {
                        mBinding.settingUserCategory.visibility = View.GONE
                    }
                    val user = SharedPrefrenceHelper.user
                    mBinding.settingUserCategory.text =
                        "${result.response.category} -  ${user.registertype}"
                }
                is ResultWrapper.Failure -> {

                }
            }
        }
    }


    fun shareApp() {
        showSnackBar("Working on this..")
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.type = "text/plain"
//        val app_url = "https://play.google.com/store/apps/details?id=com.app.phototribe"
//        shareIntent.putExtra(Intent.EXTRA_TEXT, app_url)
//        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }


}