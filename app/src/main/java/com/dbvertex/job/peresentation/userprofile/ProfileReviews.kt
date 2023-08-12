package com.dbvertex.job.peresentation.userprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.MainActivity
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentProfileReviewsBinding
import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.response.ratingres.RecentReviewDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.peresentation.navigate_to_page.freelancer.RecentReviewAdapter
import com.dbvertex.job.utils.SharedPrefrenceHelper
import kotlinx.coroutines.launch


class ProfileReviews : Fragment() {

         private lateinit var mBinding : FragmentProfileReviewsBinding
         val reviewlist = MutableLiveData<List<RecentReviewDTO>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileReviewsBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@ProfileReviews

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!MainActivity.profileid.value.toString().isNullOrEmpty()){
            getRating(MainActivity.profileid.value.toString())

        }
        val userid = SharedPrefrenceHelper.user.userid
        if (userid.toString().equals(MainActivity.profileid.value.toString())){
            mBinding.addReview.visibility = View.GONE
        }else{
            mBinding.addReview.visibility = View.VISIBLE
        }

        reviewlist.observe(viewLifecycleOwner, Observer {
            val adapter = RecentReviewAdapter(JobApp.instance.applicationContext, it as ArrayList<RecentReviewDTO>)
            mBinding.recentReviewsRv.adapter = adapter
            adapter.notifyDataSetChanged()

        })


        mBinding.addReview.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("ratee_id", MainActivity.profileid.value.toString())
            findNavController().navigate(R.id.addReviewDialog, bundle)
        }
    }



    fun getRating(user_id : String){
        lifecycleScope.launch {
            val result = FreelancerProfileRepository.getRating(user_id)
            when(result){
                is ResultWrapper.Success ->{
                    mBinding.workReviewSection.visibility = View.VISIBLE
                    mBinding.reviewDetailSection.visibility =View.VISIBLE
                    mBinding.noReviewFound.visibility = View.GONE
                    mBinding.reviewCreativityRatingBar.apply {
                        rating = result.response.userdetail.creativity.toFloat()
                    }
                    mBinding.reviewPunctualityRatingBar.apply {
                        rating = result.response.userdetail.punctuality.toFloat()
                    }
                    mBinding.reviewCommunicationRatingBar.apply {
                        rating = result.response.userdetail.communication.toFloat()
                    }
                    mBinding.reviewPresentationRatingBar.apply {
                        rating = result.response.userdetail.presentation.toFloat()
                    }
                    mBinding.reviewWorkethicRatingBar.apply {
                        rating = result.response.userdetail.workEthics.toFloat()
                    }
                    mBinding.totalReviewsCount.text = "${result.response.userdetail.num_ratings} ratings"
                    mBinding.ratingBar.apply {
                        rating = result.response.userdetail.WorkReviews.toFloat()
                    }
                    mBinding.outofRating.text = "${result.response.userdetail.WorkReviews} out of 5"


                    val list = mutableListOf<RecentReviewDTO>()
                    list.addAll(result.response.reviews.map { it })
                    reviewlist.value = list


                }
                is ResultWrapper.Failure ->{
                  //  temp_showToast("${result.errorMessage}")

                    mBinding.noReviewFound.visibility = View.VISIBLE
                    mBinding.workReviewSection.visibility = View.GONE
                    mBinding.reviewDetailSection.visibility =View.GONE
                    mBinding.recentReviewLabel.visibility = View.GONE
                }
            }
        }

    }
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (!MainActivity.profileid.value.toString().isNullOrEmpty()){
            getRating(MainActivity.profileid.value.toString())

        }
    }


}