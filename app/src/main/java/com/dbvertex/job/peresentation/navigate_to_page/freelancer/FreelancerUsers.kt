package com.dbvertex.job.peresentation.navigate_to_page.freelancer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentFreelancerUsersBinding
import com.dbvertex.job.network.response.FreelencerUserlistDTO
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter


class FreelancerUsers : Fragment(), onUserCLick {
    private lateinit var mBinding : FragmentFreelancerUsersBinding
    private val mFUViewModel by activityViewModels<FreelencerUserViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFreelancerUsersBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@FreelancerUsers

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mBinding.freelencerFilter.setOnClickListener {
            findNavController().navigate(R.id.freelencerFilter)
        }

          val bundle = arguments
        if (bundle != null){
            mBinding.freelencerToolbar.visibility = View.VISIBLE
            val category = bundle.getString("category")
            if (mFUViewModel.filterapply.value == false){
                mFUViewModel.fromcategory.value = category
                mFUViewModel.getUserbyList()
            }

        }else{
            mBinding.freelencerToolbar.visibility = View.GONE
            if (mFUViewModel.filterapply.value == false){
                mFUViewModel.getFavFreelancerUser()
            }
        }
          mBinding.freelancerCateBack.setOnClickListener {
              findNavController().navigateUp()
              mFUViewModel.filterapply.value = false
          }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
                mFUViewModel.filterapply.value = false
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this.viewLifecycleOwner, onBackPressedCallback)

        mBinding.freelencerUserSortBy.setOnClickListener {
            if (mBinding.sortByOption.isVisible){
                mBinding.sortByOption.visibility = View.GONE
            }else{
                mBinding.sortByOption.visibility = View.VISIBLE
            }
        }
        //on  sortby ooptioin click

        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)

            Log.d("radiogroup", radio.text.toString())
            if (radio.text.equals("Date")){
                mFUViewModel.sortbyDate.value = "desc"
                mFUViewModel.sortbybudget.value = ""
                mFUViewModel.sortbyRating.value = ""
                mFUViewModel.sortbynearby.value = ""
            }else if  (radio.text.equals("Budget")){
                mFUViewModel.sortbyDate.value = ""
                mFUViewModel.sortbybudget.value = "desc"
                mFUViewModel.sortbyRating.value = ""
                mFUViewModel.sortbynearby.value = ""
            }else if  (radio.text.equals("Rating")){
                mFUViewModel.sortbyDate.value = ""
                mFUViewModel.sortbybudget.value = ""
                mFUViewModel.sortbyRating.value = "desc"
                mFUViewModel.sortbynearby.value = ""
            }else if  (radio.text.equals("Nearby Location")){
                mFUViewModel.sortbyDate.value = ""
                mFUViewModel.sortbybudget.value = ""
                mFUViewModel.sortbyRating.value = ""
                mFUViewModel.sortbynearby.value = "desc"
            }else{
                  Log.d("Error", "errpr")
            }
            mFUViewModel.getUserbyList()
            mBinding.sortByOption.visibility = View.GONE
        }
/*
        //sort by date
        mBinding.sortByDate.setOnClickListener {
                if (mFUViewModel.sortbyDate.value.equals("asc")){
                    mFUViewModel.sortbyDate.value = "desc"
                    mBinding.sortByBudgetIv.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
                }else{
                    mFUViewModel.sortbyDate.value = "asc"
                    mBinding.sortByBudgetIv.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
                }
            mFUViewModel.getUserbyList()
            mBinding.sortByOption.visibility = View.GONE

        }
        mBinding.sortByDateIv.setOnClickListener {
            if (mFUViewModel.sortbyDate.value.equals("asc")){
                mFUViewModel.sortbyDate.value = "desc"
                mBinding.sortByDateIv.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
            }else{
                mFUViewModel.sortbyDate.value = "asc"
                mBinding.sortByDateIv.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
            }
            mFUViewModel.getUserbyList()
            mBinding.sortByOption.visibility = View.GONE
        }

        //sortby budget
        mBinding.sortByBudget.setOnClickListener {
            if (mFUViewModel.sortbybudget.value.equals("asc")){
                mFUViewModel.sortbybudget.value = "desc"
                mBinding.sortByBudgetIv.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
            }else{
                mFUViewModel.sortbybudget.value = "asc"
                mBinding.sortByBudgetIv.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
            }
            mFUViewModel.getUserbyList()
            mBinding.sortByOption.visibility = View.GONE
        }
        mBinding.sortByBudgetIv.setOnClickListener {
            if (mFUViewModel.sortbybudget.value.equals("asc")){
                mFUViewModel.sortbybudget.value = "desc"
                mBinding.sortByBudgetIv.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
            }else{
                mFUViewModel.sortbybudget.value = "asc"
                mBinding.sortByBudgetIv.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
            }
            mFUViewModel.getUserbyList()
            mBinding.sortByOption.visibility = View.GONE
        }

        //sort by rating
        mBinding.sortByRating.setOnClickListener {
            if (mFUViewModel.sortbyRating.value.equals("asc")){
                mFUViewModel.sortbyRating.value = "desc"
                mBinding.sortByRatingIv.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
            }else{
                mFUViewModel.sortbyRating.value = "asc"
                mBinding.sortByRatingIv.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
            }
            mFUViewModel.getUserbyList()
            mBinding.sortByOption.visibility = View.GONE
        }
        mBinding.sortByRatingIv.setOnClickListener {
            if (mFUViewModel.sortbyRating.value.equals("asc")){
                mFUViewModel.sortbyRating.value = "desc"
                mBinding.sortByRatingIv.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
            }else{
                mFUViewModel.sortbyRating.value = "asc"
                mBinding.sortByRatingIv.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
            }
            mFUViewModel.getUserbyList()
            mBinding.sortByOption.visibility = View.GONE
        }

         //sort by nearby

        mBinding.switchNearbyLocation.setOnCheckedChangeListener { button, checked ->
             if (checked){
                  mFUViewModel.sortbynearby.value = "Indore"
             }else{
                 mFUViewModel.sortbynearby.value = ""

             }
            mFUViewModel.getUserbyList()
            mBinding.sortByOption.visibility = View.GONE
        }*/


        mFUViewModel.userlist.observe(viewLifecycleOwner, Observer {
            mBinding.noUserFound.isVisible = it.isNullOrEmpty()
            mBinding.frelencerListRv.isVisible = it.isNullOrEmpty() == false
            val  adapte = FreelencerUserlistAdapte(JobApp.instance.applicationContext, it as ArrayList<FreelencerUserlistDTO>, this)
            mBinding.frelencerListRv.adapter = adapte

        })
        VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(mBinding.frelencerListRv))

    }

    override fun navigateToProfile(itemView: View, freelencerUserlistDTO: FreelencerUserlistDTO) {
                 val bundle = Bundle()
                 bundle.putString("profileid", freelencerUserlistDTO.user_id)
                findNavController().navigate(R.id.FUProfile, bundle)
    }


    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()

    }


}