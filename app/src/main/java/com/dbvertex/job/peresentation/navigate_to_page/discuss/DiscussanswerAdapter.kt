package com.dbvertex.job.peresentation.navigate_to_page.discuss


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemDdCommentBinding
import com.dbvertex.job.network.response.discuss.DiscusAnswerDTO
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class DiscussanswerAdapter(val list : ArrayList<DiscusAnswerDTO>) : RecyclerView.Adapter<DiscussanswerAdapter.AnswerviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerviewHolder {
        val v = ItemDdCommentBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return AnswerviewHolder(v)
    }

    override fun onBindViewHolder(holder: AnswerviewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    class AnswerviewHolder( val mBinding : ItemDdCommentBinding): RecyclerView.ViewHolder(mBinding.root) {
        fun bind(answerDTO: DiscusAnswerDTO) {

            mBinding.itemDiscusQuestionName.text = "${answerDTO.sender_name}"
            mBinding.itemDiscusQuestionCategory.text = "${answerDTO.sender_category}"
            mBinding.itemDiscusQuestionDate.text = "${answerDTO.created}"
            mBinding.itemDiscusAnswer.text = "${answerDTO.answer}"

            Glide.with(JobApp.instance.applicationContext)
                .load(answerDTO.profile_pic)
                .placeholder(R.color.blue_gray)
                .into(mBinding.itemDiscusQuestionPro)

            if (answerDTO.verified) mBinding.verifiedProfile.visibility = View.VISIBLE
            else mBinding.verifiedProfile.visibility = View.GONE
             val imagelist = listOf<ImageView>(
                 mBinding.discussAnswerIv1,
                 mBinding.discussAnswerIv2,
                 mBinding.discussAnswerIv3,
                 mBinding.discussAnswerIv4,
                 mBinding.discussAnswerIv5,
             )
            if (!answerDTO.imags.isNullOrEmpty()){

                mBinding.imageContainer.visibility = View.VISIBLE
                answerDTO.imags.forEachIndexed { index, ddImage ->
                    Picasso.get().load(ddImage.image).into(imagelist[index])
                }
            }else{
                mBinding.imageContainer.visibility = View.GONE
            }
        }
    }
}