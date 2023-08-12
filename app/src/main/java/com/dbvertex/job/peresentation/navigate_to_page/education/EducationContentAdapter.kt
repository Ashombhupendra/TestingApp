package com.dbvertex.job.peresentation.navigate_to_page.education

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.network.response.EducationSeriesContent
import com.dbvertex.job.network.response.Serieslist


class EducationContentAdapter(
    context: Context, val lisst : ArrayList<EducationSeriesContent>, val onContentClick: onContentClick
): RecyclerView.Adapter<EducationContentAdapter.myViewHolder>() {

    private val context: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_education_series_main, parent, false)

        return myViewHolder(v)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bind(lisst[position], onContentClick)
    }

    override fun getItemCount(): Int {
        return lisst.size
    }


    class myViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        fun bind(seriesContent: EducationSeriesContent, onContentClick: onContentClick) {
                        val title = itemView.findViewById<TextView>(R.id.item_education_series_main_title)

            title.text = "${seriesContent.series_name}"
            val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_education_series_sercondary)

            val adapter = EducationContentSecondaryAdapter(context = itemView.context, seriesContent.data.map { it } as ArrayList<Serieslist>, onContentClick)
            recyclerView.adapter = adapter


        }

    }
}