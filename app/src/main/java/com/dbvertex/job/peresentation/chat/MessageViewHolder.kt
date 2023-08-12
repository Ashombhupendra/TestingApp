package com.dbvertex.job.peresentation.chat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemChatMessageTextReceiverBinding
import com.dbvertex.job.databinding.ItemChatMessageTextSendBinding
import com.dbvertex.job.network.response.chat.ChatList
import com.dbvertex.job.network.utils.getDurationString
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

sealed class MessageViewHolder(val view : View, private val rvHost: RVHost):
    RecyclerView.ViewHolder(view)
    {
       abstract fun bind(item : ChatList)
    }

class MyMessageView(private  val binding : ItemChatMessageTextSendBinding , private val rvHost: RVHost):
        MessageViewHolder(binding.root, rvHost),
        LifecycleOwner{
                private val lifeCycleRegistry = LifecycleRegistry(this)
                var playing = MutableLiveData(false)
                var vedioplaying = MutableLiveData(false)

                private var wasLifeCyclePaused = false
                private var duration = 100
                private var progressPercent: Int = 0
                private var item: ChatList? = null
                val isLoading = MutableLiveData(false)

    init {
        lifeCycleRegistry.currentState = Lifecycle.State.INITIALIZED
        binding.viewholder = this
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

    constructor(parent: ViewGroup, RVHost: RVHost) : this(
        ItemChatMessageTextSendBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        RVHost
    )


    override fun bind(item: ChatList) {
        resetUI()
        this.item = item
        binding.chatresponse = item

        if (item.read_status.equals("0")){
            binding.messageStatus.setImageResource(R.drawable.delivered_message)
        }else{
            binding.messageStatus.setImageResource(R.drawable.seen_message)
        }




        if (item.content_type.equals("image")){
                binding.audioLl.visibility = View.GONE
                binding.itemChatMessage.visibility = View.GONE
                binding.itemChatMessageIv.visibility = View.VISIBLE
                binding.pdfContainer.visibility = View.GONE
            binding.videoView.visibility = View.GONE
            binding.playPauseVideo.visibility = View.GONE


            Picasso.get().load(item.content).into(binding.itemChatMessageIv)

            }else if (item.content_type.equals("application")){
                binding.audioLl.visibility = View.GONE
                binding.itemChatMessage.visibility = View.GONE
                binding.itemChatMessageIv.visibility = View.GONE
                binding.pdfContainer.visibility = View.VISIBLE
            binding.playPauseVideo.visibility = View.GONE

            binding.videoView.visibility = View.GONE


            binding.pdfContainer.setOnClickListener {
                     rvHost.onPdfClick(item)

                 }

            }else if (item.content_type.equals("audio")){
                binding.audioLl.visibility = View.VISIBLE
                binding.itemChatMessage.visibility = View.GONE
                binding.itemChatMessageIv.visibility = View.GONE
                binding.pdfContainer.visibility = View.GONE
                binding.videoView.visibility = View.GONE
            binding.playPauseVideo.visibility = View.GONE

                //to do something
                binding.chatSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            if (playing.value == true) {
                                rvHost.seek(progress * 100)
                            } else {
                                this@MyMessageView.progressPercent = progress * 100
                            }
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                })


            }else if (item.content_type.equals("video")){
            binding.audioLl.visibility = View.GONE
            binding.itemChatMessage.visibility = View.GONE
            binding.itemChatMessageIv.visibility = View.GONE
            binding.videoView.visibility = View.VISIBLE
            binding.pdfContainer.visibility = View.GONE
            binding.playPauseVideo.visibility = View.VISIBLE
            binding.videoView.apply {

                setVideoPath(item.content)


            }
            binding.playPauseVideo.setOnClickListener {
                if (binding.videoView.isPlaying){
                    binding.videoView.pause()
                    vedioplaying.value = false
                }else{
                    binding.videoView.start()
                    vedioplaying.value = true

                }

            }

        }else{
                binding.audioLl.visibility = View.GONE
                binding.itemChatMessage.visibility = View.VISIBLE
                binding.itemChatMessageIv.visibility = View.GONE
            binding.videoView.visibility = View.GONE
            binding.playPauseVideo.visibility = View.GONE

            binding.pdfContainer.visibility = View.GONE

            }



            }
    fun playPause() {
        if (vedioplaying.value!!.equals(true)){
            binding.videoView.stopPlayback()
            vedioplaying.value = false
        }
        val isPlayingNow = !(playing.value ?: false)
        resetLastPlayingViewHolder(isPlayingNow)
        playing.value = isPlayingNow
        rvHost.onPlay(absoluteAdapterPosition, isPlayingNow, progressPercent)
    }

    fun resetUI() {
        //resetting values
        playing.value = false
        this.progressPercent = 0
        binding.chatSeekbar.progress = 0
      //  val totalDuration = getDurationString(item!!.duration / 1000)

      //  val durationText = binding.root.resources.getString(R.string.duration, totalDuration ?: "0:00")
      //  binding.durationTv.text = durationText
    }

    private fun resetLastPlayingViewHolder(isPlaying: Boolean) {
        if (!isPlaying) return
        val cribAdapter = bindingAdapter as ChatMessageAdapter
       val lastViewHolder = cribAdapter.myMessageplayviewholder
        lastViewHolder?.run {
            val isLastViewHolderPlaying = lastViewHolder.playing.value ?: false
            if (isLastViewHolderPlaying) {
                lastViewHolder.resetUI()
            }
        }
        cribAdapter.myMessageplayviewholder = this
    }

    @SuppressLint("SetTextI18n")
    fun setProgress(progress: Int) {
        this.progressPercent = progress
        binding.chatSeekbar.progress = progress / 100
        val currentProgress = getDurationString(progress / 1000)
        val totalDuration = getDurationString(duration / 1000)
        binding.durationTv.text = "$currentProgress/$totalDuration"
    }

    fun setMaxDuration(maxDuration: Int) {
        duration = maxDuration
        Log.d("seekbarmax1","$duration,  hello")

        binding.chatSeekbar.max = maxDuration / 100
    }

}


