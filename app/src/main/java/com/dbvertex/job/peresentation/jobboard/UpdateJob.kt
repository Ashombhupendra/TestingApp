package com.dbvertex.job.peresentation.jobboard

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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentUpdateJobBinding
import com.dbvertex.job.network.response.jobboard.jobsapplieduserlist
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.peresentation.company_signup.CompanyViewModel
import com.dbvertex.job.peresentation.jobboard.postjob.PostJobViewmodel
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.gson.Gson


class UpdateJob : Fragment() {
     private lateinit var mBinding : FragmentUpdateJobBinding
    private  val mpostjobviewmodel   by activityViewModels<PostJobViewmodel>()

    private val mCompanyViewModel by activityViewModels<CompanyViewModel>()
    var specialisationss = ArrayList<String>()
    val list = mutableListOf<String>()
    val  jobtype = listOf(
        "Select Job Type",
        "Freelancer",
        "Fulltime",
        "Parttime"
    )
    val categorylist = listOf(
        "Choose your category",
        "Photographer",
        "Drone",
        "Retoucher",
        "Cinematographer",
        "Editor",
        "Shooting Studio/Location",
        "Intern",
        "Rental Houses"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentUpdateJobBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@UpdateJob
            viewmodel=  mpostjobviewmodel
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

          val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event

                findNavController().navigateUp()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this.viewLifecycleOwner, onBackPressedCallback)

        mBinding.backPreviewContract.setOnClickListener {
            findNavController().navigateUp()
        }


        val bundle = requireArguments().getString("jobdetail")
        if (bundle!=null){
           val jobs = Gson().fromJson(bundle, jobsapplieduserlist::class.java)
            mpostjobviewmodel.setdefaultvalue(jobs)


            val specialiasationlist = mpostjobviewmodel.mSpecialisation.value!!.split(",").map { it.trim() }
            Log.d("specialisationupdate", specialiasationlist.toString())

            specialiasationlist.forEach {conItem->
                val chip  = Chip(context)
                val drawable = ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    R.style.Widget_MaterialComponents_Chip_Entry
                )
                chip.setChipDrawable(drawable)
                chip.setText(conItem)
                list.add(conItem.toString())
                chip.chipBackgroundColor =  JobApp.instance.applicationContext.getResources().getColorStateList(R.color.blue_dark)
                chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
                chip.isCheckable = false
                chip.isClickable = false
                chip.setOnCloseIconClickListener {
                    mBinding.pjSpecialisationChipBox.removeView(chip)
                    list.remove(chip.text.toString())
                }

                mBinding.pjSpecialisationChipBox.addView(chip)

            }
        }
        mCompanyViewModel.getSpecialisation()
        Categorydrowdown()
        Jobtypedropdown()


        mCompanyViewModel.specialisation.observe(viewLifecycleOwner){
            if (specialisationss.isNullOrEmpty()){
                specialisationss.addAll(it.map { it.name.toString() })
                specialisationdrowdown(specialisationss)
            }

        }

        if ( mpostjobviewmodel.mUrgent.value == "1"){
            mBinding.pjCheckBoxUrgent.isChecked = true
        }else{
            mBinding.pjCheckBoxUrgent.isChecked = false
        }

        mBinding.pjPost.setOnClickListener {
            if (mBinding.pjCheckBoxUrgent.isChecked){
                mpostjobviewmodel.mUrgent.value = "1"
            }else{
                mpostjobviewmodel.mUrgent.value = "0"
            }
            val strbul = StringBuilder()
            for (str in list) {
                strbul.append(str)
                //for adding comma between elements
                strbul.append(",")
            }
            //just for removing last comma
            //strbul.setLength(strbul.length()-1);
            //just for removing last comma
            strbul.setLength(strbul.length-1);
            val str = strbul.toString()
            mpostjobviewmodel.mSpecialisation.value = str
            mpostjobviewmodel.Updatejob()




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
                            hideProgress("Update")
                            isClickable = false
                        }
                    }
                    NetworkState.SUCCESS ->{
                        findNavController().navigate(R.id.jobBoardMain)
                        mpostjobviewmodel.postjobstate.removeObservers(viewLifecycleOwner)
                        requireActivity().getViewModelStore().clear();

                    }
                    NetworkState.FAILED ->{
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
            R.layout.item_spinner,
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
                    tv.background = ColorDrawable(Color.parseColor("#FF4444"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.pjCategorySpinner.adapter = districtadapter
        mBinding.pjCategorySpinner.setSelection(districtadapter.getPosition(mpostjobviewmodel.mCategory.value.toString()))
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
            R.layout.item_spinner,
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
                    tv.background = ColorDrawable(Color.parseColor("#FF4444"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.pjJobTypeSpinner.adapter = districtadapter
        mBinding.pjJobTypeSpinner.setSelection(districtadapter.getPosition(mpostjobviewmodel.mtype.value.toString()))
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
            JobApp.instance.applicationContext, R.layout.item_spinner, lists

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
                    tv.background = ColorDrawable(Color.parseColor("#FF4444"))
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
}