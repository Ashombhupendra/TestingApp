package com.dbvertex.job.peresentation.userprofile

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dbvertex.job.peresentation.userprofile.gallery.ProfileGallery

class ProfileFragmentAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
       when(position){
           0 ->{
              return ProfileAboutme()
           }
           1 ->{
               return  ProfileResources()
           }
           2 ->{
               return  ProfileGallery()
           }
           else ->{
               return  ProfileReviews()
           }



       }
    }

    override fun getCount(): Int {
        return  totalTabs
    }
}