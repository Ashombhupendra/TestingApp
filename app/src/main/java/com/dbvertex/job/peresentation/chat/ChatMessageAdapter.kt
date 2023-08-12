package com.dbvertex.job.peresentation.chat

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dbvertex.job.network.response.chat.ChatList
import com.dbvertex.job.utils.SharedPrefrenceHelper

const val MY_MESSAGE = 1
const val OTHER_MESSAGE = 0

class ChatMessageAdapter(private val rvHost: RVHost) :
    PagingDataAdapter<ChatList, MessageViewHolder>(diffCallback) {


    private val myMessageviewholder: MutableList<MyMessageView> = mutableListOf()
    var myMessageplayviewholder: MyMessageView? = null
    private val otherMessageviewholder: MutableList<OtherPersonMessageView> = mutableListOf()
    var otherMessageplayviewholder: OtherPersonMessageView? = null

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<ChatList>() {
            override fun areItemsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = getItem(position)
        item ?: return
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == MY_MESSAGE) {
            MyMessageView(parent = parent, RVHost = rvHost).apply {
                markCreated()
                myMessageviewholder.add(this)
            }
        } else {
            OtherPersonMessageView(parent = parent, RVHost = rvHost).apply {
                markCreated()
                otherMessageviewholder.add(this)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        val userid = SharedPrefrenceHelper.user.userid
        val item = getItem(position)
        val send = item!!.sender_id
        return if (send.equals(userid)) MY_MESSAGE else OTHER_MESSAGE
    }

    override fun onViewAttachedToWindow(holder: MessageViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is MyMessageView) {
            holder.markAttach()
        } else if (holder is OtherPersonMessageView) {
            holder.markAttach()
        }
    }

    override fun onViewDetachedFromWindow(holder: MessageViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is MyMessageView) {
            val isPlaying = holder.playing.value ?: false
            if (isPlaying) {
                holder.resetUI()
                otherMessageplayviewholder = null

            }
            holder.markDetach()


        } else if (holder is OtherPersonMessageView) {
            val isPlaying = holder.playing.value ?: false
            if (isPlaying) {
                holder.resetUI()
                otherMessageplayviewholder = null

            }
            holder.markDetach()
        }
    }

    fun selfLifecycleDestroyed() {
        myMessageviewholder.forEach {
            it.markDestroyed()
        }
        otherMessageviewholder.forEach {
            it.markDestroyed()
        }
    }

    fun getChatMessage(position: Int): ChatList? {
        return getItem(position)
    }
}
