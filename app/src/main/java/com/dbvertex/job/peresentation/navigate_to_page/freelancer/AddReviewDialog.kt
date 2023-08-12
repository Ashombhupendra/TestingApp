package com.dbvertex.job.peresentation.navigate_to_page.freelancer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.dbvertex.job.databinding.FragmentAddReviewDialogBinding
import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.network.utils.temp_showToast
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kotlinx.coroutines.launch


class AddReviewDialog : DialogFragment() {

    private lateinit var mBinding: FragmentAddReviewDialogBinding
    val creativity_rating = MutableLiveData<Float>(1f)
    val punctuality_Rating = MutableLiveData<Float>(1f)
    val communication_rating = MutableLiveData<Float>(1f)
    val presentation_rating = MutableLiveData<Float>(1f)
    val workethic_Rating = MutableLiveData<Float>(1f)
    val review = MutableLiveData<String>()
    var ratee_id: String = ""


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddReviewDialogBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@AddReviewDialog
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindProgressButton(mBinding.submitRating)


        val bundle = arguments
        if (bundle != null) {
            ratee_id = bundle.getString("ratee_id").toString()
        }


        mBinding.reviewCreativityRatingBar.setOnRatingBarChangeListener { bar, fl, b ->

            if (fl > 1) creativity_rating.value = fl
        }
        mBinding.reviewPunctualityRatingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->

            if (fl > 1) punctuality_Rating.value = fl

        }
        mBinding.reviewCommunicationRatingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->

            if (fl > 1) communication_rating.value = fl
        }
        mBinding.reviewPresentationRatingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->

            if (fl > 1) presentation_rating.value = fl
        }
        mBinding.reviewWorkethicRatingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->

            if (fl > 1) workethic_Rating.value = fl
        }

        mBinding.submitRating.setOnClickListener {


            val et_review = mBinding.reviewEt.text
            if (et_review.isNullOrEmpty()) {
                mBinding.reviewEt.setError("Enter some feedback")
            } else {
                hideKeyboard(requireContext(), it)
                mBinding.submitRating.apply {
                    showProgress()
                    isClickable = false
                }
                submit_rating(et_review.toString(), ratee_id)
            }
        }
    }

    fun submit_rating(et_review: String, ratee_id: String) {
        lifecycleScope.launch {
            val result = FreelancerProfileRepository.setRating(
                ratee_id,
                creativity_rating.value!!.toFloat(),
                punctuality_Rating.value!!.toFloat(),
                communication_rating.value!!.toFloat(),
                presentation_rating = presentation_rating.value!!.toFloat(),
                workethic_Rating.value!!.toFloat(),
                et_review
            )
            when (result) {
                is ResultWrapper.Success -> {
                    mBinding.submitRating.apply {
                        hideProgress("Add Review")
                        isClickable = true
                    }
                    dismiss()
                }
                is ResultWrapper.Failure -> {
                    mBinding.submitRating.apply {
                        hideProgress("Add Review")
                        isClickable = true
                    }
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }
}