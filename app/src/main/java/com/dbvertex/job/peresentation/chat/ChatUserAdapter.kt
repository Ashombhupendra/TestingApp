package com.dbvertex.job.peresentation.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemChatUserBinding
import com.dbvertex.job.network.response.chat.ChatuserDTO
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class ChatUserAdapter(
    context: Context, val list: ArrayList<ChatuserDTO>, val onChatUserClick: onChatUserClick
) : RecyclerView.Adapter<ChatUserAdapter.myViewholder>(), Filterable {
    var searchlist = ArrayList<ChatuserDTO>()

    init {
        searchlist = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewholder {
        val v = ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return myViewholder(v)
    }

    override fun onBindViewHolder(holder: myViewholder, position: Int) {
        holder.bind(searchlist[position], onChatUserClick)
    }

    override fun getItemCount(): Int {
        return searchlist.size
    }

    class myViewholder(private val mBinding: ItemChatUserBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(chatuserDTO: ChatuserDTO, chatuserDTO1: onChatUserClick) {


            val output: String = chatuserDTO.first_name.trim().substring(0, 1)
                .toUpperCase() + chatuserDTO.first_name.substring(1)

            mBinding.itemChatUserUsername.text = "${output}"
            mBinding.itemChatUserTime.text = "${chatuserDTO.created}"
            mBinding.itemChatUserMessage.text = "${chatuserDTO.message}"

            if (chatuserDTO.verified) mBinding.verifiedProfile.visibility = View.VISIBLE
            else mBinding.verifiedProfile.visibility = View.GONE

            if (chatuserDTO.profile_pic != null) {
                Log.d("imagefound", chatuserDTO.profile_pic)
                Picasso.get().load(chatuserDTO.profile_pic).into(mBinding.itemChatUserIv)
            }

            if (chatuserDTO.unread.equals("0")) {
                mBinding.itemChatUnreadMsgCounter.visibility = View.GONE
            } else {
                mBinding.itemChatUnreadMsgCounter.text = "${chatuserDTO.unread}"
                if (chatuserDTO.profile_pic.isEmpty()) {
                    Picasso.get().load(R.drawable.testlogo).into(mBinding.itemChatUserIv)
                } else {
                    Log.d("imagefound", chatuserDTO.profile_pic)
                    Picasso.get().load(chatuserDTO.profile_pic).into(mBinding.itemChatUserIv)
                }
            }

            mBinding.chatUserContainer.setOnClickListener {
                chatuserDTO1.onChatDetail(chatuserDTO)
            }

        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Log.d("filterdata", charSearch.toString())
                if (charSearch.isEmpty()) {
                    searchlist = list
                } else {
                    val resultList = ArrayList<ChatuserDTO>()
                    for (row in list) {
                        Log.d("filterdata1", charSearch.toString() + "$row")

                        if (row.first_name.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT)) || row.first_name.uppercase(
                                Locale.ROOT
                            ).contains(charSearch.uppercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                            Log.d("filterdata132", charSearch.toString())

                        }
                    }
                    searchlist = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = searchlist
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchlist = results?.values as ArrayList<ChatuserDTO>
                Log.d("filterdata1ds", "$searchlist")
                notifyDataSetChanged()
            }

        }
    }

}

interface onChatUserClick {

    fun onChatDetail(chatuserDTO: ChatuserDTO)
}
