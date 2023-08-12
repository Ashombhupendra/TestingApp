package com.dbvertex.job.peresentation.photoshoot.questionnaire

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPhotoshootQuestionnaireBinding
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.response.photoshoot.Questionnaire.QuestionnaireDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch


class PhotoshootQuestionnaire : Fragment(), onQuestionClick {
        private lateinit var mBinding  : FragmentPhotoshootQuestionnaireBinding
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()
      private lateinit var adapter: PhotoshootQuestAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPhotoshootQuestionnaireBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@PhotoshootQuestionnaire
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(view).load(R.raw.progress_gif)
            .into(mBinding.imagesLoading)
        mPhotoshootViewModel.photoshoot_id.observe(viewLifecycleOwner){
            mPhotoshootViewModel.getPhotoshootQuestionnaire()
        }

        mPhotoshootViewModel.photoshootQuestinnaire.observe(viewLifecycleOwner){
             adapter = PhotoshootQuestAdapter(it, this)
            mBinding.photoshootQuestionnaire.adapter =adapter
            adapter.notifyDataSetChanged()
        }
        mBinding.photoshootAddNewQuestionnaire.setOnClickListener {
            if (mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
                Toast.makeText(JobApp.instance.applicationContext, "Firstly Save Photoshoot", Toast.LENGTH_SHORT).show()
            }else{
                val bundle = Bundle()
                bundle.putString("question", "")
                bundle.putString("question_id", "")
                bundle.putString("question_type", "")
                findNavController().navigate(R.id.editPhotoShootQuestionnaire, bundle)
            }
        }
    }

    override fun OnQuestionClick(questionnaireDTO: QuestionnaireDTO) {
         if (!mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
             val bundle = Bundle()
             bundle.putString("question", questionnaireDTO.question)
             bundle.putString("question_id", questionnaireDTO.id)
             bundle.putString("question_type", questionnaireDTO.type)

             findNavController().navigate(R.id.editPhotoShootQuestionnaire, bundle)
         }else{
             showSnackBar("Firstly create Photoshoot")
         }

    }

    override fun onDeleteClick(questionnaireDTO: QuestionnaireDTO) {

        if (!mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
            val builder = AlertDialog.Builder(requireActivity(),R.style.MyDatePickerStyle)
            builder.setTitle("Delete ")
            builder.setMessage("Are you sure you want to delete questionnaire.")
            builder.setPositiveButton("YES"){dialog, which ->
                mBinding.imagesLoading.visibility = View.VISIBLE
                lifecycleScope.launch {
                    val result = PhotoShootRepository.DeletePhotoShootQuestionnaire(mPhotoshootViewModel.photoshoot_id.value.toString(),
                        questionnaireDTO.id, questionnaireDTO.type)
                    when(result){
                        is ResultWrapper.Success ->{
                            mBinding.imagesLoading.visibility = View.GONE

                            temp_showToast("Deleted")
                            mPhotoshootViewModel.getPhotoshootQuestionnaire()
                        }
                        is ResultWrapper.Failure ->{
                            mBinding.imagesLoading.visibility = View.GONE

                            temp_showToast("${result.errorMessage}")
                        }
                    }
                }
            }
            builder.setNegativeButton("No"){dialog, which ->
                dialog.cancel()
            }
            // Set other dialog properties
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }else{
            showSnackBar("Firstly create Photoshoot")
        }
    }


    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        mPhotoshootViewModel.photoshoot_id.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty() && !mPhotoshootViewModel.session.value.isNullOrEmpty()){
              mPhotoshootViewModel.getPhotoshootQuestionnaire()
            }
        }
    }

}