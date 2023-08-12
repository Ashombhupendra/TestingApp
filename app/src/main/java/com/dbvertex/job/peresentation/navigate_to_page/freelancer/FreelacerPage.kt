package com.dbvertex.job.peresentation.navigate_to_page.freelancer

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.dbvertex.job.JobApp
import com.dbvertex.job.data.EditorChoiceModel
import com.dbvertex.job.data.SliderItem
import com.dbvertex.job.databinding.FragmentFreelacerPageBinding
import com.dbvertex.job.network.repository.FreeLancerRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.peresentation.dashboard.searchfreelancers.SearchFreelancersAdapter
import com.dbvertex.job.peresentation.dashboard.searchfreelancers.Search_freelancer_item_list
import com.dbvertex.job.peresentation.dashboard.searchfreelancers.navigatetosearch
import com.dbvertex.job.peresentation.dashboard.slider.CustomSliderAdapter
import com.dbvertex.job.peresentation.dashboard.slider.onBannerClick
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.launch
import com.dbvertex.job.R
import com.dbvertex.job.peresentation.dashboard.slider.SliderAdapter

class FreelacerPage : Fragment(), navigatetosearch, onBannerClick {
       private lateinit var mBinding : FragmentFreelacerPageBinding
    private val mFUViewModel by activityViewModels<FreelencerUserViewmodel>()
     var currentItem : Int = 0

    var slideritemlist = ArrayList<SliderItem>()
    var sf_list = ArrayList<Search_freelancer_item_list>()
    var ec_p_list = ArrayList<Search_freelancer_item_list>()
    var ec_c_list = ArrayList<Search_freelancer_item_list>()
    val sliderlist1 = MutableLiveData<List<Search_freelancer_item_list>>()
    val Cinematographylist = MutableLiveData<List<Search_freelancer_item_list>>()
    val dronelist = MutableLiveData<List<Search_freelancer_item_list>>()
    val Retoucherlist = MutableLiveData<List<Search_freelancer_item_list>>()
    val Editorlist = MutableLiveData<List<Search_freelancer_item_list>>()
    val Shootinglist = MutableLiveData<List<Search_freelancer_item_list>>()
    val Rentalhouses = MutableLiveData<List<Search_freelancer_item_list>>()
    private lateinit var slideradapter: CustomSliderAdapter
    private var listitemcount = 0
    val sliderlsit =  MutableLiveData<List<SliderItem>>()
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
        mBinding = FragmentFreelacerPageBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@FreelacerPage

        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        searchfreelancerrv()
        getEditorChoice()
        getBanners()
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 300 //You can manage the blinking time with this parameter
        anim.startOffset = 20
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        mBinding.nextCategory.startAnimation(anim)
        sliderlsit.observe(viewLifecycleOwner, Observer {
            val adapter = SliderAdapter(JobApp.instance.applicationContext, it, this)
            mBinding.headerSlider.adapter = adapter
            adapter.notifyDataSetChanged()
            mBinding.dotsIndicator.setViewPager(mBinding.headerSlider)
        })

