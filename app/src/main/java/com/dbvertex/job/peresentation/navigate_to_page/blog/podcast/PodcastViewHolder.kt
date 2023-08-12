package com.dbvertex.job.peresentation.navigate_to_page.blog.podcast

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.SeekBar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemPodcastListBinding
import com.dbvertex.job.network.response.PodcastDTO
import com.dbvertex.job.network.utils.getDurationString
import com.bumptech.glide.Glide


sealed class PodcastViewHolder(val view : View, private val rvpodcast: RVPODCAST):
    RecyclerView.ViewHolder(view) {

        abstract fun bind(podcastDTO: PodcastDTO)




}

class PodcastHolder(
    private  val binding : ItemPodcastListBinding,
    private val rvpodcast: RVPODCAST
    ): PodcastViewHolder(binding.root, rvpodcast),

        LifecycleOwner{
    private val lifeCycleRegistry = LifecycleRegistry(this)
    var playing = MutableLiveData(false)
    private var wasLifeCyclePaused = false
    private var duration = 100
    private var progressPercent: Int = 0
    private var item  : PodcastDTO? = null
    val isLoading = MutableLiveData(false)

    init {
        lifeCycleRegistry.currentState = Lifecycle.State.INITIALIZED
        binding.viewHolder = this
        binding.lifecycleOwner = this


        Glide.with(view).load(R.raw.progress_gif)
            .into(binding.progressImage)
    }
    //lifecycle methods start
    fun markCreated() {
        lifeCycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun markAttach() {
        if (wasLifeCyclePaused) {
            lifeCycleRegistry.currentState = Lifecycle.State.RESUMED
            wasLifeCyclePaused = false
        } else {
            lifeCycleRegistry.currentState = Lifecycle.State.STARTED
        }
    }

    fun markDetach() {
        wasLifeCyclePaused = true
        lifeCycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun markDestroyed() {
        lifeCycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle {
        return lifeCycleRegistry
    }

    //lifecycle methods end

    constructor(parent: ViewGroup, rvpodcast: RVPODCAST) : this(
        ItemPodcastListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        rvpodcast
    )

    override fun bind( podcastDTO: PodcastDTO) {
        resetUI()
        this.item = item
        binding.cribResponse = podcastDTO


        if (JobApp.instance.isConnectedToInternet()){
            binding.itemPodcastFav.visibility = View.VISIBLE
            binding.podcastDownloadCv.visibility= View.VISIBLE
        }else{
            binding.itemPodcastFav.visibility = View.INVISIBLE
            binding.podcastDownloadCv.visibility= View.INVISIBLE
        }
        Log.d("podcastitem", item.toString())
        binding.podcastDownloadCv.setOnClickListener {
            rvpodcast.setDownloadpodcast(podcastDTO)
        }
        binding.itemPodcastFav.setOnClickListener {
              if (podcastDTO.favourite){
                  podcastDTO.favourite = false
              }else{
                  podcastDTO.favourite = true
              }
            val scaleXAnimator = ObjectAnimator.ofFloat(binding.itemPodcastFav, "scaleX", 1f, 1.5f, 1f)
            val scaleYAnimator = ObjectAnimator.ofFloat(binding.itemPodcastFav, "scaleY", 1f, 1.5f, 1f)
            val rotationAnimator = ObjectAnimator.ofFloat(binding.itemPodcastFav, "rotation", 0f, 45f, 0f)
            AnimatorSet().apply {
                duration = 800
                interpolator = OvershootInterpolator()
                playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
                start()
            }
            rvpodcast.setpodcastfav(podcastDTO)
        }

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    if (playing.value == true) {
                        rvpodcast.seek(progress * 100)
                    } else {
                        this@PodcastHolder.progressPercent = progress * 100
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    fun playPause() {
        val isPlayingNow = !(playing.value ?: false)
        resetLastPlayingViewHolder(isPlayingNow)
        playing.value = isPlayingNow
        rvpodcast.onPlay(absoluteAdapterPosition, isPlayingNow, progressPercent)
    }
    fun resetUI() {
        //resetting values
        playing.value = false
        this.progressPercent = 0
        binding.seekbar.progress = 0
        Log.d("seekbarreset", "1")
        if (item?.duration.toString().isNullOrEmpty()){
            val totalDuration = getDurationString(item!!.duration / 1000)

            val durationText =
                binding.root.resources.getString(R.string.duration, totalDuration ?: "0:00")
            binding.durationTv.text = durationText
        }

    }

    private fun resetLastPlayingViewHolder(isPlaying: Boolean) {
        if (!isPlaying) return
        val podcastadapter = bindingAdapter as PodcastAdapter
        val lastViewHolder = podcastadapter.audioholder
        lastViewHolder?.run {
            val isLastViewHolderPlaying = lastViewHolder.playing.value ?: false
            if (isLastViewHolderPlaying) {
                lastViewHolder.resetUI()
            }
        }
        podcastadapter.audioholder = this
    }


    @SuppressLint("SetTextI18n")
    fun setProgress(progress: Int) {
        this.progressPercent = progress
        binding.seekbar.progress = progress /100
        val currentProgress = getDurationString(progress / 1000)
        val totalDuration = getDurationString(duration / 1000)
        Log.d("seekbar1", "$progress,  Seekduration - ${binding.seekbar.progress}")

        binding.durationTv.text = "$currentProgress/$totalDuration"

    }

    fun setMaxDuration(maxDuration: Int) {
        duration = maxDuration
        Log.d("seekbar", maxDuration.toString())
        binding.seekbar.max = maxDuration /100
    }

}