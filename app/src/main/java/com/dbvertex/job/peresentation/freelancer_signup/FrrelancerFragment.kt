package com.dbvertex.job.peresentation.freelancer_signup

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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentFrrelancerBinding
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.peresentation.company_signup.CompanyViewModel
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.gson.Gson


class FrrelancerFragment : Fragment() {
    private lateinit var mBinding: FragmentFrrelancerBinding
    private val mCompanyViewModel by activityViewModels<CompanyViewModel>()
    private val mFreelancerViewModel by activityViewModels<FreelancerViewModel>()
    var experiencelists = ArrayList<String>()
    var specialisationss = ArrayList<String>()
    var equipmentlist = ArrayList<String>()
    var equipmentID = ArrayList<String>()
    var Manufatureslist = ArrayList<String>()
    var ManufatureslistID = ArrayList<String>()

    var Modellist = ArrayList<String>()
    val list = mutableListOf<String>()
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
    ): View {
        mBinding = FragmentFrrelancerBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@FrrelancerFragment
            viewmodel = mFreelancerViewModel
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCompanyViewModel.getSpecialisation()
        mFreelancerViewModel.getEquipment()

        mBinding.back.setOnClickListener {
            findNavController().navigate(R.id.signUp)
        }

        mBinding.addEquipmentIv.setOnClickListener {
            findNavController().navigate(R.id.addEquipmentDialog)
        }

        mFreelancerViewModel.finalequipments.observe(requireActivity(), Observer {

            if (it.isNullOrEmpty()) {

            } else {

                mFreelancerViewModel.list.addAll(it)
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


        /**
         * CHECK STATE
         */
        budgetSpinner()
        mFreelancerViewModel.freelancestate.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                NetworkState.LOADING_STARTED -> {

                    mBinding.createCompany.apply {
                        showProgress()
                        isClickable = false
                    }
                }
                NetworkState.LOADING_STOPPED -> {
                    mBinding.createCompany.apply {
                        hideProgress("Create My Account")
                        isClickable = true
                    }
                }
                NetworkState.SUCCESS -> {
                    requireActivity().getViewModelStore().clear();
                    SharedPrefrenceHelper.isLoggedIn = true
                    mBinding.createCompany.apply {
                        hideProgress("Create My Account")
                        isClickable = true
                    }
                    findNavController().navigate(R.id.jobBoardMain)
                }
                NetworkState.FAILED -> {
                    mBinding.createCompany.apply {
                        hideProgress("Create My Account")
                        isClickable = true
                    }

                }
            }

        })


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

        for (exp in 0..50) {
            if (exp > 1) experiencelists.add("$exp years") else experiencelists.add("$exp year")

        }
        experiencelist()




        mBinding.createCompany.setOnClickListener {

            if (list.isNullOrEmpty()) {
                showSnackBar("Select specialisation")
            } else {
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
                mFreelancerViewModel.specialisation.value = str
                if (mBinding.checkBox.isChecked) {
                    mFreelancerViewModel.passport.value = 1
                    val gson = Gson()
                    Log.d("Add Equipment test", gson.toJson(mFreelancerViewModel.list).toString() )
                   mFreelancerViewModel.createFreelancer(gson.toJson(mFreelancerViewModel.list))
                } else {
                    mFreelancerViewModel.passport.value = 0
                    val gson = Gson()
                    Log.d("Add Equipment test", gson.toJson(mFreelancerViewModel.list).toString() )
                   mFreelancerViewModel.createFreelancer(gson.toJson(mFreelancerViewModel.list))
                }

            }
        }

    }


    fun experiencelist() {
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

        ) {


            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style
                if (position.toLong() == mBinding.comExperience.selectedItemPosition.toLong() && position != 0) {
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                } else {
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.comExperience.adapter = districtadapter
        mBinding.comExperience.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val conItem = districtadapter.getItem(position)
                    val indexs = experiencelists.indexOf(conItem)
                    if (indexs >= 1) {
                        // mCompanyViewModel.CompanyExperience.value = conItem

                        mFreelancerViewModel.experience.value = conItem
                        // sellProductViewModel.selectconditionID.value = conItem

                    } else {

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }


            }
        /**
         * Freelancer experience end
         */
    }


    fun specialisationdrowdown(lists: ArrayList<String>) {

        mBinding.specialisationSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.specialisationSpinner)
                return false
            }

        })

        val specialisationSpinner = object : ArrayAdapter<String>(
            requireContext(), R.layout.item_spinner, lists

        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style
                if (position.toLong() == mBinding.specialisationSpinner.selectedItemPosition.toLong() && position != 0) {
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                } else {
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }





        mBinding.specialisationSpinner.adapter = specialisationSpinner
        mBinding.specialisationSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val conItem = specialisationSpinner.getItem(position)
                    val indexs = lists.indexOf(conItem)

                    if (indexs >= 1) {
                        mBinding.specialisationChipBox.visibility = View.VISIBLE

                        if (list.contains(conItem)) {
                            Toast.makeText(
                                requireContext(),
                                "Already added $conItem",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {


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
                        ///    Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                    } else {

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }


            }
    }


    fun Categorydrowdown() {

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

        ) {


            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style
                if (position.toLong() == mBinding.categorySpinner.selectedItemPosition.toLong() && position != 0) {
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                } else {
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.categorySpinner.adapter = districtadapter
        mBinding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val conItem = districtadapter.getItem(position)
                    val indexs = categorylist.indexOf(conItem)
                    if (indexs >= 1) {
                        //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                        mFreelancerViewModel.category.value = conItem
                        // sellProductViewModel.selectconditionID.value = conItem

                    } else {

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }


            }
        /**
         * Freelancer experience end
         */
    }

    fun budgetSpinner() {
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

        ) {


            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style
                if (position.toLong() == mBinding.addBudgetSpinner.selectedItemPosition.toLong() && position != 0) {
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                } else {
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addBudgetSpinner.adapter = districtadapter
        mBinding.addBudgetSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val conItem = districtadapter.getItem(position)
                    val indexs = budgetlist.indexOf(conItem)
                    if (indexs >= 1) {
                        //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                        when (indexs) {
                            1 -> {
                                // Toast.makeText(requireContext(), "5000", Toast.LENGTH_SHORT).show()
                                mFreelancerViewModel.budget.value = "5000"
                            }
                            2 -> {
                                // Toast.makeText(requireContext(), "10000", Toast.LENGTH_SHORT).show()
                                mFreelancerViewModel.budget.value = "10000"
                            }
                            3 -> {
                                // Toast.makeText(requireContext(), "15000", Toast.LENGTH_SHORT).show()
                                mFreelancerViewModel.budget.value = "15000"
                            }
                            4 -> {
                                // Toast.makeText(requireContext(), "20000", Toast.LENGTH_SHORT).show()
                                mFreelancerViewModel.budget.value = "20000"
                            }
                            5 -> {
                                // Toast.makeText(requireContext(), "50000", Toast.LENGTH_SHORT).show()
                                mFreelancerViewModel.budget.value = "50000"
                            }
                            else -> {
                                // Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                                mFreelancerViewModel.budget.value = conItem
                            }
                        }

                        //.
                        // mFreelancerViewModel.budget.value = conItem

                    } else {

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }


            }
        /**
         * Freelancer experience end
         */
    }

    fun EquipmentSpinner(lists: ArrayList<String>) {

        mBinding.addEquipmentSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.addEquipmentSpinner)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner,
            lists

        ) {


            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style
                if (position.toLong() == mBinding.addEquipmentSpinner.selectedItemPosition.toLong() && position != 0) {
                    tv.background = ColorDrawable(Color.parseColor("#FF4444"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                } else {
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addEquipmentSpinner.adapter = districtadapter
        mBinding.addEquipmentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val conItem = districtadapter.getItem(position)
                    val indexs = lists.indexOf(conItem)
                    if (indexs >= 1) {
                        //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                        //   Toast.makeText(requireContext(), conItem + indexs +"Listid : ${equipmentID[position]}", Toast.LENGTH_SHORT).show()
                        mFreelancerViewModel.getManufacturers(equipmentID[position])
                        mFreelancerViewModel.equipments.value = conItem

                    } else {

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }


            }
        /**
         * Freelancer experience end
         */
    }

    fun ManuFactureSpinner(lists: ArrayList<String>) {

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

        ) {


            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style
                if (position.toLong() == mBinding.addEquipmentSpinnerCompany.selectedItemPosition.toLong() && position != 0) {
                    tv.background = ColorDrawable(Color.parseColor("#FF4444"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                } else {
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addEquipmentSpinnerCompany.adapter = districtadapter
        mBinding.addEquipmentSpinnerCompany.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val conItem = districtadapter.getItem(position)
                    val indexs = lists.indexOf(conItem)
                    if (indexs >= 0) {
                        //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                        //   Toast.makeText(requireContext(), conItem + indexs +"Listid : ${equipmentID[position]}", Toast.LENGTH_SHORT).show()
                        mFreelancerViewModel.getModel(ManufatureslistID[position])
                        mFreelancerViewModel.manufature.value = conItem

                    } else {

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }


            }
        /**
         * Freelancer experience end
         */
    }


    fun ModelSpinner(lists: ArrayList<String>) {

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

        ) {


            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style
                if (position.toLong() == mBinding.addEquipmentSpinnerModel.selectedItemPosition.toLong() && position != 0) {
                    tv.background = ColorDrawable(Color.parseColor("#FF4444"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                } else {
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addEquipmentSpinnerModel.adapter = districtadapter
        mBinding.addEquipmentSpinnerModel.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val conItem = districtadapter.getItem(position)
                    val indexs = lists.indexOf(conItem)
                    if (indexs >= 0) {
                        //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                        //   Toast.makeText(requireContext(), conItem + indexs , Toast.LENGTH_SHORT).show()
                        mFreelancerViewModel.model.value = conItem

                    } else {

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }


            }
        /**
         * Freelancer experience end
         */
    }


    fun validation() {
        val hasCategory = mFreelancerViewModel.category.value.isNullOrEmpty()
        val hasspecialisation = mFreelancerViewModel.specialisation.value.isNullOrEmpty()
        val hasExperience = mFreelancerViewModel.specialisation.value.isNullOrEmpty()
        val hasEquipment = mFreelancerViewModel.equipments.value.isNullOrEmpty()
        val hasaboutme = mFreelancerViewModel.aboutme.value.isNullOrEmpty()
        val haspassport = mFreelancerViewModel.passport.value.toString().isNullOrEmpty()
        if (hasCategory) {

        } else if (hasspecialisation) {

        } else if (hasExperience) {

        } else if (hasEquipment) {

        } else if (hasaboutme) {
            mBinding.notessss.setError("Enter why you want to join this platform?")
        } else if (haspassport) {

        } else {


        }


    }

}