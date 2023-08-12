package com.dbvertex.job.peresentation.navigate_to_page.freelancer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.network.response.FreelencerUserlistDTO
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.squareup.picasso.Picasso

class FreelencerUserlistAdapte(context: Context , val lisst : ArrayList<FreelencerUserlistDTO>,val  monUserCLick: onUserCLick) :
        RecyclerView.Adapter<FreelencerUserlistAdapte.FUViewmodel>(){
    private val context: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FUViewmodel {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_freelancer_userlist, parent, false)

        return FUViewmodel(v)
    }

    override fun onBindViewHolder(holder: FUViewmodel, position: Int) {
        val userid  = SharedPrefrenceHelper.user.userid.toString()
        Log.d("freelanceruser111", lisst.toString())
        holder.bind(lisst[position],monUserCLick)

    }

    override fun getItemCount(): Int {
        return lisst.size
    }

    class FUViewmodel(itemView : View): RecyclerView.ViewHolder(itemView) {
        fun bind(freelencerUserlistDTO: FreelencerUserlistDTO, monUserCLick: onUserCLick) {

            val title = itemView.findViewById<TextView>(R.id.item_freelancer_userlist_username)
            val category = itemView.findViewById<TextView>(R.id.item_freelancer_userlist_category)
            val total_rating = itemView.findViewById<TextView>(R.id.item_freelancer_total_rating)
            val image = itemView.findViewById<ImageView>(R.id.item_freelancer_userlist_img)
            val verified_profile = itemView.findViewById<ImageView>(R.id.verified_profile)
            val container = itemView.findViewById<ConstraintLayout>(R.id.FUContainer)
            val ratercontainer = itemView.findViewById<AppCompatRatingBar>(R.id.item_freelancer_rating)

            if (freelencerUserlistDTO.verified) verified_profile.visibility = View.VISIBLE
            else  verified_profile.visibility = View.GONE
            container.setOnClickListener {
                monUserCLick.navigateToProfile(itemView, freelencerUserlistDTO)
            }

            if (freelencerUserlistDTO.profile_img.isNullOrEmpty()){

            }else{
                Picasso.get().load(freelencerUserlistDTO.profile_img).into(image)
            }

            total_rating.text = "(${freelencerUserlistDTO.ratings_count})"

            ratercontainer.apply {
                rating = freelencerUserlistDTO.ratings.toFloat()
            }
            val output: String = freelencerUserlistDTO.first_name.trim().substring(0, 1).toUpperCase() + freelencerUserlistDTO.first_name.substring(1)

            title.text = "${output}"
            category.text = "${freelencerUserlistDTO.user_freelancer_category}"



        }

    }
}


interface onUserCLick{
     fun navigateToProfile(itemView: View, freelencerUserlistDTO: FreelencerUserlistDTO)
}