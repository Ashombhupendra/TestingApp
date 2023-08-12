package com.dbvertex.job.peresentation.navigate_to_page.blog.podcast

import com.dbvertex.job.network.response.PodcastDTO

interface RVPODCAST {

    fun onPlay(position: Int, playing: Boolean, progress: Int)
    fun stop()
    fun seek(progress: Int)
    fun setpodcastfav(podcastDTO: PodcastDTO)
    fun setDownloadpodcast(podcastDTO: PodcastDTO)

}