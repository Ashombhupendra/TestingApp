package com.dbvertex.job.peresentation.jobboard.managejob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemManageJobAppliedBinding
import com.dbvertex.job.network.response.jobboard.jobsapplieduserlist
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.bumptech.glide.Glide


private var lastPosition = -1

class ManageJobAdapter(context: Context, val list : ArrayList<jobsapplieduserlist>, val onManageJobCLick: onManageJobCLick) :
   RecyclerView.Adapter<ManageJobAdapter.myViewHolder>(){

    private val context: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val v = ItemManageJobAppliedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return myViewHolder(v)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

          holder.bind(list[position], onManageJobCLick)
        setAnimation(holder.itemView, position);
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
       /* if (position > lastPosition) {
            val anim = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 1000
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }*/
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 800
        viewToAnimate.startAnimation(anim)
    }
    override fun getItemCount(): Int {
        return list.size
    }





    class myViewHolder(val mBinding : ItemManageJobAppliedBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(applieduser: jobsapplieduserlist, onManageJobCLick: onManageJobCLick) {
            mBinding.itemManageAppliedTitle.text = "${applieduser.title}".capitalize()
            mBinding.itemManageAppliedDes.text = "${applieduser.description}".capitalize()
            mBinding.itemManageAppliedLocation.text = "${applieduser.apilocation}".capitalize()
            mBinding.itemManageAppliedTime.text = "${applieduser.created}"
            mBinding.itemManageAppliedUsername.text = "${applieduser.firstname} ${applieduser.last_name}".capitalize()
            mBinding.itemManageAppliedIncome.text = "${applieduser.budget}"

            Glide.with(JobApp.instance.applicationContext)
                .load(applieduser.profile_pic)
                .placeholder(R.color.blue_gray)
                .into(mBinding.itemAppliedProfile)

            mBinding.itemManageContainer.setOnClickListener {
                onManageJobCLick.onManageJobCLick(applieduser)

            }

            val userid = SharedPrefrenceHelper.user.userid
            val otheruserid = applieduser.sender_id
            if (!userid.equals(otheruserid)){
                mBinding.itemManageAppliedMenu.visibility = View.GONE
            }
            mBinding.itemManageAppliedMenu.setOnClickListener {
                onManageJobCLick.onMenuItemClick(applieduser, it)
            }

        }


    }
}

interface onManageJobCLick{
  fun  onMenuItemClick(applieduser: jobsapplieduserlist, view: View)

  fun onManageJobCLick(applieduser: jobsapplieduserlist)
}