package com.dbvertex.job.peresentation.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dbvertex.job.databinding.FragmentFavoritesHomeBinding
import com.dbvertex.job.peresentation.navigate_to_page.blog.podcast.Podcast
import com.dbvertex.job.peresentation.navigate_to_page.discuss.DiscussPage
import com.dbvertex.job.peresentation.navigate_to_page.discuss.DiscussViewModel
import com.dbvertex.job.peresentation.navigate_to_page.freelancer.FreelancerUsers
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FavoritesHome : Fragment() {

    private lateinit var mBinding : FragmentFavoritesHomeBinding
    private val mDiscussViewModel by activityViewModels<DiscussViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View {
        mBinding = FragmentFavoritesHomeBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@FavoritesHome

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ProfilePagerAdapter(this)
        mBinding.favViewPager.adapter = adapter


        mDiscussViewModel.discussFavboolean.value = false
       /* JobApp.FavSlideBoolean.observe(viewLifecycleOwner){
            if (it){

            }else{

            }
        }*/
        mBinding.favTablayout.setOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
               Log.d("Favlog", "Position : ${tab!!.position}")
                /*if (tab!!.position == 5){
                    mBinding.favViewPager.isUserInputEnabled = false
                }else{
                    mBinding.favViewPager.isUserInputEnabled = true
                }*/
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }

        })

        TabLayoutMediator(mBinding.favTablayout, mBinding.favViewPager) { tab, position ->
            tab.text = when(position){
                0 ->{
                    "Freelancer"
                }
                1 ->{
                    "Education"
                }
                2 ->{
                    "Discuss"
                }
                3 ->{
                    "Blog"
                }
                4 ->{
                    "Podcast"
                }

                else ->{
                    "Post"
                }
            }
        }.attach()
    }



    inner class ProfilePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = 6



        override fun createFragment(position: Int) =   when(position){
            0 ->{

                FreelancerUsers()
            }
            1 ->{
                FavEducation()
            }
            2 ->{
                DiscussPage(false)
            }
            3 ->{
                FavBlog()
            }
            4 ->{
                Podcast()
            }

            else ->{
                FavInspiration()
            }

        }
    }


}