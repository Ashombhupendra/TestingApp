package com.dbvertex.job.peresentation.jobboard.findjob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemFjRecentjobBinding
import com.dbvertex.job.network.response.jobboard.JobsDTO

class AllJobAdapter(context: Context, val list : ArrayList<JobsDTO>, val onJobClick: onJobClick) :
       RecyclerView.Adapter<AllJobAdapter.myViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val v = ItemFjRecentjobBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return myViewHolder(v)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
       holder.bind(list[position], onJobClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class myViewHolder(val mBinding : ItemFjRecentjobBinding): RecyclerView.ViewHolder(mBinding.root) {
        fun bind(jobsDTO: JobsDTO, onJobClick: onJobClick) {
            mBinding.itemFjTitle.text = "${jobsDTO.title}".capitalize()
            mBinding.itemFjDescription.text = "${jobsDTO.description}".capitalize()
            mBinding.itemFjCreatedDate.text = "${jobsDTO.created}"
            mBinding.itemFjTime.text = "${jobsDTO.jobtype}"

            if (jobsDTO.urgent.equals("0")){
                mBinding.itemFjUrjent.visibility =View.INVISIBLE
            }else{
                mBinding.itemFjUrjent.visibility =View.VISIBLE
            }
            mBinding.itemfjcontainer.setOnClickListener {
                onJobClick.onNavigateJobDetail(jobsDTO)
            }


        }

    }
}

interface onJobClick{
  fun  onNavigateJobDetail(jobsdto : JobsDTO)
}