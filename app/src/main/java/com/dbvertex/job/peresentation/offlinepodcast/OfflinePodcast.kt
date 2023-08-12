package com.dbvertex.job.peresentation.offlinepodcast

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.database.PodcastTableModel
import com.dbvertex.job.databinding.FragmentOfflinePodcastBinding
import com.dbvertex.job.network.repository.PodcastRepository
import com.dbvertex.job.network.response.PodcastDTO
import com.dbvertex.job.peresentation.navigate_to_page.blog.podcast.PodcastAdapter
import com.dbvertex.job.peresentation.navigate_to_page.blog.podcast.RVPODCAST


class OfflinePodcast : Fragment(), RVPODCAST {

        private lateinit var mBinding : FragmentOfflinePodcastBinding
        private var mMediaPlayer: MediaPlayer? = null
        private lateinit var mPodcastAdapter : PodcastAdapter
        private val mHandler = Handler(Looper.getMainLooper())
        val   sslist = MutableLiveData<List<PodcastDTO>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentOfflinePodcastBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@OfflinePodcast

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       mBinding.backOfflinePodcast.setOnClickListener {
           findNavController().navigateUp()
       }


        mPodcastAdapter = PodcastAdapter(rvpodcast = this).apply {
            registerAdapterDataObserver(object  : RecyclerView.AdapterDataObserver(){
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    //     val count = mPodcastAdapter.itemCount

                }
            })
        }

        mBinding.offlinePodcastRv.adapter = mPodcastAdapter
        mBinding.offlinePodcastRv.hasFixedSize()
        val list = PodcastRepository.getSavepodcast()
         val listsss = mutableListOf<PodcastDTO>()
        listsss.addAll(list.map { toPodcastdto(it) })
        sslist.value = listsss

        Log.d("savepodcasat", list.toString())
        sslist.observe(viewLifecycleOwner){
            Log.d("savepodcasat", it.toString())
               if (it.isNullOrEmpty()){
                   mBinding.noDownloadPodcast.visibility = View.VISIBLE
               }
            else{
                   mBinding.noDownloadPodcast.visibility = View.GONE

               }
            mPodcastAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(it))
            mPodcastAdapter.refresh()
        }

        //initializing the media player
        mMediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
        initViewHolderSeekBar()


    }


    private fun toPodcastdto(podcastTableModel: PodcastTableModel) = PodcastDTO(
        podcastTableModel.id,
        podcastTableModel.title ,
        podcastTableModel.description ,
        podcastTableModel.created,"/storage/emulated/0/Download/${podcastTableModel.audio}" ,
        podcastTableModel.duration,
        podcastTableModel.favourite
    )

    override fun onDestroyView() {
        super.onDestroyView()
        mPodcastAdapter.selfLifecycleDestroyed()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    private fun initViewHolderSeekBar() {
        requireActivity().runOnUiThread(object : Runnable {
            override fun run() {
                val isPlaying = mMediaPlayer?.isPlaying ?: false
                if (isPlaying) {
                    val progress = mMediaPlayer?.currentPosition
                    if (progress != null)  {
                        Log.d("seekbar3", progress.toString())
                        mPodcastAdapter.audioholder?.setProgress(progress)
                    }
                }
                mHandler.postDelayed(this, 100)
            }
        })
    }

    override fun onPlay(position: Int, playing: Boolean, progress: Int) {
        if (playing){
            val podItem = mPodcastAdapter.getPodcast(position)
            mMediaPlayer?.apply {
                reset()

                setDataSource(podItem?.audio)
                prepareAsync()
                mPodcastAdapter.audioholder?.isLoading?.value = true
                setOnCompletionListener {
                    mPodcastAdapter.audioholder?.resetUI()
                }
                setOnPreparedListener {
                    start()
                    seekTo(progress)
                    Log.d("seekbar", "$progress")
                    mPodcastAdapter.audioholder?.apply {
                        setMaxDuration(podItem!!.duration)
                        //Log.d("seelbarmax", "")
                        isLoading.value = false
                    }
                }
                setOnBufferingUpdateListener { _, percent ->
                    mPodcastAdapter.audioholder?.isLoading?.value =
                        percent < 100
                }
            }

        }else{
            mMediaPlayer?.reset()
        }
    }

    override fun stop() {
        mMediaPlayer?.reset()
    }

    override fun seek(progress: Int) {
        mMediaPlayer?.seekTo(progress)

    }
    override fun setpodcastfav(podcastDTO: PodcastDTO) {
       //fsdjfnkdjfn
    }

    override fun setDownloadpodcast(podcastDTO: PodcastDTO) {
        //dfjknksufn
    }


    override fun onPause()  {
        super.onPause()
        mBinding.offlinePodcastRv.isSaveEnabled
    }

    override fun onResume() {
        super.onResume()
        mPodcastAdapter.refresh()

    }

}