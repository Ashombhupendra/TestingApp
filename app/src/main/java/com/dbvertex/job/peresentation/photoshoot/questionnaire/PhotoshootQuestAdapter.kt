package com.dbvertex.job.peresentation.photoshoot.questionnaire

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemPhotoshootQuestionnaireBinding
import com.dbvertex.job.network.response.photoshoot.Questionnaire.QuestionnaireDTO

class PhotoshootQuestAdapter(val list: List<QuestionnaireDTO>, val onQuestionClick: onQuestionClick):
RecyclerView.Adapter<PhotoshootQuestAdapter.questionViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): questionViewHolder {
        val binding = ItemPhotoshootQuestionnaireBinding.inflate(LayoutInflater.from(parent.context), parent, false)
          return questionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: questionViewHolder, position: Int) {
        holder.bind(list[position], onQuestionClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class questionViewHolder(val mBinding : ItemPhotoshootQuestionnaireBinding): RecyclerView.ViewHolder(mBinding.root) {
        fun bind(dto: QuestionnaireDTO, onQuestionClick: onQuestionClick) {
            val positionin = absoluteAdapterPosition+1
             mBinding.itemQuestionnaireText.text = "$positionin. ${dto.question}"
             mBinding.itemQuestionnaireText.setOnClickListener {
                 onQuestionClick.OnQuestionClick(dto)
             }

            mBinding.icQuestionMenu.setOnClickListener {
                onQuestionClick.onDeleteClick(dto)
            }
        }

    }
}
interface onQuestionClick{
      fun OnQuestionClick(questionnaireDTO: QuestionnaireDTO)
      fun  onDeleteClick(questionnaireDTO: QuestionnaireDTO)
}