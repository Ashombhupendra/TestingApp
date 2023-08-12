package com.dbvertex.job.peresentation.photoshoot.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentEditPhotoShootQuestionnaireBinding
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch


class EditPhotoShootQuestionnaire : Fragment() {

     private lateinit var mBinding : FragmentEditPhotoShootQuestionnaireBinding
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()
     private var question_id : String = ""
    private var question_type : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentEditPhotoShootQuestionnaireBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@EditPhotoShootQuestionnaire
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(view).load(R.raw.progress_gif)
            .into(mBinding.imagesLoading)
        mBinding.addPosesBack.setOnClickListener {
            findNavController().navigateUp()
        }
        val bundle  = arguments
        if (bundle != null){
            val question = bundle.getString("question")
            question_id = bundle.getString("question_id").toString()
            question_type = bundle.getString("question_type").toString()
             if (question.isNullOrEmpty()){
                       mBinding.photoshootShareQuestion.visibility = View.GONE
                       mBinding.photoshootCreateQuestion.visibility  = View.VISIBLE
                       mBinding.photoshootQuestionnaireTitle.setText("Create Question")
                     mBinding.photoshootQuestionnaireSave.visibility = View.INVISIBLE
             }else{
                 mBinding.photoshootQuestionnaireEt.setText("$question")
                 mBinding.photoshootShareQuestion.visibility = View.VISIBLE
                 mBinding.photoshootQuestionnaireSave.visibility = View.VISIBLE
                 mBinding.photoshootCreateQuestion.visibility  = View.GONE
             }


        }
        mBinding.photoshootShareQuestion.setOnClickListener {

            shareQuestion(mBinding.photoshootQuestionnaireEt.text.toString())
        }
        mBinding.photoshootQuestionnaireSave.setOnClickListener {
            mBinding.imagesLoading.visibility = View.VISIBLE
            saveQuestion(mBinding.photoshootQuestionnaireEt.text.toString())
        }
        mBinding.photoshootCreateQuestion.setOnClickListener {
            mBinding.imagesLoading.visibility = View.VISIBLE
            createQuestion(mBinding.photoshootQuestionnaireEt.text.toString())
        }
    }

    private fun shareQuestion(text : String){
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setType("text/plain")
            .setText("$text")
            .intent
        if (shareIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(shareIntent)
        }
    }
    private fun saveQuestion(text : String){
     lifecycleScope.launch {
         val result = PhotoShootRepository.editPhotoShootQuestionnaire(mPhotoshootViewModel.photoshoot_id.value.toString(),
         mPhotoshootViewModel.session.value.toString(), question_id, text, question_type)
         when(result){
             is ResultWrapper.Success ->{
                  mBinding.imagesLoading.visibility = View.GONE
                 findNavController().navigateUp()
                 Toast.makeText(JobApp.instance.applicationContext, "Questionnaire Saved", Toast.LENGTH_SHORT).show()
             }
             is ResultWrapper.Failure ->{
                 mBinding.imagesLoading.visibility = View.GONE
                 Toast.makeText(JobApp.instance.applicationContext, "${result.errorMessage}", Toast.LENGTH_SHORT).show()

             }
         }
     }
    }

    private fun createQuestion(text: String){
            lifecycleScope.launch {
                val result = PhotoShootRepository.AddPhotoShootQuestionnaire(mPhotoshootViewModel.photoshoot_id.value.toString(),
                    mPhotoshootViewModel.session.value.toString(), text)
                when(result){
                    is ResultWrapper.Success ->{
                        mBinding.imagesLoading.visibility = View.GONE
                        Toast.makeText(JobApp.instance.applicationContext, "Questionnaire Added", Toast.LENGTH_SHORT).show()
                    }
                    is ResultWrapper.Failure ->{
                        mBinding.imagesLoading.visibility = View.GONE
                        Toast.makeText(JobApp.instance.applicationContext, "${result.errorMessage}", Toast.LENGTH_SHORT).show()

                    }
                }
            }
    }

}