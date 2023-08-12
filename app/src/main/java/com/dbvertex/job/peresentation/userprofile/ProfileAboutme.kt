package com.dbvertex.job.peresentation.userprofile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.dbvertex.job.MainActivity
import com.dbvertex.job.databinding.FragmentProfileAboutmeBinding
import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import kotlinx.coroutines.launch


class ProfileAboutme : Fragment() {

    private lateinit var mBinding: FragmentProfileAboutmeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileAboutmeBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@ProfileAboutme

        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (MainActivity.profileid.value != null) {
            getAboutme()

        } else {
            Log.d("profileid", ProfileFragment.profileid)
        }
    }


    fun getAboutme() {
        lifecycleScope.launch {
            val result =
                FreelancerProfileRepository.getAboutMe(MainActivity.profileid.value.toString())

            when (result) {
                is ResultWrapper.Success -> {
                    if (result.response.user_category.equals("company")) {
                        mBinding.aboutmetitle.text = "${result.response.user_company_about}"
                        mBinding.activepassport.visibility = View.GONE

                    } else if (result.response.user_category.equals("other")) {
                        mBinding.aboutmetitle.text = "${result.response.user_other_review}"
                        mBinding.activepassport.visibility = View.GONE

                    } else {
                        mBinding.aboutmetitle.text = "${result.response.Aboutme}"

                    }

                    mBinding.address.text = "${result.response.Manual_Location}"

                    if (result.response.passport == 0) {
                        mBinding.activepassport.text = "Have an UnActive Passport"

                    }


                }
                is ResultWrapper.Failure -> {
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        getAboutme()
    }
}