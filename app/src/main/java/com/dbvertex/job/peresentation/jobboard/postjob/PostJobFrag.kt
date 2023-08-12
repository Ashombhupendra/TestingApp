package com.dbvertex.job.peresentation.jobboard.postjob

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPostJobBinding
import com.dbvertex.job.network.repository.AutoCompleteRepository
import com.dbvertex.job.network.response.placeList
import com.dbvertex.job.network.utils.*
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.peresentation.company_signup.AutoCompleteAdapter
import com.dbvertex.job.peresentation.company_signup.CompanyViewModel
import com.dbvertex.job.peresentation.company_signup.RVHost
import com.dbvertex.job.peresentation.jobboard.JobBoardMain
import com.dbvertex.job.peresentation.jobboard.JobboardViewmodel
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.launch


class PostJobFrag : Fragment() , RVHost {

      private lateinit var mBinding : FragmentPostJobBinding
    private  val mJobviewmodle   by activityViewModels<JobboardViewmodel>()
    private  val mpostjobviewmodel   by activityViewModels<PostJobViewmodel>()
    private val mCompanyViewModel by activityViewModels<CompanyViewModel>()
    var specialisationss = ArrayList<String>()
    var searchboolean : Boolean  = true
    val list = mutableListOf<String>()

    val  jobtype = listOf(
        "Select Job Type",
        "Freelance",
        "Fulltime",
        "Part Time",
        "Internship"
    )


    val categorylist = listOf(
        "Choose your category",
        "Administrative Assistant",
        "Executive Assistant",
        "Marketing Manager",
        "Customer Service Representative",
        "Nurse Practitioner",
        "Software Engineer",
        "Sales Manager",
        "Data Entry Clerk",
        "Real Estate",
        "Video Editing",
        "Office Assistant",
        "Internship"
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
    ):   View {
        mBinding = FragmentPostJobBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@PostJobFrag
            viewmodel = mpostjobviewmodel

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindProgressButton(mBinding.pjPost)
       // mCompanyViewModel.getSpecialisation()
        Categorydrowdown()
        Jobtypedropdown()
        mCompanyViewModel.specialisation.observe(requireActivity(), Observer {
            if (specialisationss.isNullOrEmpty()){
                specialisationss.addAll(it.map { it.name.toString() })
                specialisationdrowdown(specialisationss)
            }
        })

        mBinding.pjGuidelinesDes.setOnClickListener {
            findNavController().navigate(R.id.publishingGuidelines)
        }
        mBinding.pjLocation.setOnClickListener {
            searchboolean = true
            mBinding.pjScrollView.scrollTo(0,50)
        }
        mBinding.pjLocation.apply {
            addTextChangedListener {
                if (searchboolean){
                    getPlaceList(it.toString())
                }
            }
        }
//        else if (list.isNullOrEmpty()){
//            ErrorshowSnackBar("Select atleast one specialization")
//        }
        mBinding.pjPost.setOnClickListener {
            if (mpostjobviewmodel.mtitle.value.isNullOrEmpty()){
               mBinding.pjJobtitle.setError("Please enter title")
            }else if(mpostjobviewmodel.mCategory.value.isNullOrEmpty()){
                ErrorshowSnackBar("Please select category")
            }else if (mpostjobviewmodel.mdescription.value.isNullOrEmpty()){
               mBinding.pjJobDescription.setError("Please enter description")
            }else if (mpostjobviewmodel.mlocation.value.isNullOrEmpty()){
               mBinding.pjLocation.setError("Please enter location")
            }else if (mpostjobviewmodel.mtype.value.isNullOrEmpty()){
                ErrorshowSnackBar("Please select Job Type")
            }

//            else if (!mBinding.pjGuidelinesCheckbox.isChecked){
//                ErrorshowSnackBar("Please check guidelines")
//            }

            else{
//               val strbul = StringBuilder()
//               for (str in list) {
//                   strbul.append(str)
//                   //for adding comma between elements
//                   strbul.append(",")
//               }
               //just for removing last comma
               //strbul.setLength(strbul.length()-1);
               //just for removing last comma

//               strbul.setLength(strbul.length-1);
//               val str = strbul.toString()

               if (mBinding.pjCheckBoxUrgent.isChecked){
                   mpostjobviewmodel.mUrgent.value = "1"
               }else{
                   mpostjobviewmodel.mUrgent.value = "0"
               }
             //  mpostjobviewmodel.mSpecialisation.value = str

               mpostjobviewmodel.Postjob(SharedPrefrenceHelper.user.userid.toString())



               mpostjobviewmodel.postjobstate.observe(viewLifecycleOwner){ state ->
                   when(state){
                       NetworkState.LOADING_STARTED ->{
                           mBinding.pjPost.apply {
                               showProgress()
                               isClickable = false
                           }
                       }
                       NetworkState.LOADING_STOPPED ->{
                           mBinding.pjPost.apply {
                               hideProgress("Post")
                               isClickable = true
                           }
                       }
                       NetworkState.SUCCESS ->{
                           showSnackBar("Job Successfully added")

                           mJobviewmodle.mtitle.value = ""
                           mJobviewmodle.mdescription.value = ""
                           mJobviewmodle.mbudget.value = ""
                           mJobviewmodle.mlocation.value = ""
                           JobBoardMain.fragpostion.value = 0
                           mpostjobviewmodel.postjobstate.removeObservers(viewLifecycleOwner)
                           this.viewModelStore.clear()


                       }
                       NetworkState.FAILED ->{
                           ErrorshowSnackBar("Something went wrong!")
                       }
                   }

               }
           }

        }
    }



