package com.dbvertex.job.peresentation.company_signup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.network.response.placeList

class AutoCompleteAdapter(val list : ArrayList<placeList>,  context: Context,private val rvHost: RVHost) : RecyclerView.Adapter<AutoCompleteAdapter.Myviewholder>() {
    private val context: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myviewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_autocomplete, parent, false)
        return Myviewholder(v)
    }

    override fun onBindViewHolder(holder: Myviewholder, position: Int) {
               holder.bindItems(list[position])


        holder.itemView.findViewById<TextView>(R.id.item_prediction).setOnClickListener {
                    rvHost.setText(holder.itemView, list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class Myviewholder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        fun bindItems(mplaceList: placeList) {
                val text = itemView.findViewById<TextView>(R.id.item_prediction)

            text.text = "${mplaceList.description}"

        }

    }
}