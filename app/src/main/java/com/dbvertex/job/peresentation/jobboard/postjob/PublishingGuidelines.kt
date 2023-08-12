package com.dbvertex.job.peresentation.jobboard.postjob

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.databinding.FragmentPublishingGuidelinesBinding

class PublishingGuidelines : Fragment() {
        private lateinit var mBinding : FragmentPublishingGuidelinesBinding
         val pubshingguide = "<p style=\"text-align: center;\"><strong>The Photo Mentor App</strong> is a mobile application for the best Photography/Cinematography/Editing artists throughout the world and providing the world class resources to upgrade everyone related to our industry. This app is to remove the friction and struggles of the craft and business of photography Industry.</p>\n" +
                 "<p style=\"text-align: center;\">RULES:</p>\n" +
                 "<p style=\"text-align: center;\">JOBS/APPLICATION/REQUEST MAY BE REMOVED OR EDITED AT THE DISCRETION OF THE EDITORIAL STAFF WITH NO WARNING OR NOTICE. ALL REMOVAL AND EDIT DECISIONS ARE FINAL.</p>\n" +
                 "<p style=\"text-align: center;\">MEMBERS may post, apply, request, decline any job/as-signment.</p>\n" +
                 "<p style=\"text-align: center;\">Our publishing guidelines prohibit the posting of advertisements of personal brand/studio/camera/work. As this section is for exchanging work/assignment/-job/etc.</p>\n" +
                 "<p style=\"text-align: center;\">&nbsp;Unnecessary posts may lead to unwanted traffic which lead to confusion and unhealthy environment&nbsp; for quality leads.</p>\n" +
                 "<p style=\"text-align: center;\">POSTS MAY BE REMOVED OR EDITED AT THE DISCRETION OF THE EDITORIAL STAFF WITH NO WARNING OR NOTICE. ALL REMOVAL AND EDIT DECISIONS ARE FINAL.</p>"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPublishingGuidelinesBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.closeGuidelines.setOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.guidelineDescription.text = Html.fromHtml(pubshingguide)
    }


}