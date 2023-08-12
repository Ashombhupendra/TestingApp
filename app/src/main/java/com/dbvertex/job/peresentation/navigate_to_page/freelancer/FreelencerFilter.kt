package com.dbvertex.job.peresentation.navigate_to_page.freelancer

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentFreelencerFilterBinding
import com.dbvertex.job.network.repository.AutoCompleteRepository
import com.dbvertex.job.network.response.placeList
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.peresentation.company_signup.AutoCompleteAdapter
import com.dbvertex.job.peresentation.company_signup.CompanyViewModel
import com.dbvertex.job.peresentation.company_signup.RVHost
import com.dbvertex.job.peresentation.freelancer_signup.Equipments
import com.dbvertex.job.peresentation.freelancer_signup.FreelancerViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FreelencerFilter :  Fragment(), DatePickerDialog.OnDateSetListener, RVHost {

    private lateinit var mBinding : FragmentFreelencerFilterBinding
    private val mCompanyViewModel by activityViewModels<CompanyViewModel>()
    private val mFreelancerViewModel by activityViewModels<FreelancerViewModel>()
    private val mFUViewModel by activityViewModels<FreelencerUserViewmodel>()
    var specialisationss = ArrayList<String>()
    val list = mutableListOf<String>()
    var listequip = ArrayList<String>()
    var datefrom = true
    var Timefrom = true
    var day = 0
    var month: Int = 0
    var year: Int = 0

    var hour: Int = 0
    var minute: Int = 0
    var second : Int = 0
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
        "₹1000-5000",
        "₹5000-10000",
        "₹10000-15000",
        "₹15000-20000",
        "₹20000 & above",
        "No bar"
    )
    var searchboolean : Boolean  = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFreelencerFilterBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@FreelencerFilter
              frag = this@FreelencerFilter
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            budgetSpinner()
            Categorydrowdown()
        mCompanyViewModel.getSpecialisation()
        mCompanyViewModel.specialisation.observe(requireActivity(), Observer {
            specialisationss.addAll(it.map { it.name.toString() })
            specialisationdrowdown(specialisationss)
        })

        mBinding.closeFrag.setOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.filEquipment.setOnClickListener {
            findNavController().navigate(R.id.addEquipmentDialog)
        }
        mFreelancerViewModel.finalequipments.observe(viewLifecycleOwner, Observer {

            if (it.isNullOrEmpty()){

            }else {

          //      mFreelancerViewModel.list.addAll(it)
                val name =
                    "${it.map { it.equipmentname }} , ${it.map { it.equipmentcompany }} ,${it.map { it.equipmentModel }}"

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
                listequip.add(final.replace("]", ""))
                chip.chipBackgroundColor =
                    requireContext().getResources().getColorStateList(R.color.blue_dark)
                chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
                chip.isCheckable = false
                chip.isClickable = false
                chip.setOnCloseIconClickListener {
                    mBinding.EquipmentChipBox.removeView(chip)
                   // showSnackBar("list sie ${listequip.size}")
                    if (listequip.size == 1){
                         val list = mutableListOf<Equipments>()
                          mFreelancerViewModel.finalequipments.value = list
                      //  showSnackBar("All items remove")
                    }
                    listequip.remove(chip.text.toString())
                }

                mBinding.EquipmentChipBox.addView(chip)

            }
        })


        mBinding.filterShowResult.setOnClickListener {


            if (!list.isNullOrEmpty()) {
                val strbul = StringBuilder()
                for (str in list) {
                    strbul.append(str)
                    //for adding comma between elements
                    strbul.append(",")
                }
                //just for removing last comma
                //strbul.setLength(strbul.length()-1);
                //just for removing last comma
                strbul.setLength(strbul.length - 1);
                val str = strbul.toString()
                mFUViewModel.mspecialisation.value = str

            }
            mFUViewModel.filterapply.value =  true
            mFUViewModel.getFilterlist()
            findNavController().navigateUp()

        }



        mBinding.filEtLocation.setOnClickListener {
            searchboolean = true
        }

        mBinding.filEtLocation.apply {
            addTextChangedListener {
                if (searchboolean){
                    getPlaceList(it.toString())

                }
            }

        }

        mBinding.selectFromDate.setOnClickListener {
             datefrom = true
            hideKeyboard(requireContext(), it)
            datepicker()
        }


        mBinding.selectToDate.setOnClickListener {
            datefrom = false
            hideKeyboard(requireContext(), it)
            datepicker()
        }
        mBinding.selectFromTime.setOnClickListener {
            hideKeyboard(requireContext(),it)
            if (mFUViewModel.mfromdate.value.isNullOrEmpty()){
                showSnackBar("Please firstly select From date !")
            }else{
                Timefrom =  true
                getTime()
            }

        }
        mBinding.selectToTime.setOnClickListener {
            hideKeyboard(requireContext(),it)
            if (mFUViewModel.mfromdate.value.isNullOrEmpty()){
                showSnackBar("Please firstly select To date !")
            }else{
                Timefrom =  false
                getTime()
            }

        }

    }

    fun specialisationdrowdown(lists: ArrayList<String>){

        mBinding.filSpeciSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.filSpeciSpinner)
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
                if (position.toLong() == mBinding.filSpeciSpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }





        mBinding.filSpeciSpinner.adapter = specialisationSpinner
        mBinding.filSpeciSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = specialisationSpinner.getItem(position)
                val indexs = lists.indexOf(conItem)

                if (indexs >=1) {

                   if (list.contains(conItem)){
                      showSnackBar("Already add $conItem")
                    }else {


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
                      chip.chipBackgroundColor =
                          context!!.getResources().getColorStateList(R.color.blue_dark)
                      chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
                      chip.isCheckable = false
                      chip.isClickable = false
                      chip.setOnCloseIconClickListener {
                          mBinding.specialisationChipBox.removeView(chip)
                          list.remove(chip.text.toString())
                      }

                      mBinding.specialisationChipBox.addView(chip)

                  }

                    ///
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
        mBinding.categorySpinner.setSelection(districtadapter.getPosition(mFUViewModel.mcategory.value.toString()))
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
                   // mFreelancerViewModel.category.value =conItem
                    mFUViewModel.mcategory.value = conItem
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
        mBinding.filBudgetSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.filBudgetSpinner)
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
                if (position.toLong() == mBinding.filBudgetSpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }
                return tv
            }
        }
        mBinding.filBudgetSpinner.adapter = districtadapter
        mBinding.filBudgetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = budgetlist.indexOf(conItem)
                if (indexs >=1){
                 //   Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                    when(indexs){
                        1 ->{
                            // Toast.makeText(requireContext(), "5000", Toast.LENGTH_SHORT).show()
                            mFUViewModel.mbudget.value = "5000"
                        }
                        2 ->{
                            // Toast.makeText(requireContext(), "10000", Toast.LENGTH_SHORT).show()
                            mFUViewModel.mbudget.value = "10000"
                        }
                        3 ->{
                            // Toast.makeText(requireContext(), "15000", Toast.LENGTH_SHORT).show()
                            mFUViewModel.mbudget.value = "15000"
                        }
                        4 ->{
                            // Toast.makeText(requireContext(), "20000", Toast.LENGTH_SHORT).show()
                            mFUViewModel.mbudget.value = "20000"
                        }
                        5 ->{
                            // Toast.makeText(requireContext(), "50000", Toast.LENGTH_SHORT).show()
                            mFUViewModel.mbudget.value = "50000"
                        }
                        else ->{
                            // Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                            mFUViewModel.mbudget.value = conItem
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

    fun getPlaceList(input : String){
        lifecycleScope.launch {
            val result = AutoCompleteRepository.getAutocompletelist(input)
            when(result){
                is ResultWrapper.Success ->{

                    Log.d("Places", result.response.prediction.map { it.description }.toString())

                    val adapter = AutoCompleteAdapter(result.response.prediction as ArrayList<placeList>, requireContext() ,this@FreelencerFilter)
                    mBinding.filLocRv.adapter = adapter
                    mBinding.filLocRv.visibility = View.VISIBLE


                }
                is ResultWrapper.Failure ->{
                    Log.d("Placesf", result.errorMessage.toString())
                }
            }
        }
    }

    override fun setText(itemView: View, listdata: placeList) {
        //  Toast.makeText(requireContext(), "${listdata.description}", Toast.LENGTH_SHORT).show()
        mBinding.filLocRv.visibility = View.GONE
        searchboolean = false
        mFUViewModel.mlocation.value = "${listdata.description}"
        mBinding.filEtLocation.setText("${listdata.description}")

        hideKeyboard(requireContext(), itemView)


    }

    private fun datepicker(){
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(requireContext(), R.style.MyDatePickerStyle, this ,year , month, day)
        datePickerDialog.show()
    }
    override fun onDateSet(view: DatePicker?, myear: Int, mmonth: Int, dayOfMonth: Int) {
        val SmyDay = dayOfMonth
        val SmyYear = myear
        val  SmyMonth = mmonth+1

        if (datefrom){
            mBinding.filDateFrom.setText("$SmyYear/$SmyMonth/$SmyDay")
            mFUViewModel.mfromdate.value = "$SmyYear-$SmyMonth-$SmyDay"
        }else{
            mBinding.filDateTo.setText("$SmyYear/$SmyMonth/$SmyDay")
            mFUViewModel.mtodate.value =  "$SmyYear-$SmyMonth-$SmyDay"
        }

    }

    private fun getTime(){
        val cal = Calendar.getInstance()
        TimePickerDialog(context,R.style.MyDatePickerStyle, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()


    }
    private val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        val cal =  Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)

        val time  = SimpleDateFormat("hh:mm a").format(cal.time)
        val sectime  = SimpleDateFormat("HH:mm:ss").format(cal.time)
       // mBinding.photoshootDateTime.append(", $time")
        if (Timefrom){
            mBinding.flFromTime.setText("$time")
            val selecteddate = mFUViewModel.mfromdate.value.toString()
            val newdatetime = "$selecteddate $sectime"
            mFUViewModel.mfromdate.value = newdatetime

        }else{
            mBinding.flToTime.setText("$time")
            val selecteddate = mFUViewModel.mtodate.value.toString()
            val newdatetime = "$selecteddate $sectime"
            mFUViewModel.mtodate.value = newdatetime
        }

    }

}