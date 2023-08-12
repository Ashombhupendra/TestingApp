package com.dbvertex.job

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController

import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import android.view.animation.OvershootInterpolator
import androidx.lifecycle.*
import androidx.test.core.app.ApplicationProvider
import com.dbvertex.job.data.UserEntity
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.databinding.ActivityMainBinding
import com.dbvertex.job.network.repository.AboutUsRepository


class MainActivity : AppCompatActivity() {

    private lateinit var mNavController: NavController
    private lateinit var mBinding: ActivityMainBinding
    var onSreen : Boolean = false
    var noitener : Boolean = true
    var updateornot : Boolean = true
    var homebuttonclick : Boolean = false
    companion object{
        var profileid = MutableLiveData<String>()
        val offlinepodcastboolean = MutableLiveData<Boolean>(false)
        var updateornot : Boolean = true
        val onEducationScreenBoolean = MutableLiveData<Boolean>(false)
        val posesaddingprocessenable = MutableLiveData<Boolean>(false)
        var setheadline=MutableLiveData<String>()
        var currentPage=MutableLiveData<Int>()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

          bottomappBarController()
        //***SHA to hash key converter
         //    jjhashFromSHA1("BF:98:C1:3E:68:B8:BE:31:1B:BA:6E:2B:C9:5F:36:DB:32:20:07:9E")
        mNavController = findNavController(R.id.main_nav_host)


        /*  GlobalScope.launch {
              PodcastRepository.deleteSavePodcast()
          }*/

       /* onEducationScreenBoolean.observe(this){
            if (it){
                mBinding.activitymainCont.apply {
                    backgroundTintList = context.getResources().getColorStateList(R.color.dark_gray)
                }
                mBinding.homeScreenTop.apply {
                    backgroundTintList = context.getResources().getColorStateList(R.color.dark_gray_ii)
                }
                mBinding.homeScreenUsername.setTextColor(this.resources?.getColorStateList(R.color.white));
                mBinding.notificationBell.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

            }else{
                mBinding.activitymainCont.apply {
                    backgroundTintList = context.getResources().getColorStateList(R.color.white)
                }
                mBinding.homeScreenTop.apply {
                    backgroundTintList = context.getResources().getColorStateList(R.color.white)
                }
                mBinding.homeScreenUsername.setTextColor(this.resources?.getColorStateList(R.color.blue_dark));
                mBinding.notificationBell.setColorFilter(ContextCompat.getColor(this, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);

            }
        }*/


       /* val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {



                if (onSreen && noitener) {
                    if (offlinepodcastboolean.value == false){
                        NoInternetDialog()
                    }
                }
             if (SharedPrefrenceHelper.isLoggedIn){
                    lifecycleScope.launch {
                    val result = NotificationRepository.getNotificationCounter()
                    when(result){
                        is ResultWrapper.Success ->{
                            if (result.response.equals("0")) mBinding.notificationCounter.visibility = View.GONE
                            else mBinding.notificationCounter.visibility = View.VISIBLE

                            Log.d("notificationaaaa", result.response)
                        }
                        is ResultWrapper.Failure ->{
                            Log.d("notificationaaaa", result.errorMessage)
                        }
                    }
                }
             }

                handler.postDelayed(this, 500)
            }
        }, 500)
*/

        mNavController.addOnDestinationChangedListener{_, destination, _ ->
                    currentPage.value=destination.id

              if (destination.id.equals(R.id.notificationFrag)){
                  mBinding.notificationBell.visibility = View.INVISIBLE
                  mBinding.notificationCounter.visibility = View.INVISIBLE
              }else{
                  mBinding.notificationBell.visibility = View.INVISIBLE
                  mBinding.notificationCounter.visibility = View.INVISIBLE
              }
              if (destination.id.equals(R.id.offlinePodcast)){
                     offlinepodcastboolean.value = true
                 }else{
                     offlinepodcastboolean.value = false
                 }


              if (destination.id.equals(R.id.homeScreen)) {
                   homebuttonclick = false

               }
              else{
                   homebuttonclick = true
                   onEducationScreenBoolean.value = false
               }
              if (destination.id.equals(R.id.inspirationFrag) ||destination.id.equals(R.id.chatHome) ||destination.id.equals(R.id.chatMessage)
                || destination.id.equals(R.id.notificationFrag) ||
                destination.id.equals(R.id.posesHome)|| destination.id.equals(R.id.favoritesHome)  ){
                mBinding.homeScreenUsername.text = destination.label

            }else{
                            updateornot = true
                val handler = Handler()
                handler.postDelayed(object : Runnable {
                    override fun run() {

                        if(SharedPrefrenceHelper.isLoggedIn && updateornot){
                            val username = SharedPrefrenceHelper.user.username
                            mBinding.homeScreenUsername.text = "Welcome $username"
                            val userprofile = SharedPrefrenceHelper.user.userprofile
                            if (userprofile != null){
                                Picasso.get().load(SharedPrefrenceHelper.user.userprofile).into(mBinding.profileImage)
                            }
                            if (SharedPrefrenceHelper.user.userVerfiied == true){
                               mBinding.verifiedProfile.visibility =View.VISIBLE
                            }else{
                                mBinding.verifiedProfile.visibility =View.GONE
                            }

                            lifecycleScope.launch {
                                val result = AboutUsRepository.getHeadlines()
                                when(result){
                                    is ResultWrapper.Success ->{
                                        Log.d("newsapi", result.response)
                                        if (!result.response.isNullOrEmpty()){

                                           // mBinding.homeScreenUsername.text = "${result.response}"
                                            //mBinding.homeScreenUsername.setSelected(true);
                                        }
                                    }
                                    is ResultWrapper.Failure ->{
                                          Log.d("errror", result.errorMessage)
                                    }
                                }
                            }

                            updateornot = false


                        }else{


                         setheadline.observe(this@MainActivity, Observer {
                             if(it!=null){
                                 mBinding.homeScreenUsername.text = it.toString()

                             }else{
                                 mBinding.homeScreenUsername.text = "you are offline!!"

                             }

                         })


                        }

                        handler.postDelayed(this, 500)
                    }
                }, 500)
              }

             mBinding.bottomAppBar.isVisible = destination.id in setOf(

               R.id.homeScreen,
               R.id.jobBoardMain,
               R.id.inspirationFrag,
               R.id.notificationFrag,
               R.id.posesHome,
               R.id.chatHome,
               R.id.favoritesHome,
               R.id.resourcesFrag,
                 R.id.settings


             )

                mBinding.homeScreenTop.isVisible = destination.id in setOf(
                    R.id.homeScreen,
                    R.id.resourcesFrag,
                    R.id.inspirationFrag,
                    R.id.chatHome,
                    R.id.notificationFrag,
                    R.id.posesHome,
                    R.id.favoritesHome,
                    R.id.jobBoardMain,
                )

            if (destination.id.equals(R.id.jobBoardMain)){

                mBinding.homeicon.getLayoutParams().height = 60;
                mBinding.homeicon.requestLayout();mBinding.hometxtid.visibility=View.GONE

                mBinding.chaticon.getLayoutParams().height = 50;
                mBinding.chattxtid.requestLayout();mBinding.hometxtid.visibility=View.VISIBLE

                mBinding.prfileicon.getLayoutParams().height = 50;
                mBinding.profiletxtid.requestLayout();mBinding.hometxtid.visibility=View.VISIBLE
            }else if(destination.id.equals(R.id.chatHome)){

                mBinding.chaticon.getLayoutParams().height = 60;
                mBinding.chattxtid.requestLayout();mBinding.hometxtid.visibility=View.GONE

                mBinding.homeicon.getLayoutParams().height = 50;
                mBinding.homeicon.requestLayout();mBinding.hometxtid.visibility=View.VISIBLE

                mBinding.prfileicon.getLayoutParams().height = 50;
                mBinding.profiletxtid.requestLayout();mBinding.hometxtid.visibility=View.VISIBLE
            }else if (destination.id.equals(R.id.settings)){

                mBinding.prfileicon.getLayoutParams().height = 60;
                mBinding.profiletxtid.requestLayout();mBinding.hometxtid.visibility=View.GONE

                mBinding.homeicon.getLayoutParams().height = 50;
                mBinding.homeicon.requestLayout();mBinding.hometxtid.visibility=View.VISIBLE

                mBinding.chaticon.getLayoutParams().height = 50;
                mBinding.chattxtid.requestLayout();mBinding.hometxtid.visibility=View.VISIBLE



            }

        }



        mBinding.notificationBell.setOnClickListener {
              itemAnimation(mBinding.notificationBell)
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setPopExitAnim(R.anim.slide_out_right)
                .build()

            mNavController.navigate(R.id.notificationFrag, null, navOptions)

        }



       /* mBinding.add.setOnClickListener {
            if (mBinding.bottomBarDown.isVisible) {
                ObjectAnimator.ofFloat(mBinding.ivAdd,View.ROTATION, 45f, 0f).setDuration(350).start()
                ViewAnimationUtils.collapse(mBinding.bottomBarDown)
                mBinding.tvAdd.setText("Add")

            }else{
                ObjectAnimator.ofFloat(mBinding.ivAdd,View.ROTATION, 0f, 45f).setDuration(350).start()
                ViewAnimationUtils.expand(mBinding.bottomBarDown)
                mBinding.tvAdd.setText("Close")

            }

        }*/

    }



