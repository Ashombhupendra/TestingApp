package com.dbvertex.job.peresentation.userprofile.updateprofile

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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentUpdatefreelencerproBinding
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.peresentation.company_signup.CompanyViewModel
import com.dbvertex.job.peresentation.freelancer_signup.FreelancerViewModel
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.gson.Gson


class Updatefreelencerpro : Fragment() {
    private lateinit var mBinding: FragmentUpdatefreelencerproBinding
    private val mCompanyViewModel by activityViewModels<CompanyViewModel>()
    private val mFreelancerViewModel by activityViewModels<FreelancerViewModel>()
    private val mUpdateProfileViewModel by activityViewModels<UpdateProfileViewModel>()
    var experiencelists = ArrayList<String>()
    var specialisationss = ArrayList<String>()
    var equipmentlist = ArrayList<String>()
    var equipmentID = ArrayList<String>()
    var Manufatureslist = ArrayList<String>()
    var ManufatureslistID = ArrayList<String>()
     var addequipmentenable : Boolean = false
    var  screen : Boolean = true
    var Modellist = ArrayList<String>()
    val list = mutableListOf<String>()
    val specialisationlist = mutableListOf<String>()
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
    val budgetlist = listOf(
        "Select your pricing range",
        "More than ₹20000",
        "₹15000-20000",
        "₹12000-15000",
        "₹10000-12000",
        "₹8000-10000",
        "₹5000-8000",
        "less than ₹5000"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentUpdatefreelencerproBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@Updatefreelencerpro


            viewmodel = mUpdateProfileViewModel



        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
              bindProgressButton(mBinding.createCompany)
        mUpdateProfileViewModel.getProfessionalSingleUser()
        mCompanyViewModel.getSpecialisation()
        mFreelancerViewModel.getEquipment()

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                requireActivity().getViewModelStore().clear();
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.profileFragment, true).build()
                findNavController().navigate(R.id.profileFragment, null , navOptions)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this.viewLifecycleOwner, onBackPressedCallback)



        mBinding.addEquipmentIv.setOnClickListener {
            addequipmentenable = true
            findNavController().navigate(R.id.addEquipmentDialog)
        }



        mUpdateProfileViewModel.equipmentlist.observe(requireActivity(), Observer {
            if (!mUpdateProfileViewModel.specialisation.value.isNullOrEmpty())
            {

                val specialiasationlist = mUpdateProfileViewModel.specialisation.value!!.split(",").map { it.trim() }
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
                    specialisationlist.add(conItem.toString())
                    chip.chipBackgroundColor =  JobApp.instance.applicationContext.getResources().getColorStateList(R.color.blue_dark)
                    chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
                    chip.isCheckable = false
                    chip.isClickable = false
                    chip.setOnCloseIconClickListener {
                        mBinding.specialisationChipBox.removeView(chip)
                        specialisationlist.remove(chip.text.toString())
                    }

                    mBinding.specialisationChipBox.addView(chip)

                }
            }


            if (it.isNullOrEmpty()){

            }else {

                   it.forEach {
                         val name =
                    "${it.equipmentname},${it.equipmentcompany},${it.equipmentModel}"

                val final = name.replace("[", "")

                val chip = Chip(requireContext())
                val drawable = ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    R.style.Widget_MaterialComponents_Chip_Entry
                )
                chip.setChipDrawable(drawable)
                chip.setText(final.replace("]", ""))
                list.addAll(listOf(final.replace("]", "")))
                chip.chipBackgroundColor =
                    requireContext().getResources().getColorStateList(R.color.blue_dark)
                chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
                chip.isCheckable = false
                chip.isClickable = false
                chip.setOnCloseIconClickListener {
                    mBinding.EquipmentChipBox.removeView(chip)
                    list.remove(chip.text.toString())
                }

                mBinding.EquipmentChipBox.addView(chip)

            }

                   }


        })

        mFreelancerViewModel.finalequipments.observe(requireActivity(), Observer {

            if (it.isNullOrEmpty()){

            }else {

                val name =
                    "${it.map { it.equipmentname }}, ${it.map { it.equipmentcompany }}, ${it.map { it.equipmentModel }}"

                val final = name.replace("[", "")
                 list.addAll(listOf(name))


                val chip = Chip(requireContext())
                val drawable = ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    R.style.Widget_MaterialComponents_Chip_Entry
                )
                chip.setChipDrawable(drawable)
                chip.setText(final.replace("]", ""))

                chip.chipBackgroundColor =
                    requireContext().getResources().getColorStateList(R.color.blue_dark)
                chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
                chip.isCheckable = false
                chip.isClickable = false
                chip.setOnCloseIconClickListener {
                    mBinding.EquipmentChipBox.removeView(chip)
                    list.remove(chip.text.toString())
                }

                mBinding.EquipmentChipBox.addView(chip)

            }
        })

        budgetSpinner()


        Categorydrowdown()
        equipmentlist.add("Add Equipment")
        equipmentID.add("-1")



        mFreelancerViewModel.equipmentList.observe(requireActivity(), Observer {
            equipmentlist.addAll(it.map { it.name })
            equipmentID.addAll(it.map { it.id })
            EquipmentSpinner(equipmentlist)

        })

        mFreelancerViewModel.manufactureList.observe(requireActivity(), Observer {
            Manufatureslist.addAll(it.map { it.company_name })
            ManufatureslistID.addAll(it.map { it.id })
            ManuFactureSpinner(Manufatureslist)
        })


        mFreelancerViewModel.EqModelList.observe(requireActivity(), Observer {
            Modellist.addAll(it.map { it.model_name })
            ModelSpinner(Modellist)
        })

        mCompanyViewModel.specialisation.observe(requireActivity(), Observer {
            specialisationss.addAll(it.map { it.name.toString() })
            specialisationdrowdown(specialisationss)
        })
        experiencelists.add("Experience*")

        for (exp in 0..50){
            if (exp > 1)  experiencelists.add("$exp years") else  experiencelists.add("$exp year")
        }
        experiencelist()


        mBinding.createCompany.setOnClickListener {

            updateprofileobserver()

            if (specialisationlist.isNullOrEmpty()){
                showSnackBar("Select specialisation")
            }else{
                val strbul = StringBuilder()
                for (str in specialisationlist) {
                    strbul.append(str)
                    //for adding comma between elements
                    strbul.append(",")
                }
                //just for removing last comma
                //strbul.setLength(strbul.length()-1);
                //just for removing last comma
                strbul.setLength(strbul.length - 1);
                val str = strbul.toString()
                mUpdateProfileViewModel.specialisation.value = str
                if (mBinding.checkBox.isChecked) {
                    mUpdateProfileViewModel.passport.value = 1
                    val gson = Gson()
                    Log.d("equipmentlistsssssss", gson.toJson(list))
                    mUpdateProfileViewModel.UpdatePRofeesionalFreelancerProfile(gson.toJson(list))

                } else {
                    mFreelancerViewModel.passport.value = 0
                    val gson = Gson()
                    Log.d("equipmentlistsssssss", gson.toJson(list))
                    mUpdateProfileViewModel.UpdatePRofeesionalFreelancerProfile(gson.toJson(list))
                }

            }
        }



    }

    fun experiencelist(){
        /**
         * Freelancer experience start
         */
        mBinding.comExperience.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.comExperience)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner,
            experiencelists

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
                if (position.toLong() == mBinding.comExperience.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.comExperience.adapter = districtadapter
        mBinding.comExperience.setSelection(districtadapter.getPosition(mUpdateProfileViewModel.experience.value.toString()))
        mBinding.comExperience.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = experiencelists.indexOf(conItem)
                if (indexs >=1){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                  //  Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                    // mCompanyViewModel.CompanyExperience.value = conItem

                    mUpdateProfileViewModel.experience.value = conItem
                    // sellProductViewModel.selectconditionID.value = conItem

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
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
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
                    mBinding.specialisationChipBox.visibility = View.VISIBLE
                  if (specialisationlist.contains(conItem)){
                        Toast.makeText(requireContext(), "Already added $conItem", Toast.LENGTH_SHORT).show()
                    }else {

                      val chip = Chip(context)
                      val drawable = ChipDrawable.createFromAttributes(
                          requireContext(),
                          null,
                          0,
                          R.style.Widget_MaterialComponents_Chip_Entry
                      )
                      chip.setChipDrawable(drawable)
                      chip.setText(conItem)
                      specialisationlist.add(conItem.toString())
                      chip.chipBackgroundColor =
                          context!!.getResources().getColorStateList(R.color.blue_dark)
                      chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
                      chip.isCheckable = false
                      chip.isClickable = false
                      chip.setOnCloseIconClickListener {
                          mBinding.specialisationChipBox.removeView(chip)
                          specialisationlist.remove(chip.text.toString())
                      }

                      mBinding.specialisationChipBox.addView(chip)

                  }

                    ///    Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                }
                else{

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }


        }
    }


    fun Categorydrowdown(){

        /**
         * Freelancer experience start
         */
        mBinding.categorySpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.categorySpinner)
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
                if (position.toLong() == mBinding.categorySpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.categorySpinner.adapter = districtadapter
        mBinding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
                    mUpdateProfileViewModel.category.value =conItem
                    // sellProductViewModel.selectconditionID.value = conItem

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

    fun budgetSpinner(){
        mBinding.addBudgetSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.addBudgetSpinner)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner,
            budgetlist

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
                if (position.toLong() == mBinding.addBudgetSpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addBudgetSpinner.adapter = districtadapter
        mBinding.addBudgetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = budgetlist.indexOf(conItem)
                if (indexs >=1){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                    when(indexs){
                        1 ->{
                            // Toast.makeText(requireContext(), "5000", Toast.LENGTH_SHORT).show()
                            mUpdateProfileViewModel.budget.value = "5000"
                        }
                        2 ->{
                            // Toast.makeText(requireContext(), "10000", Toast.LENGTH_SHORT).show()
                            mUpdateProfileViewModel.budget.value = "10000"
                        }
                        3 ->{
                            // Toast.makeText(requireContext(), "15000", Toast.LENGTH_SHORT).show()
                            mUpdateProfileViewModel.budget.value = "15000"
                        }
                        4 ->{
                            // Toast.makeText(requireContext(), "20000", Toast.LENGTH_SHORT).show()
                            mUpdateProfileViewModel.budget.value = "20000"
                        }
                        5 ->{
                            // Toast.makeText(requireContext(), "50000", Toast.LENGTH_SHORT).show()
                            mUpdateProfileViewModel.budget.value = "50000"
                        }
                        else ->{
                            // Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                            mUpdateProfileViewModel.budget.value = conItem
                        }
                    }

                    //.
                    // mFreelancerViewModel.budget.value = conItem

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

    fun  EquipmentSpinner(lists : ArrayList<String>){

        mBinding.addEquipmentSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.addEquipmentSpinner)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            JobApp.instance.applicationContext,
            R.layout.item_spinner,
            lists

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
                if (position.toLong() == mBinding.addEquipmentSpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#FF4444"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addEquipmentSpinner.adapter = districtadapter
        mBinding.addEquipmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = lists.indexOf(conItem)
                if (indexs >=1){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                    Toast.makeText(requireContext(), conItem + indexs +"Listid : ${equipmentID[position]}", Toast.LENGTH_SHORT).show()
                    mFreelancerViewModel.getManufacturers(equipmentID[position])
               //     mFreelancerViewModel.equipments.value = conItem

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

    fun  ManuFactureSpinner(lists : ArrayList<String>){

        mBinding.addEquipmentSpinnerCompany.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.addEquipmentSpinnerCompany)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner,
            lists

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
                if (position.toLong() == mBinding.addEquipmentSpinnerCompany.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#FF4444"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addEquipmentSpinnerCompany.adapter = districtadapter
        mBinding.addEquipmentSpinnerCompany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = lists.indexOf(conItem)
                if (indexs >=0){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                    //   Toast.makeText(requireContext(), conItem + indexs +"Listid : ${equipmentID[position]}", Toast.LENGTH_SHORT).show()
                    mFreelancerViewModel.getModel(ManufatureslistID[position])
                 //   mFreelancerViewModel.manufature.value = conItem

                }else{

                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }


        }
        /**
         * Freelancer experience en d
         */
    }


    fun  ModelSpinner(lists : ArrayList<String>){

        mBinding.addEquipmentSpinnerModel.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.addEquipmentSpinnerModel)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner,
            lists

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
                if (position.toLong() == mBinding.addEquipmentSpinnerModel.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#FF4444"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addEquipmentSpinnerModel.adapter = districtadapter
        mBinding.addEquipmentSpinnerModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = lists.indexOf(conItem)
                if (indexs >=0){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                    //   Toast.makeText(requireContext(), conItem + indexs , Toast.LENGTH_SHORT).show()
                 //   mFreelancerViewModel.model.value = conItem

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


    override fun onPause() {
        super.onPause()


    }


    override fun onResume() {
        super.onResume()


    }


    fun updateprofileobserver(){
        mUpdateProfileViewModel.uploadprofessionalprofilestate.observe(requireActivity(), Observer { state ->
            when(state){
                NetworkState.LOADING_STARTED ->{
                    mBinding.createCompany.apply {
                        showProgress()
                        isClickable = false
                    }
                }
                NetworkState.SUCCESS ->{
                    mBinding.createCompany.apply {
                        hideProgress("Update Profile")
                        isClickable = true
                    }
                    requireActivity().getViewModelStore().clear();

                    val navOptions = NavOptions.Builder().setPopUpTo(R.id.profileFragment, true).build()

                    findNavController().navigate(R.id.profileFragment, null , navOptions)
                    mUpdateProfileViewModel.uploadprofessionalprofilestate.removeObservers(requireActivity())
                }
                NetworkState.FAILED ->{
                    mBinding.createCompany.apply {
                        hideProgress("Update Profile")
                        isClickable = true
                    }
                }
            }

        })
    }




}