       /* mBinding.cvHeaderSlider.clipChildren = false
        mBinding.cvHeaderSlider.clipToPadding = false
        mBinding.cvHeaderSlider.offscreenPageLimit = 3
        mBinding.cvHeaderSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(30))
        compositePageTransformer.addTransformer { page, position ->
            page.apply {
                when {
                    position < -1 -> {
                        // This page is way off-screen to the left.
                        alpha = 0.7f

                    }
                    position <= 1 -> {
                       alpha = 1f
                        overlay.clear()
                    }
                    else -> {
                        // This page is way off-screen to the right.
                        alpha = 0.7f

                    }
                }
            }
        }
        mBinding.cvHeaderSlider.setPageTransformer(compositePageTransformer)

        mBinding.cvHeaderSlider.registerOnPageChangeCallback(object :  ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("slidepage", "postion $position,  totalpage : ${slideradapter.itemCount}")
                val values = position % 5
                 mBinding.selectedbanner = values


            }
        })*/
        mBinding.headerSlider.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {
                currentItem = position
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun run() {
                if (currentItem < mBinding.headerSlider.childCount+2){
                    mBinding.headerSlider.setCurrentItem(currentItem++ , true)
                }else{
                    currentItem = 0
                    mBinding.headerSlider.setCurrentItem(0 , false)
                }
                handler.postDelayed(this, 5000)
            }
        }, 5000)




        sliderlist1.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                mBinding.phtographerdownlitem.visibility = View.GONE
                mBinding.homeEditorChoiceSubtitle.visibility  = View.GONE
                mBinding.homeEditorChoiceRv.visibility  = View.GONE

            }else{
                mBinding.homeEditorChoiceSubtitle.visibility = View.VISIBLE
                mBinding.phtographerdownlitem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext, this)
                mBinding.homeEditorChoiceRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })


        dronelist.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                mBinding.homeEditorDroneContainer.visibility = View.GONE
                mBinding.dronedownitem.visibility = View.GONE
            }else{
                mBinding.homeDroneSubtitle.visibility = View.VISIBLE

                mBinding.homeEditorDroneContainer.visibility = View.VISIBLE
                mBinding.dronedownitem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext, this)
                mBinding.homeDroneRv.adapter = adapter
                //     mBinding.homeSearchFreelancerRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })

        Retoucherlist.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                mBinding.homeEditorRetoucherContainer.visibility = View.GONE
                mBinding.retoucherItem.visibility = View.GONE
            }else{
                mBinding.homeRetoucherSubtitle.visibility = View.VISIBLE

                mBinding.homeEditorRetoucherContainer.visibility = View.VISIBLE
                mBinding.retoucherItem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext, this)
                mBinding.homeRetoucherRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
        Editorlist.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                mBinding.homeEditorEditorContainer.visibility = View.GONE
                mBinding.EditorItem.visibility = View.GONE
            }else{
                mBinding.homeEditorSubtitle.visibility = View.VISIBLE

                mBinding.homeEditorEditorContainer.visibility = View.VISIBLE
                mBinding.EditorItem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext, this)
                mBinding.homeEditorRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })

        Shootinglist.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                mBinding.homeEditorShootingStudioContainer.visibility = View.GONE
                mBinding.shootingitem.visibility = View.GONE
            }else{
                mBinding.homeShootingSubtitle.visibility = View.VISIBLE

                mBinding.homeEditorShootingStudioContainer.visibility = View.VISIBLE
                mBinding.shootingitem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext, this)
                mBinding.homeShootingRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })

        Rentalhouses.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                mBinding.homeEditorRentalContainer.visibility = View.GONE
                mBinding.rentalItem.visibility = View.GONE
            }else{
                mBinding.homeRentalSubtitle.visibility = View.VISIBLE

                mBinding.homeEditorRentalContainer.visibility = View.VISIBLE
                mBinding.rentalItem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext, this)
                mBinding.homeRentalRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })

        Cinematographylist.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                mBinding.homeEditorChoiceCineContainer.visibility = View.GONE
                mBinding.cinematographerbottomitem.visibility = View.GONE
            }
            else{
                mBinding.homeCinematographerSubtitle.visibility = View.VISIBLE

                mBinding.homeEditorChoiceCineContainer.visibility = View.VISIBLE
                mBinding.cinematographerbottomitem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext,this)
                mBinding.homeCinematographerRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })



    }


    fun searchfreelancerrv(){
        sf_list.clear()
        sf_list.add(Search_freelancer_item_list(
            "1",
            "Photographer",
            R.drawable.fs_camera.toString(),
            "fs",
            false
        ))
        sf_list.add(Search_freelancer_item_list(
            "4",
            "Cinematographer",
            R.drawable.fs_cinematography.toString(),
            "fs",
            false
        ))
        sf_list.add(Search_freelancer_item_list(
            "2",
            "Drone",
            R.drawable.fs_drone.toString(),
            "fs",
            false
        ))
        sf_list.add(Search_freelancer_item_list(
            "3",
            "Retoucher",
            R.drawable.fs_retoucher.toString() ,
            "fs",
            false
        ))

        sf_list.add(Search_freelancer_item_list(
            "5",
            "Editor",
            R.drawable.fs_editor.toString(),
            "fs",
            false
        ))
        sf_list.add(Search_freelancer_item_list(
            "8",
            "Intern",
            R.drawable.fs_intern.toString(),
            "fs",
            false
        ))
        sf_list.add(Search_freelancer_item_list(
            "7",
            "Rental Houses",
            R.drawable.fs_rental.toString(),
            "fs",
            false
        ))
        sf_list.add(Search_freelancer_item_list(
            "6",
            "Shooting Studio/Location",
            R.drawable.fs_shooting.toString(),
            "fs",
        false))



        val adapter = SearchFreelancersAdapter(sf_list, JobApp.instance.applicationContext, this)
        mBinding.homeSearchFreelancerRv.adapter = adapter
        mBinding.homeSearchFreelancerRv.setHasFixedSize(true)
        adapter.notifyDataSetChanged()
        //getEditorChoicePhotographer()

    }



    fun getEditorChoicePhotographer(){



      sliderlist1.observe(viewLifecycleOwner, Observer {
         if(it.isNullOrEmpty()){
             mBinding.phtographerdownlitem.visibility = View.GONE
             mBinding.homeEditorChoiceSubtitle.visibility  = View.GONE
             mBinding.homeEditorChoiceRv.visibility  = View.GONE

         }else{
             mBinding.homeEditorChoiceSubtitle.visibility = View.VISIBLE
             mBinding.phtographerdownlitem.visibility = View.VISIBLE
             val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                 JobApp.instance.applicationContext, this)
             mBinding.homeEditorChoiceRv.adapter = adapter
             adapter.notifyDataSetChanged()
         }
      })



    }
    fun getEditorChoiceDrone(){



        dronelist.observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()){
                     mBinding.homeEditorDroneContainer.visibility = View.GONE
                    mBinding.dronedownitem.visibility = View.GONE
                }else{
                    mBinding.homeDroneSubtitle.visibility = View.VISIBLE

                    mBinding.homeEditorDroneContainer.visibility = View.VISIBLE
                    mBinding.dronedownitem.visibility = View.VISIBLE
                    val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                        JobApp.instance.applicationContext, this)
                    mBinding.homeDroneRv.adapter = adapter
               //     mBinding.homeSearchFreelancerRv.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
        })

        Retoucherlist.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                mBinding.homeEditorRetoucherContainer.visibility = View.GONE
                mBinding.retoucherItem.visibility = View.GONE
            }else{
                mBinding.homeRetoucherSubtitle.visibility = View.VISIBLE

                mBinding.homeEditorRetoucherContainer.visibility = View.VISIBLE
                mBinding.retoucherItem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext, this)
                mBinding.homeRetoucherRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
        Editorlist.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                mBinding.homeEditorEditorContainer.visibility = View.GONE
                mBinding.EditorItem.visibility = View.GONE
            }else{
                mBinding.homeEditorSubtitle.visibility = View.VISIBLE

                mBinding.homeEditorEditorContainer.visibility = View.VISIBLE
                mBinding.EditorItem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext, this)
                mBinding.homeEditorRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })

        Shootinglist.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                mBinding.homeEditorShootingStudioContainer.visibility = View.GONE
                mBinding.shootingitem.visibility = View.GONE
            }else{
                mBinding.homeShootingSubtitle.visibility = View.VISIBLE

                mBinding.homeEditorShootingStudioContainer.visibility = View.VISIBLE
                mBinding.shootingitem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext, this)
                mBinding.homeShootingRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })

        Rentalhouses.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                mBinding.homeEditorRentalContainer.visibility = View.GONE
                mBinding.rentalItem.visibility = View.GONE
            }else{
                mBinding.homeRentalSubtitle.visibility = View.VISIBLE

                mBinding.homeEditorRentalContainer.visibility = View.VISIBLE
                mBinding.rentalItem.visibility = View.VISIBLE
                val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                    JobApp.instance.applicationContext, this)
                mBinding.homeRentalRv.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })



    }


    fun getEditorchoiceCinematographer(){
       Cinematographylist.observe(viewLifecycleOwner, Observer {
              if (it.isNullOrEmpty()){
                  mBinding.homeEditorChoiceCineContainer.visibility = View.GONE
                  mBinding.cinematographerbottomitem.visibility = View.GONE
              }
           else{
                  mBinding.homeCinematographerSubtitle.visibility = View.VISIBLE

                  mBinding.homeEditorChoiceCineContainer.visibility = View.VISIBLE
                  mBinding.cinematographerbottomitem.visibility = View.VISIBLE
                  val adapter = SearchFreelancersAdapter(it as ArrayList<Search_freelancer_item_list>,
                      JobApp.instance.applicationContext,this)
                  mBinding.homeCinematographerRv.adapter = adapter
                  adapter.notifyDataSetChanged()
           }
       })



    }



    fun  getEditorChoice(){

        lifecycleScope.launch {
            val result = FreeLancerRepository.getEditorChoice()
            when(result){
                is ResultWrapper.Success ->{
                   val list = mutableListOf<Search_freelancer_item_list>()
                    list.addAll(result.response.Photographer!!.map { toMYlist(it) })
                   sliderlist1.value = list
                    val list2 = mutableListOf<Search_freelancer_item_list>()
                    list2.addAll(result.response.Cinematographer!!.map { toMYlist(it) })
                    Cinematographylist.value = list2
                    //getEditorchoiceCinematographer()
                    val list3 = mutableListOf<Search_freelancer_item_list>()
                    list3.addAll(result.response.Drone!!.map { toMYlist(it) })
                    dronelist.value = list3
                    val list4 = mutableListOf<Search_freelancer_item_list>()
                    list4.addAll(result.response.Rental_Houses!!.map { toMYlist(it) })
                    Rentalhouses.value = list4

                    val list5 = mutableListOf<Search_freelancer_item_list>()
                    list5.addAll(result.response.Shooting_Studio!!.map { toMYlist(it) })
                    Shootinglist.value = list5

                    val list6 = mutableListOf<Search_freelancer_item_list>()
                    list6.addAll(result.response.Retoucher!!.map { toMYlist(it) })
                    Retoucherlist.value = list6

                    val list7 = mutableListOf<Search_freelancer_item_list>()
                    list7.addAll(result.response.Editor!!.map { toMYlist(it) })
                    Editorlist.value = list7


                   // getEditorChoiceDrone()



                }
                is ResultWrapper.Failure ->{
                    Log.d("editchoice", result.errorMessage.toString())
                }
            }

        }
    }


    override fun onPause() {
        super.onPause()
      //  mBinding.cvHeaderSlider.postDelayed(sliderRunnable, 5000)
    }

    override fun onResume() {
        super.onResume()
            // mBinding.cvHeaderSlider.postDelayed(sliderRunnable, 5000)
    }

    fun toMYlist(editorChoiceModel: EditorChoiceModel) = Search_freelancer_item_list(
         editorChoiceModel.user_id,
        "${editorChoiceModel.first_name}",
        editorChoiceModel.profile_pic,
        "",
        editorChoiceModel.verified
    )

    override fun navigateToCategoryToFU(
        itemview: View,
        searchFreelancerItemList: Search_freelancer_item_list
    ) {

        val bundle = Bundle()
        bundle.putString("category", searchFreelancerItemList.name)
        mFUViewModel.mcategory.value = searchFreelancerItemList.name
        findNavController().navigate(R.id.freelancerUsers , bundle)
    }

    override fun navigatetoFreelencerProfile(searchFreelancerItemList: Search_freelancer_item_list) {
        val bundle = Bundle()
        bundle.putString("profileid", searchFreelancerItemList.id)
          mFUViewModel.filterapply.value = false

        findNavController().navigate(R.id.FUProfile, bundle)
    }


    private fun getBanners(){
        lifecycleScope.launch {
            val result = FreeLancerRepository.getBanner()
            when(result){
                is ResultWrapper.Success ->{
                     val list = mutableListOf<SliderItem>()
                    list.addAll(result.response.map { it })
                    sliderlsit.value = list
                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }

    override fun bannerclick(sliderItem: SliderItem) {
        if (!sliderItem.Intenturl.isNullOrEmpty()){
         try {
             val openURL = Intent(Intent.ACTION_VIEW)
             openURL.data = Uri.parse(sliderItem.Intenturl)
             startActivity(openURL)
         }  catch (e : Exception){
             Toast.makeText(context, "Valid url not found", Toast.LENGTH_SHORT).show()
             Log.d("Banner Exception", e.message.toString())
         }
        }
    }
}