class OtherPersonMessageView(private  val binding : ItemChatMessageTextReceiverBinding , private val rvHost: RVHost):
    MessageViewHolder(binding.root, rvHost),
    LifecycleOwner{
    private val lifeCycleRegistry = LifecycleRegistry(this)
    var playing = MutableLiveData(false)
    var vedioplaying = MutableLiveData(false)

    private var wasLifeCyclePaused = false
    private var duration = 100
    private var progressPercent: Int = 0
    private var item: ChatList? = null
    val isLoading = MutableLiveData(false)

    init {
        lifeCycleRegistry.currentState = Lifecycle.State.INITIALIZED
        binding.viewholder = this
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

    constructor(parent: ViewGroup, RVHost: RVHost) : this(
        ItemChatMessageTextReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        RVHost
    )


    override fun bind(item: ChatList) {
        resetUI()
        this.item = item
        binding.chatresponse = item
                if (item.content_type.equals("image")){
            binding.audioLl.visibility = View.GONE
            binding.itemChatMessage.visibility = View.GONE
            binding.itemChatMessageIv.visibility = View.VISIBLE
            binding.videoView.visibility = View.GONE
            binding.pdfContainer.visibility = View.GONE
                    binding.playPauseVideo.visibility = View.GONE

                    Picasso.get().load(item.content).into(binding.itemChatMessageIv)

        }else if (item.content_type.equals("application")){
            binding.audioLl.visibility = View.GONE
            binding.itemChatMessage.visibility = View.GONE
            binding.itemChatMessageIv.visibility = View.GONE
            binding.videoView.visibility = View.GONE
                    binding.playPauseVideo.visibility = View.GONE

                    binding.pdfContainer.visibility = View.VISIBLE

                    binding.pdfContainer.setOnClickListener {
                        rvHost.onPdfClick(item)
                    }

        }else if (item.content_type.equals("audio")){
            binding.audioLl.visibility = View.VISIBLE
            binding.itemChatMessage.visibility = View.GONE
            binding.itemChatMessageIv.visibility = View.GONE
            binding.videoView.visibility = View.GONE
                    binding.playPauseVideo.visibility = View.GONE

                    binding.pdfContainer.visibility = View.GONE


            //to do something
            binding.chatSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        if (playing.value == true) {
                            rvHost.seek(progress * 100)
                        } else {
                            this@OtherPersonMessageView.progressPercent = progress * 100
                        }
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })


        }else if (item.content_type.equals("video")){
                    binding.audioLl.visibility = View.GONE
                    binding.itemChatMessage.visibility = View.GONE
                    binding.itemChatMessageIv.visibility = View.GONE
                    binding.videoView.visibility = View.VISIBLE
                    binding.pdfContainer.visibility = View.GONE
                    binding.playPauseVideo.visibility = View.VISIBLE

                    binding.videoView.apply {

                        setVideoPath(item.content)
                        seekTo(100)

                    }
                    binding.playPauseVideo.setOnClickListener {
                        if (binding.videoView.isPlaying){
                            vedioplaying.value = false
                            binding.videoView.pause()
                        }else{
                            vedioplaying.value = true
                            binding.videoView.start()
                        }
                    }
        }
                else{
            binding.audioLl.visibility = View.GONE
            binding.itemChatMessage.visibility = View.VISIBLE
            binding.itemChatMessageIv.visibility = View.GONE
            binding.videoView.visibility = View.GONE
                    binding.playPauseVideo.visibility = View.GONE

                    binding.pdfContainer.visibility = View.GONE

        }



    }
    fun playPause() {
        if (vedioplaying.value!!.equals(true)){
            binding.videoView.stopPlayback()
            vedioplaying.value = false
        }

        val isPlayingNow = !(playing.value ?: false)
        resetLastPlayingViewHolder(isPlayingNow)
        playing.value = isPlayingNow
        rvHost.onPlay(absoluteAdapterPosition, isPlayingNow, progressPercent)
    }

    fun resetUI() {
        //resetting values
        playing.value = false
        this.progressPercent = 0
        binding.chatSeekbar.progress = 0
        //  val durationText = binding.root.resources.getString(R.string.duration, item?.audioDuration ?: "0:00")
        //  binding.durationTv.text = durationText
    }

    private fun resetLastPlayingViewHolder(isPlaying: Boolean) {
        if (!isPlaying) return
           val cribAdapter = bindingAdapter as ChatMessageAdapter
          val lastViewHolder = cribAdapter.otherMessageplayviewholder
           lastViewHolder?.run {
               val isLastViewHolderPlaying = lastViewHolder.playing.value ?: false
               if (isLastViewHolderPlaying) {
                   lastViewHolder.resetUI()
               }
           }
           cribAdapter.otherMessageplayviewholder = this
    }

    @SuppressLint("SetTextI18n")
    fun setProgress(progress: Int) {
        this.progressPercent = progress
        binding.chatSeekbar.progress = progress / 100
        val currentProgress = getDurationString(progress / 1000)
        val totalDuration = getDurationString(duration / 1000)
        binding.durationTv.text = "$currentProgress/$totalDuration"
    }

    fun setMaxDuration(maxDuration: Int) {
        duration = maxDuration
        Log.d("seekbarmax1","$duration,  hello")

        binding.chatSeekbar.max = maxDuration / 100
    }

}