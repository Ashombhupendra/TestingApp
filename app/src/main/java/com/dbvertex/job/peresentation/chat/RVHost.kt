package com.dbvertex.job.peresentation.chat

import com.dbvertex.job.network.response.chat.ChatList

interface RVHost {

    fun onPlay(position: Int, playing: Boolean, progress: Int)
    fun stop()
    fun seek(progress: Int)
    fun onPdfClick(chatList: ChatList)

}