package com.dbvertex.job.peresentation.introduction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.JobApp
import com.dbvertex.job.data.IntroItem
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.fragment_intro.*


class IntroFragment : Fragment() {

   var introitemlist = ArrayList<IntroItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SharedPrefrenceHelper.isIntro = true
        val viewpagercustom = view.findViewById<CustomViewPager>(R.id.view_pager_intro)
         introitemlist.clear()
        introitemlist.add(
                IntroItem(
                        "https://images.pexels.com/photos/821749/pexels-photo-821749.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                       "Welcome! \n\nTo the all in one app for modern photographers with big dreams.",
                        ""
                )
        )
        introitemlist.add(IntroItem(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzqRyIcLEundWljdBGyUmwwMTCyAPao2SKMP0-_drGJJmG7ZFgd5yyZonIZq1FDUDJloA&usqp=CAU",
                "SEARCH FREELANCERS ",
                "Discover freelancers from all across India. \nFind trusted professional like-minded photography & Cinemotography " +
                        "related creatives."
        ))
        introitemlist.add(
                IntroItem(
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQh_kZ_13SxpMr3tQBqYAxUvflSpuAB4sKVHA&usqp=CAU",
                        "Assignments/Jobs",
                        "A Common platform for companies and freelancer creatives to know and work with eachother. \n\n" +
                                "Connect and work with the leadings brands and creatives in industry. \n\nGet verified jobs in your own expertise."

                )
        )
        introitemlist.add(IntroItem(
                "https://i.pinimg.com/originals/af/b9/3a/afb93afce89db47589cf4127d47e9cc8.jpg",
                "Learn ",
                "No-bullshit content \n\nSkip the expensive online courses and learn everthing that you need to grow your skillset and business with ever growing library." +
                        "\n\nContent to make you the best version of yourself"
        ))
        introitemlist.add(IntroItem(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5tXlybutnBJ7QHQEKgJrB1VdTE_r79H1kRg&usqp=CAU",
                "Discuss",
                "Discuss all your questions and talk photography with our community." +
                        "\n\nOur positive community is all about supporting each other and becoming better photographers  along the way."
        ))
        introitemlist.add(IntroItem(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5tXlybutnBJ7QHQEKgJrB1VdTE_r79H1kRg&usqp=CAU",
                "Resources",
                "Game changing resources to help every individual and company." +
                        "\n\nWe will help you to get the most irresistible offers and in establishing  your own business that you can never dream of doing alone"
        ))
        introitemlist.add(IntroItem(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJZ6D6YNtIcdVIPf_0KIZxj-ApxY_iGmiANA&usqp=CAU",
                "Poses",
                "Get inspired and show up prepared to capture the raw, emotion-filled photos and videos you and your clients love." +
                        "\n\nWith new content added regularly , you'll never run out of ideas."
        ))
        introitemlist.add(IntroItem(
                "https://i.pinimg.com/originals/72/1a/3e/721a3eb2ef1aa90467995034fe098ca9.jpg",
                "Inspiration",
                "Get inspired with messaging ideas and work from world's most creative minds." +
                        "\n\nIdeas to challenge and enhance your daily creative needs"
        ))
        introitemlist.add(IntroItem(
                "https://media.weddingz.in/images/5653bd56fe7b640a08e784b1fbb14e98/pre-wedding-photo-shoot-ideas-captured-by-lightarts-photography.jpg",
                "Manage your clients",
                "Make youself stand out of the crowd with our business centric tools." +
                        "\n\nKeep client details organized and sleep well knowing your important info will never go missing." +
                        "\n\nWith the click of a button share invoices, custom made contracts, checklists, emails, etc to your client "
        ))

        val adpter = IntroPagerAdapter(JobApp.instance.applicationContext, introitemlist)

        viewpagercustom.adapter = adpter
        val tabLayout = view.findViewById<WormDotsIndicator>(R.id.intro_worm_dots_indicator)
        tabLayout.setViewPager(viewpagercustom)

          intro_skip.setOnClickListener {
              findNavController().navigate(R.id.loginFragment)
          }



    }

}