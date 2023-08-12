package com.dbvertex.job.peresentation.navigate_to_page.discuss


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemDiscusQuestionBinding
import com.dbvertex.job.network.response.discuss.AllDiscussionDTO
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.bumptech.glide.Glide


class AllDiscussionAdapter(context: Context, val list : List<AllDiscussionDTO>, val onDiscussClick: onDiscussClick):
     RecyclerView.Adapter<AllDiscussionAdapter.viewholder>(){

    private val context: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val v = ItemDiscusQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return viewholder(v)

    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(list[position], onDiscussClick)



    }

    override fun getItemCount(): Int {
        return list.size
    }


    class viewholder(private val mBinding : ItemDiscusQuestionBinding): RecyclerView.ViewHolder(mBinding.root) {
        fun bind(allDiscussionDTO: AllDiscussionDTO, onDiscussClick: onDiscussClick) {


            mBinding.itemDiscusQuestionName.text = "${allDiscussionDTO.sender_name}"
            mBinding.itemDiscusQuestionDate.text = "${allDiscussionDTO.created}"
            mBinding.itemDiscusQuestionCategory.text = "${allDiscussionDTO.sender_category}"
            mBinding.itemDiscusAnswerCount.text = "${allDiscussionDTO.total_answers} Answers"
            mBinding.itemDiscusQuestion.text = "${allDiscussionDTO.question}"
            mBinding.itemDiscusAnswer.text = "${allDiscussionDTO.description}"

            if (allDiscussionDTO.verified) mBinding.verifiedProfile.visibility = View.VISIBLE
            else mBinding.verifiedProfile.visibility = View.GONE

            if (allDiscussionDTO.last_answered.isNullOrEmpty()){
                mBinding.itemDiscusAnswerLastseen.visibility= View.GONE
            }
            val userid = SharedPrefrenceHelper.user.userid
            if (userid.equals(allDiscussionDTO.sender_id)) {
                mBinding.itemDiscusAnswerFollowUnfollow.visibility =View.GONE

            }else{
                mBinding.itemDiscusAnswerFollowUnfollow.visibility =View.VISIBLE

            }
            mBinding.itemDiscusAnswerLastseen.text = "Last Answered ${allDiscussionDTO.last_answered}"
            if (allDiscussionDTO.favourite){
                mBinding.itemDiscusAnswerFavIv.setImageResource(R.drawable.ic_baseline_favorite_24)
                mBinding.itemDiscusAnswerFavTv.setText(R.string.unfollow)

            }else{
                mBinding.itemDiscusAnswerFavTv.setText(R.string.follow)
                mBinding.itemDiscusAnswerFavIv.setImageResource(R.drawable.ic_baseline_favorite_border_24)

            }

            mBinding.itemDiscusAnswerFollowUnfollow.setOnClickListener {
                if (allDiscussionDTO.favourite){
                    allDiscussionDTO.favourite = false
                    mBinding.itemDiscusAnswerFavTv.setText(R.string.follow)
                    mBinding.itemDiscusAnswerFavIv.setImageResource(R.drawable.ic_baseline_favorite_border_24)

                }else{
                    allDiscussionDTO.favourite = true
                    mBinding.itemDiscusAnswerFavTv.setText(R.string.unfollow)
                    mBinding.itemDiscusAnswerFavIv.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
                onDiscussClick.onDiscusslike(allDiscussionDTO)
            }
            mBinding.itemQuestionContainer.setOnClickListener {
                onDiscussClick.onDiscussclick(allDiscussionDTO)
            }

            Glide.with(JobApp.instance.applicationContext)
                .load(allDiscussionDTO.profile_pic)
                .placeholder(R.color.blue_gray)
                .into(mBinding.itemDiscusQuestionPro)


        }

    }
}

interface onDiscussClick{
    fun onDiscussclick(allDiscussionDTO: AllDiscussionDTO)

    fun onDiscusslike(allDiscussionDTO: AllDiscussionDTO)


}