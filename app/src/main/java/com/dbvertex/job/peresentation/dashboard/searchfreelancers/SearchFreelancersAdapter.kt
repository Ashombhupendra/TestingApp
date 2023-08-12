package com.dbvertex.job.peresentation.dashboard.searchfreelancers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.bumptech.glide.Glide

class SearchFreelancersAdapter(val list: ArrayList<Search_freelancer_item_list>,  context : Context, val navigatetosearch: navigatetosearch ? = null) : RecyclerView.Adapter<SearchFreelancersAdapter.myViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewholder {
         val v = LayoutInflater.from(parent.context).inflate(R.layout.item_search_freelancer_home, parent, false)
        return myViewholder(v)
    }

    override fun onBindViewHolder(holder: myViewholder, position: Int) {
        holder.bind(list[position], navigatetosearch)
        Log.d("Recyclers ", list[position].toString())
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class myViewholder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        fun bind(
            searchFreelancerItemList: Search_freelancer_item_list,
            navigatetosearch: navigatetosearch?
        ) {

            val image = itemView.findViewById<ImageView>(R.id.search_freelancer_img)
            val pro_verified = itemView.findViewById<ImageView>(R.id.verified_profile)
            val text = itemView.findViewById<TextView>(R.id.search_freelancer_name)

            if (searchFreelancerItemList.category.equals("fs")){

                image.setOnClickListener {

                    navigatetosearch!!.navigateToCategoryToFU(itemView, searchFreelancerItemList)
                }
                pro_verified.visibility =View.GONE
                    val rs = searchFreelancerItemList.image_url?.toInt()
                if (rs != null) {
                    image.setImageResource(rs)
                }
            }else {
                 if (searchFreelancerItemList.verified)   pro_verified.visibility =View.VISIBLE
                else   pro_verified.visibility =View.GONE
                image.setOnClickListener {
                    navigatetosearch!!.navigatetoFreelencerProfile(searchFreelancerItemList)
                }
                Glide.with(itemView.context).load(searchFreelancerItemList.image_url).into(image)
            }
            val output: String = searchFreelancerItemList.name.substring(0, 1).toUpperCase() + searchFreelancerItemList.name.substring(1)

            if (searchFreelancerItemList.name.equals("Photographer")) text.text = "${searchFreelancerItemList.name}\n"
           else if (searchFreelancerItemList.name.equals("Cinematographer"))  text.text ="Cinematographer"
            else text.text = "${output}"

        }


    }

}

interface navigatetosearch{
     fun navigateToCategoryToFU(itemview: View, searchFreelancerItemList: Search_freelancer_item_list)

     fun navigatetoFreelencerProfile(searchFreelancerItemList: Search_freelancer_item_list)
}

