package com.dbvertex.job.peresentation.navigate_to_page.blog.podcast


import android.view.ViewGroup

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil


import com.dbvertex.job.network.response.PodcastDTO



class PodcastAdapter( val rvpodcast: RVPODCAST):
    PagingDataAdapter<PodcastDTO, PodcastViewHolder>(diffCallback) {
    private val allAudioViewHolders: MutableList<PodcastHolder> = mutableListOf()

    var audioholder: PodcastHolder? = null


    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PodcastDTO>() {
            override fun areItemsTheSame(oldItem: PodcastDTO, newItem: PodcastDTO): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PodcastDTO, newItem: PodcastDTO): Boolean {
                return oldItem.id == newItem.id

            }
        }
    }
    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        val item = getItem(position)
        item ?: return
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
       return PodcastHolder(parent, rvpodcast).apply {
            markCreated()
            allAudioViewHolders.add(this)
       }
    }


    override fun onViewAttachedToWindow(holder: PodcastViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is PodcastHolder) {
            holder.markAttach()
        }
    }

    override fun onViewDetachedFromWindow(holder: PodcastViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is PodcastHolder) {
            val isPlaying = holder.playing.value ?: false
            if (isPlaying) {
                //for playing lockscreen
//                holder.resetUI()
//                audioholder = null
//                rvpodcast.stop()
            }
          //  holder.markDetach()
        }
    }

    fun selfLifecycleDestroyed() {
       allAudioViewHolders.forEach {
             it.markCreated()
       }
    }

   fun getPodcast(position: Int) : PodcastDTO?{
         return getItem(position)
   }


}