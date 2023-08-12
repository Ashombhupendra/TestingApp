package com.dbvertex.job.peresentation.navigate_to_page.freelancer

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.dbvertex.job.MainActivity
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentFUProfileBinding
import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.peresentation.userprofile.ProfileAboutme
import com.dbvertex.job.peresentation.userprofile.ProfileReviews
import com.dbvertex.job.peresentation.userprofile.gallery.ProfileGallery
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class FUProfile : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var mBinding: FragmentFUProfileBinding
    private val mFUViewModel by activityViewModels<FreelencerUserViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFUProfileBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@FUProfile

        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)



        setAnimation(mBinding.profileMainContianer)

        Handler().postDelayed({
            mBinding.profileMainContianer.elevation = 0f
            val animationSlideUp = AnimationUtils.loadAnimation(
                JobApp.instance.applicationContext,
                R.anim.fade_in
            )
            mBinding.cvItemThis.animation = animationSlideUp
            mBinding.cvItemThis.visibility = View.VISIBLE
        }, 1500)



        mBinding.FUChat.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("receiver_id", mFUViewModel.mreceiverid.value.toString())
            findNavController().navigate(R.id.chatMessage, bundle)
        }
        mBinding.FUCalendar.setOnClickListener {
            findNavController().navigate(R.id.userSchedule)
        }
        mBinding.FUBook.setOnClickListener {
            Toast.makeText(requireContext(), "Book", Toast.LENGTH_SHORT).show()
        }
        mBinding.profileCancel.setOnClickListener {
            findNavController().navigateUp()
        }



        mBinding.icProfileFav.setOnClickListener {

            setFavUnFav()

        }
        mFUViewModel.favuser.observe(viewLifecycleOwner, Observer {
            if (it) {
                mBinding.icProfileFav.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                //   Picasso.get().load(R.drawable.ic_baseline_favorite_border_24).into(mBinding.icProfileFav)
                mBinding.icProfileFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        })


        val bundle = arguments
        if (bundle != null) {
            MainActivity.profileid.value = bundle.getString("profileid").toString()
            getHeader()
        }

        viewPager.adapter = FUPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> {
                    "About Me"
                }
                1 -> {
                    "Gallery"
                }

                else -> {
                    "Reviews"
                }
            }
        }.attach()

    }


    inner class FUPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

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

    fun getHeader() {
        val userid = SharedPrefrenceHelper.user.userid
        lifecycleScope.launch {
            val result = FreelancerProfileRepository.getHeader(
                MainActivity.profileid.value.toString(),
                userid.toString()
            )
            when (result) {
                is ResultWrapper.Success -> {

                    mBinding.FUUsername.text =
                        "${result.response.first_name} ${result.response.last_name}"
                    mBinding.FUCategory.text = result.response.category
                    Picasso.get().load(result.response.profile_back).into(mBinding.FUProfileImg1)
                    Picasso.get().load(result.response.profile_pic).into(mBinding.FUProfileImg2)

                    if (result.response.verified) {
                        mBinding.verifiedProfile.visibility = View.VISIBLE
                        mBinding.verifiedProfileTv.visibility = View.VISIBLE
                    } else {
                        mBinding.verifiedProfile.visibility = View.GONE
                        mBinding.verifiedProfileTv.visibility = View.GONE
                    }



                    mFUViewModel.mUsername.value =
                        "${result.response.first_name} ${result.response.last_name}"
                    mFUViewModel.mreceiverid.value = "${result.response.user_id}"
                    mFUViewModel.mProfileimage.value = "${result.response.profile_pic}"

                    mFUViewModel.favuser.value = result.response.favorite


                }
                is ResultWrapper.Failure -> {
                    Log.d("hello", result.errorMessage)
                }
            }
        }
    }


    private fun setAnimation(item: View) {

        val animationSlideUp =
            AnimationUtils.loadAnimation(JobApp.instance.applicationContext, R.anim.slide_up)
        /* val scaleXAnimator = ObjectAnimator.ofFloat(item, "scaleX", 1f, 1.5f, 1f)
         val scaleYAnimator = ObjectAnimator.ofFloat(item, "scaleY", 1f, 1.5f, 1f)
         val rotationAnimator = ObjectAnimator.ofFloat(item, "rotation", 0f, 0f, 0f)
         AnimatorSet().apply {
             duration = 1000
             interpolator = OvershootInterpolator()
             playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
             start()
         }*/
        item.animation = animationSlideUp
    }

    fun setFavUnFav() {
        val userid = SharedPrefrenceHelper.user.userid
        lifecycleScope.launch {
            val result = FreelancerProfileRepository.setFavUnFav(
                MainActivity.profileid.value.toString(),
                userid.toString()
            )

            when (result) {
                is ResultWrapper.Success -> {

                    mFUViewModel.favuser.value = result.response
                }
                is ResultWrapper.Failure -> {

                }
            }
        }
    }
}