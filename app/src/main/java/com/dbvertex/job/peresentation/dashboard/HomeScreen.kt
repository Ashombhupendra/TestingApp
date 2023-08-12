package com.dbvertex.job.peresentation.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dbvertex.job.MainActivity
import com.dbvertex.job.R
import com.dbvertex.job.data.SliderItem
import com.dbvertex.job.databinding.FragmentHomeScreenBinding
import com.dbvertex.job.network.repository.AuthRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.dashboard.searchfreelancers.Search_freelancer_item_list
import com.dbvertex.job.peresentation.navigate_to_page.blog.BlogsPage
import com.dbvertex.job.peresentation.navigate_to_page.discuss.DiscussPage
import com.dbvertex.job.peresentation.navigate_to_page.discuss.DiscussViewModel
import com.dbvertex.job.peresentation.navigate_to_page.education.EducationPage
import com.dbvertex.job.peresentation.navigate_to_page.freelancer.FreelacerPage
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch


class HomeScreen : Fragment() {
    private lateinit var mBinding: FragmentHomeScreenBinding
    private val mDiscussViewModel by activityViewModels<DiscussViewModel>()
    var slideritemlist = ArrayList<SliderItem>()
    var sf_list = ArrayList<Search_freelancer_item_list>()
    var ec_p_list = ArrayList<Search_freelancer_item_list>()
    var ec_c_list = ArrayList<Search_freelancer_item_list>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeScreenBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@HomeScreen
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        navigateToPage()

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    activity!!.finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        mBinding.freenlanceTablayout.setOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (mBinding.freenlanceTablayout.selectedTabPosition) {
                    0 -> {
                        MainActivity.onEducationScreenBoolean.value = false
                        mBinding.freenlanceTablayout.apply {
                            backgroundTintList =
                                context.getResources().getColorStateList(R.color.white)
                        }
                        mBinding.freenlanceTablayout.setTabTextColors(
                            Color.parseColor("#a9a9a9"),
                            Color.parseColor("#223A54")
                        );


                    }
                    1 -> {
                        MainActivity.onEducationScreenBoolean.value = true
                        mBinding.freenlanceTablayout.apply {
                            backgroundTintList =
                                context.getResources().getColorStateList(R.color.dark_gray)
                        }

                        mBinding.freenlanceTablayout.setTabTextColors(
                            Color.parseColor("#a9a9a9"),
                            Color.parseColor("#ffffff")
                        );
                    }
                    2 -> {
                        MainActivity.onEducationScreenBoolean.value = false
                        mBinding.freenlanceTablayout.apply {
                            backgroundTintList =
                                context.getResources().getColorStateList(R.color.white)
                        }
                        mBinding.freenlanceTablayout.setTabTextColors(
                            Color.parseColor("#a9a9a9"),
                            Color.parseColor("#223A54")
                        );

                    }
                    else -> {
                        MainActivity.onEducationScreenBoolean.value = false
                        mBinding.freenlanceTablayout.apply {
                            backgroundTintList =
                                context.getResources().getColorStateList(R.color.white)
                        }
                        mBinding.freenlanceTablayout.setTabTextColors(
                            Color.parseColor("#a9a9a9"),
                            Color.parseColor("#223A54")
                        );

                    }

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val currentToken = it.result
                currentToken?.let { token ->
                    Log.d("token", token)
                    updatetoken(token)


                }
            }
        }

        val adapter = FreelancerPagerAdapter(this)
        mBinding.freelanceViewPager.adapter = adapter
        mBinding.freelanceViewPager.isUserInputEnabled = false
        TabLayoutMediator(
            mBinding.freenlanceTablayout,
            mBinding.freelanceViewPager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> {
                    "Freelancers"
                }
                1 -> {
                    "Education"
                }
                2 -> {
                    "Discuss"
                }

                else -> {
                    "Blog"
                }
            }
        }.attach()

        /*  loadFragment(FreelacerPage())
          mBinding.freelancerDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.blue_dark))
          mBinding.blogDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
          mBinding.discussDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
          mBinding.educationDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))

  */
        val user = SharedPrefrenceHelper.user
        Log.d("usercompany", user.toString())

    }


    fun navigateToPage() {

        /* mBinding.freelancerDashBoard.setOnClickListener {
            loadFragment(FreelacerPage())
             mDiscussViewModel.homescreenfragposition.value = 0
             mBinding.freelancerDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.blue_dark))
             mBinding.blogDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
             mBinding.discussDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
             mBinding.educationDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
         }
         mBinding.blogDashBoard.setOnClickListener {
             loadFragment(BlogsPage())
             mDiscussViewModel.homescreenfragposition.value = 1

             mBinding.freelancerDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
             mBinding.blogDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.blue_dark))
             mBinding.discussDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
             mBinding.educationDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
         }
         mBinding.discussDashBoard.setOnClickListener {
             mDiscussViewModel.homescreenfragposition.value = 2


             loadFragment(DiscussPage())
             mDiscussViewModel.discussFavboolean.value = true
             mBinding.freelancerDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
             mBinding.blogDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
             mBinding.discussDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.blue_dark))
             mBinding.educationDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
         }
         mBinding.educationDashBoard.setOnClickListener {
             mDiscussViewModel.homescreenfragposition.value = 3

             loadFragment(EducationPage())
             mBinding.freelancerDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
             mBinding.blogDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
             mBinding.discussDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
             mBinding.educationDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.blue_dark))
         }
 */
    }


    private fun loadFragment(fragment: Fragment) {
        /* val transaction = parentFragmentManager.beginTransaction()
         transaction.replace(R.id.frame_layout, fragment)
         transaction.disallowAddToBackStack()
         transaction.commit()*/
    }

    private fun updatetoken(token: String) {
        val userphone = SharedPrefrenceHelper.user.phonenumber
        lifecycleScope.launch {
            val result = AuthRepository.updatetoke(userphone.toString(), token)
            when (result) {
                is ResultWrapper.Success -> {
                    Log.d("token", result.response.toString())
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
        MainActivity.updateornot = true
        /*  when(mDiscussViewModel.homescreenfragposition.value){
              0 ->{
                  loadFragment(FreelacerPage())
                  mBinding.freelancerDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.blue_dark))
                  mBinding.blogDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
                  mBinding.discussDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
                  mBinding.educationDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))

              }
              1->{
                  loadFragment(BlogsPage())
                  mBinding.freelancerDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
                  mBinding.blogDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.blue_dark))
                  mBinding.discussDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
                  mBinding.educationDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
              }
              2 ->{
                  loadFragment(DiscussPage())
                  mBinding.freelancerDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
                  mBinding.blogDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
                  mBinding.discussDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.blue_dark))
                  mBinding.educationDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
              }
              else ->{
                  loadFragment(EducationPage())
                  mBinding.freelancerDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
                  mBinding.blogDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
                  mBinding.discussDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.grey))
                  mBinding.educationDashBoard.setTextColor(context?.resources?.getColorStateList(R.color.blue_dark))
              }
          }*/
    }

    inner class FreelancerPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = 4


        override fun createFragment(position: Int) = when (position) {
            0 -> {

                FreelacerPage()
            }
            1 -> {
                EducationPage()
            }
            2 -> {
                DiscussPage(true)
            }
            else -> {
                BlogsPage()
            }

        }
    }
}