package com.dbvertex.job.peresentation.navigate_to_page.education

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentEducationDetailBinding
import com.dbvertex.job.network.repository.EducationRepository
import com.dbvertex.job.network.response.SingleSerieslist
import com.dbvertex.job.network.utils.ResultWrapper
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch


class EducationDetail :  Fragment(), onSingleContentClick {
    var video_id = "vG2PNdI8axo"
       private lateinit var mBinding : FragmentEducationDetailBinding
    private val mEducationViewModel by activityViewModels<EducationViewModel>()
    private lateinit var youTubePlayerView :YouTubePlayerView
    private lateinit var webView  : WebView
    val api_key =  "AIzaSyAuJlJoxTMjjeSLvb3BYmty0eEkp6oHfCU"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEducationDetailBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@EducationDetail

            viewmodel = mEducationViewModel

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        youTubePlayerView = view.findViewById<YouTubePlayerView>(R.id.youtube_videoPlayer)
        webView = view.findViewById(R.id.webView_youtube_video)

        mBinding.educationDetailBack.setOnClickListener {
            findNavController().navigateUp()
        }

        webView.webViewClient = WebViewClient()
     //   lifecycle.addObserver(mBinding.youtubePlayerView)
        Glide.with(view).load(R.raw.progress_gif)
            .into(mBinding.categoryLoading)
        val bundle = arguments
        if (bundle != null){
            val seriesid = bundle.getString("seriesid")

            val contentid = bundle.getString("contentid")

            mEducationViewModel.getSingleContent(contentid.toString())
            mEducationViewModel.getSeries(seriesid.toString())

        }

        mEducationViewModel.serieslist.observe(viewLifecycleOwner, Observer {

            val adapter = EducationDetailAdapter(
                JobApp.instance.applicationContext,
                it as ArrayList<SingleSerieslist>,
                this
            )
            mBinding.rvEducationSeriesDetail.adapter = adapter


        })




        mEducationViewModel.isVideo.observe(viewLifecycleOwner, Observer {
            if (it) {
                val url = "http://thephototribe.in/welcome/youtube/${mEducationViewModel.mvedioID.value}"
                webView.loadUrl("http://thephototribe.in/welcome/youtube/${mEducationViewModel.mvedioID.value}")
                // this will enable the javascript settings
                 webView.settings.javaScriptEnabled = true

                // if you want to enable zoom feature
                webView.settings.setSupportZoom(true)
                mEducationViewModel.isVideo.value = false
        //         Toast.makeText(requireContext(), "this", Toast.LENGTH_SHORT).show()

            }
        })




    }



    override fun onnavContentDetail(itemview: View, serieslist: SingleSerieslist) {
        mBinding.categoryLoading.visibility =View.VISIBLE
        mEducationViewModel.getSingleContent(serieslist.content_id)
        mEducationViewModel.getSeries(serieslist.series_id)
    }

    override fun onContentLiked(itemview: View, serieslist: SingleSerieslist) {
        lifecycleScope.launch {
            val result = EducationRepository.SetfavUnFav(serieslist.content_id)
            when(result){
                is ResultWrapper.Success -> {

                }
                is ResultWrapper.Failure -> {

                }
            }
        }
    }

    private  fun  playvideo(){

        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.getPlayerUiController()
        youTubePlayerView.enableBackgroundPlayback(false)
        youTubePlayerView.addYouTubePlayerListener(object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {}

            override fun onCurrentSecond(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                second: Float
            ) {
            }

            override fun onError(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                error: PlayerConstants.PlayerError
            ) {
                Log.d("error", error.toString())
            }

            override fun onPlaybackQualityChange(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {
                Log.d("errorquality", playbackQuality.toString())
            }

            override fun onPlaybackRateChange(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                playbackRate: PlayerConstants.PlaybackRate
            ) {
                Log.d("errorquality1", playbackRate.toString())
            }

            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                if (!mEducationViewModel.mvedioID.value.isNullOrEmpty()){
                    youTubePlayer.loadVideo(mEducationViewModel.mvedioID.value.toString(),0f);
                }

            }

            override fun onStateChange(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                Log.d("errorquality1", state.toString())
            }

            override fun onVideoDuration(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                duration: Float
            ) {
                Log.d("errorquality12", duration.toString())
            }

            override fun onVideoId(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                videoId: String
            ) {
                Log.d("errorquality13", videoId.toString())
            }

            override fun onVideoLoadedFraction(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                loadedFraction: Float
            ) {
                Log.d("errorquality14", loadedFraction.toString())
            }


        })

    }
    inner class WebViewClient : android.webkit.WebViewClient() {

        // Load the URL
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        // ProgressBar will disappear once page is loaded
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mBinding.categoryLoading.visibility =View.GONE
        }
    }

}