    //***SHA to hash key converter
   /* fun jjhashFromSHA1(sha1: String) {
        val arr = sha1.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val byteArr = ByteArray(arr.size)

        for (i in arr.indices) {
            byteArr[i] = Integer.decode("0x" + arr[i]).toByte()
        }

        Log.e("hash : ", Base64.encodeToString(byteArr, Base64.NO_WRAP))
    }*/


    @SuppressLint("SuspiciousIndentation")
    fun bottomappBarController(){

        val fragment = R.id.nav_host_fragment_container



          mBinding.home.setOnClickListener {
                    itemAnimation(it)
              if (homebuttonclick){
                  mNavController.navigate(R.id.chatHome)//home screen
                  setheadline.value="Chat List"

              }else{

              }

//              if (mBinding.bottomBarDown.isVisible) {
//                  mBinding.bottomBarDown.visibility = View.GONE
//                  mBinding.ivAdd.setImageResource(R.drawable.n_add)
//                  mBinding.tvAdd.setText("Add")
//              }

          }
         mBinding.job.setOnClickListener {
             itemAnimation(it)
             if (mBinding.bottomBarDown.isVisible) {
                 mBinding.bottomBarDown.visibility = View.GONE

                 mBinding.ivAdd.rotation = 0f
                 mBinding.tvAdd.setText("Add")
             }
             mNavController.navigate(R.id.jobBoardMain)
         }
        mBinding.post.setOnClickListener {
            itemAnimation(it)
            if (mBinding.bottomBarDown.isVisible) {
                mBinding.bottomBarDown.visibility = View.GONE

                mBinding.ivAdd.rotation = 0f
                mBinding.tvAdd.setText("Add")
            }
            mNavController.navigate(R.id.posesHome)
        }

        //profile//setting
        mBinding.inspiration.setOnClickListener {
            itemAnimation(it)
            if (mBinding.bottomBarDown.isVisible) {
                mBinding.bottomBarDown.visibility = View.GONE

                mBinding.ivAdd.rotation = 0f
                mBinding.tvAdd.setText("Add")
            }
            //mNavController.navigate(R.id.inspirationFrag)
            mNavController.navigate(R.id.settings)
        }
        mBinding.settingMain.setOnClickListener {
            itemAnimation(it)
            if (mBinding.bottomBarDown.isVisible) {
                mBinding.bottomBarDown.visibility = View.GONE

                mBinding.ivAdd.rotation = 0f
                mBinding.tvAdd.setText("Add")
            }

             mNavController.navigate(R.id.photoshootMain)
            ///mtoast("Shortly here is  Photoshoot....")
         }
        mBinding.favourites.setOnClickListener {
            itemAnimation(it)
            if (mBinding.bottomBarDown.isVisible) {
                mBinding.bottomBarDown.visibility = View.GONE

                mBinding.ivAdd.rotation = 0f
                mBinding.tvAdd.setText("Add")
            }
            mNavController.navigate(R.id.favoritesHome)
        }
        mBinding.homeCalendar.setOnClickListener {
            itemAnimation(it)
            if (mBinding.bottomBarDown.isVisible) {
                mBinding.bottomBarDown.visibility = View.GONE

                mBinding.ivAdd.rotation = 0f
                mBinding.tvAdd.setText("Add")
            }
            profileid.value = SharedPrefrenceHelper.user.userid.toString()

            mNavController.navigate(R.id.userSchedule)
        }
        mBinding.resources.setOnClickListener {
            itemAnimation(it)
            if (mBinding.bottomBarDown.isVisible) {
                mBinding.bottomBarDown.visibility = View.GONE

                mBinding.ivAdd.rotation = 0f
                mBinding.tvAdd.setText("Add")
            }
            mNavController.navigate(R.id.resourcesFrag)
        }
        mBinding.homeMessage.setOnClickListener {
            itemAnimation(it)
            if (mBinding.bottomBarDown.isVisible) {
                mBinding.bottomBarDown.visibility = View.GONE

                mBinding.ivAdd.rotation = 0f
                mBinding.tvAdd.setText("Add")
            }
            mNavController.navigate(R.id.chatHome)
        }
    }




