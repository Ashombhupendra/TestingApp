package com.dbvertex.job.peresentation.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.data.UsersList


class UserAdapter(val context: Context, val list: List<UsersList>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_message_user, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])

        
    }



    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(usersList: UsersList) {
            val username = itemView.findViewById<TextView>(R.id.item_msg_name)
            val usermssg = itemView.findViewById<TextView>(R.id.item_msg_message)
             Log.d("Userlist", usersList.toString())
            username.text = usersList.username
            usermssg.text = "Hello this is the previous message"
        }

    }
}