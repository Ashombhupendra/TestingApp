package com.dbvertex.job.peresentation.company_signup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentCompanyBinding
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable


class CompanyFragment : Fragment() {
    private lateinit var mBinding: FragmentCompanyBinding
    private val mCompanyViewModel by activityViewModels<CompanyViewModel>()
    val list = mutableListOf<String>()

    companion object {
        var userid: Int = 0
    }

    var specialisationss = ArrayList<String>()
    var experiencelists = arrayOf(
        "Choice your company's experience*",
        "> 1 year",
        "1 year",
        "2 years",
        "3 years",
        "4 years",
        "5 years",
        "6 years",
        "7 years",
        "8 years",
        "9 years",
        "10 years",
        "10+ years"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCompanyBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@CompanyFragment
            viewmodel = mCompanyViewModel
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (SharedPrefrenceHelper.isLoggedIn) {
            val user_id = SharedPrefrenceHelper.user.userid
            mBinding.createCompany.text = "Update Profile"
            mCompanyViewModel.getCompanyUser(user_id.toString())
            mCompanyViewModel.loadcomplete.observe(viewLifecycleOwner) {
                if (it) {

                    val specialisation =
                        mCompanyViewModel.CompanySpecification.value!!.split(",")
                            .map { it.trim() }
                    specialisation.forEach { conitem ->
                        val chip = Chip(requireContext())
                        val drawable = ChipDrawable.createFromAttributes(
                            requireContext(),
                            null,
                            0,
                            R.style.Widget_MaterialComponents_Chip_Entry
                        )
                        chip.setChipDrawable(drawable)
                        chip.setText(conitem)
                        list.add(conitem.toString())
                        chip.chipBackgroundColor = requireContext()!!.getResources()
                            .getColorStateList(R.color.blue_dark)
                        chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected)
                        chip.isCheckable = false
                        chip.isClickable = false
                        chip.setOnCloseIconClickListener {
                            mBinding.specialisationChipBox.removeView(chip)
                            list.remove(chip.text.toString())
                        }

                        mBinding.specialisationChipBox.addView(chip)


                    }


                }
                ///do here
            }
        }

        mCompanyViewModel.getSpecialisation()

        /**
         * CHECK STATE
         */


        mBinding.back.setOnClickListener {
            if (SharedPrefrenceHelper.isLoggedIn) {
                requireActivity().getViewModelStore().clear();
                findNavController().navigateUp()
            } else {
                findNavController().navigateUp()
            }

        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (SharedPrefrenceHelper.isLoggedIn) {
                    requireActivity().getViewModelStore().clear();
                    findNavController().navigateUp()
                } else {
                    findNavController().navigateUp()
                }

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback( this.viewLifecycleOwner, onBackPressedCallback)


        mCompanyViewModel.specialisation.observe(requireActivity(), Observer {
            specialisationss.addAll(it.map { it.name.toString() })
            specialisationdrowdown(specialisationss)
        })
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
        //binding.proCity.setSelection(conditionadapter.getPosition(citypos.value.toString()))
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


                        mCompanyViewModel.CompanyExperience.value = conItem


                    } else {
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }

        ////********assignment click event*********///

        mBinding.doTakeYes.setOnClickListener {
            mCompanyViewModel.takeInternationalAssignment.value = 1
        }
        mBinding.doTakeNo.setOnClickListener {
            mCompanyViewModel.takeInternationalAssignment.value = 0
        }

        ////********close assignment click event*********///

        mBinding.createCompany.setOnClickListener {
            // mCompanyViewModel.regCompanyUser()
            if (list.isNullOrEmpty()){
                 showSnackBar("Select atleast one specialisation")
            }else{
                val strbul = StringBuilder()
                for (str in list) {
                    strbul.append(str)
                    //for adding comma between elements
                    strbul.append(",")
                }
                strbul.setLength(strbul.length - 1);
                val str = strbul.toString()
                mCompanyViewModel.CompanySpecification.value = str

            if (SharedPrefrenceHelper.isLoggedIn) {
                mCompanyViewModel.updateCompanyUser()
            } else {
                mCompanyViewModel.regCompanyUser()

            }
            mCompanyViewModel.companystate.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    NetworkState.LOADING_STARTED -> {

                        mBinding.createCompany.apply {
                            showProgress()
                            isClickable = false
                        }
                        mCompanyViewModel.companystate.removeObservers(requireActivity())

                    }
                    NetworkState.LOADING_STOPPED -> {
                        mBinding.createCompany.apply {
                            hideProgress("Create My Account")
                            isClickable = true
                        }
                        mCompanyViewModel.companystate.removeObservers(requireActivity())

                    }
                    NetworkState.SUCCESS -> {
                        SharedPrefrenceHelper.isLoggedIn = true
                        mBinding.createCompany.apply {
                            hideProgress("Create My Account")
                            isClickable = true
                        }
                        findNavController().navigate(R.id.jobBoardMain)
                        mCompanyViewModel.companystate.removeObservers(requireActivity())

                    }
                    NetworkState.FAILED -> {
                        mBinding.createCompany.apply {
                            hideProgress("Create My Account")
                            isClickable = true
                        }
                        mCompanyViewModel.companystate.removeObservers(requireActivity())


                    }
                }

            })

            }
        }
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
                           showSnackBar("Already added $conItem")
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
}