    private fun clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                (ACTIVITY_SERVICE as ActivityManager?)!!.clearApplicationUserData() // note: it has a return value!
            } else {
                val packageName: String =
                    ApplicationProvider.getApplicationContext<Context>().getPackageName()
                val runtime = Runtime.getRuntime()
                runtime.exec("pm clear $packageName")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun logouts(){
        SharedPrefrenceHelper.user = UserEntity(
           "",
            "",
           " res.user_category",
            "",
            "",
            false
        )
        Log.d("logoutprocess","called");
        Log.d("logoutprocess",SharedPrefrenceHelper.user.userid.toString());
        SharedPrefrenceHelper.isLoggedIn = false
        SharedPrefrenceHelper.isSignupComleted = false
        mNavController.navigate(R.id.loginFragment)
        clearAppData()

    }



    override fun onPause() {
        super.onPause()
        onSreen = false
    }

    override fun onResume() {
        super.onResume()
        onSreen = true

    }




    private fun itemAnimation(item : View){
        val scaleXAnimator = ObjectAnimator.ofFloat(item, "scaleX", 1f, 1.5f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(item, "scaleY", 1f, 1.5f, 1f)
        val rotationAnimator = ObjectAnimator.ofFloat(item, "rotation", 0f, 0f, 0f)
        AnimatorSet().apply {
            duration = 1000
            interpolator = OvershootInterpolator()
            playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
            start()
        }
    }



}