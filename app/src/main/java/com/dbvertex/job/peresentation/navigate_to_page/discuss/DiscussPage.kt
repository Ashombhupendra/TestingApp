package com.dbvertex.job.peresentation.navigate_to_page.discuss

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentDiscussPageBinding
import com.dbvertex.job.network.response.discuss.AllDiscussionDTO
import com.dbvertex.job.network.utils.hideKeyboard
import com.google.android.material.transition.MaterialFadeThrough

class DiscussPage(val favboolean: Boolean) : Fragment(), onDiscussClick {
    private lateinit var mBinding : FragmentDiscussPageBinding
    private val mDiscussViewModel by activityViewModels<DiscussViewModel>()
    val discussfilter = listOf(
        "All Questions",
        "Trending Questions",
        "Unanswered Questions",
        "Followed Questions",
        "My Questions"
        )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDiscussPageBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@DiscussPage

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        mBinding.discusEtSearch.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", "discuss")
            findNavController().navigate(R.id.searchDiscuss, bundle)
        }
        Discussfilterdrowdown()

        mBinding.discusTopSubDescription.setOnClickListener {
            findNavController().navigate(R.id.beforepostingDialog)
        }

        if (favboolean == true){

            mBinding.discusAddQustions.visibility = View.VISIBLE
            mBinding.discusEtSearch.visibility = View.VISIBLE
            mBinding.discusTopDescription.visibility = View.VISIBLE
            mBinding.discusTopSubDescription.visibility = View.VISIBLE
            mBinding.discusTopTitle.visibility = View.VISIBLE
            mBinding.discusDivider.visibility = View.VISIBLE
            mBinding.discussFilter.visibility =View.VISIBLE

            mDiscussViewModel.alldiscussion.observe(viewLifecycleOwner, Observer {
                mBinding.discussQuestionRv.isVisible = it.isNullOrEmpty() == false
                mBinding.noQuestionFound.isVisible = it.isNullOrEmpty()
                val adapter = AllDiscussionAdapter(JobApp.instance.applicationContext, it, this )
                mBinding.discussQuestionRv.adapter = adapter
                adapter.notifyDataSetChanged()
            })

           // VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(mBinding.scrollView))




            mBinding.discusAddQustions.setOnClickListener {
                findNavController().navigate(R.id.addQuestion_frag)

            }
        }else{

            mBinding.discusAddQustions.visibility = View.GONE
            mBinding.discusEtSearch.visibility = View.GONE
            mBinding.discusTopDescription.visibility = View.GONE
            mBinding.discusTopSubDescription.visibility = View.GONE
            mBinding.discusTopTitle.visibility = View.GONE
            mBinding.discusDivider.visibility = View.GONE
            mBinding.discussFilter.visibility =View.GONE
          mDiscussViewModel.getFavDiscuss()
            mDiscussViewModel.alldiscussion.observe(viewLifecycleOwner, Observer {
                val adapter = AllDiscussionAdapter(JobApp.instance.applicationContext, it, this )
                mBinding.discussQuestionRv.adapter = adapter
            })
        }






    }

    override fun onDiscussclick(allDiscussionDTO: AllDiscussionDTO) {
        mDiscussViewModel.getSingleQuestion(allDiscussionDTO.questionid)
        mDiscussViewModel.questionid.value = allDiscussionDTO.questionid
        findNavController().navigate(R.id.discussDetail)
    }

    override fun onDiscusslike(allDiscussionDTO: AllDiscussionDTO) {
             mDiscussViewModel.setFavUnfavDiscuss(allDiscussionDTO.questionid)
    }


    fun Discussfilterdrowdown(){

        mBinding.filterDiscussSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.filterDiscussSpinner)
                return false
            }

        })

        val FilterSpinner = object : ArrayAdapter<String>(
            requireContext(), R.layout.item_spinner_discuss, discussfilter

        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv : TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,15F)
                // set selected item style

                if (position.toLong() == mBinding.filterDiscussSpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#223A54"))

                }
                return tv
            }
        }





        mBinding.filterDiscussSpinner.adapter = FilterSpinner
        mBinding.filterDiscussSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = FilterSpinner.getItem(position)
                val indexs = discussfilter.indexOf(conItem)

                mDiscussViewModel.getAllDiscussion(conItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }


        }
    }



}