    fun Categorydrowdown(){

        /**
         * Freelancer experience start
         */
        mBinding.pjCategorySpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.pjCategorySpinner)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner_jobboard,
            categorylist

        ){


            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv : TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style

                if (position.toLong() == mBinding.pjCategorySpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#0E2E50"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }

        mBinding.pjCategorySpinner.adapter = districtadapter
        mBinding.pjCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = categorylist.indexOf(conItem)
                if (indexs >=1){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                   // Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                    mpostjobviewmodel.mCategory.value = conItem

                }else{

                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }


        }
        /**
         * Freelancer experience end
         */
    }
    fun Jobtypedropdown(){

        /**
         * Freelancer experience start
         */
        mBinding.pjJobTypeSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.pjJobTypeSpinner)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner_jobboard,
            jobtype

        ){


            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv : TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style
                if (position.toLong() == mBinding.pjJobTypeSpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#0E2E50"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.pjJobTypeSpinner.adapter = districtadapter
        mBinding.pjJobTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = jobtype.indexOf(conItem)
                if (indexs >=1){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                    // Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                    mpostjobviewmodel.mtype.value = conItem

                }else{

                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }


        }
        /**
         * Freelancer experience end
         */
    }


    fun specialisationdrowdown(lists: ArrayList<String>){

        mBinding.specialisationSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.specialisationSpinner)
                return false
            }

        })

        val specialisationSpinner = object : ArrayAdapter<String>(
           JobApp.instance.applicationContext, R.layout.item_spinner_jobboard, lists

        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv : TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style
                if (position.toLong() == mBinding.specialisationSpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#0E2E50"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }





        mBinding.specialisationSpinner.adapter = specialisationSpinner
        mBinding.specialisationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = specialisationSpinner.getItem(position)
                val indexs = lists.indexOf(conItem)

                if (indexs >=1) {

                    val chip = Chip(requireContext())
                    val drawable = ChipDrawable.createFromAttributes(
                        requireContext(),
                        null,
                        0,
                        R.style.Widget_MaterialComponents_Chip_Entry
                    )
                    chip.setChipDrawable(drawable)
                    chip.setText(conItem)
                    list.add(conItem.toString())
                    chip.chipBackgroundColor =  context!!.getResources().getColorStateList(R.color.blue_dark)
                    chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
                    chip.isCheckable = false
                    chip.isClickable = false
                    chip.setOnCloseIconClickListener {
                        mBinding.pjSpecialisationChipBox.removeView(chip)
                        list.remove(chip.text.toString())
                    }

                    mBinding.pjSpecialisationChipBox.addView(chip)



                    ///    Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                }
                else{

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }


        }
    }

    fun getPlaceList(input : String){
        lifecycleScope.launch {
            val result = AutoCompleteRepository.getAutocompletelist(input)
            when(result){
                is ResultWrapper.Success ->{

                    Log.d("Places", result.response.prediction.map { it.description }.toString())

                    val adapter = AutoCompleteAdapter(result.response.prediction as ArrayList<placeList>, requireContext() ,this@PostJobFrag)
                    mBinding.pjAutocompleteRecycler.adapter = adapter
                    mBinding.pjAutocompleteRecycler.visibility = View.VISIBLE


                }
                is ResultWrapper.Failure ->{
                    Log.d("Placesf", result.errorMessage.toString())
                }
            }
        }
    }

    override fun setText(itemView: View, listdata: placeList) {
        //  Toast.makeText(requireContext(), "${listdata.description}", Toast.LENGTH_SHORT).show()
        mBinding.pjAutocompleteRecycler.visibility = View.GONE
        mpostjobviewmodel.mlocation.value = "${listdata.description}"
        searchboolean = false
        hideKeyboard(requireContext(), itemView)


    }

}