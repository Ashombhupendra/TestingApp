package com.dbvertex.job.peresentation.other_signup

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
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentOtherSignupBinding
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.peresentation.company_signup.CompanyViewModel
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable

class OtherSignup : Fragment() {

    private lateinit var mBinding: FragmentOtherSignupBinding
    private val mCompanyViewModel by activityViewModels<CompanyViewModel>()
    private val mOtherViewModel by activityViewModels<OtherViewModel>()
    val list = mutableListOf<String>()
    var specialisationss = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentOtherSignupBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@OtherSignup
             viewmodel = mOtherViewModel



        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCompanyViewModel.getSpecialisation()

        mBinding.back.setOnClickListener {
            requireActivity().getViewModelStore().clear();
            findNavController().navigateUp()
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                requireActivity().getViewModelStore().clear();
               findNavController().navigateUp()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this.viewLifecycleOwner, onBackPressedCallback)

        if (SharedPrefrenceHelper.isLoggedIn){
            mBinding.createCompany.text = " Update Profile"
            mOtherViewModel.getOtherInforemation()
            mOtherViewModel.loader.observe(viewLifecycleOwner){
                if (it){
                    Log.d("specilisatinoother", mOtherViewModel.specialisation.value.toString())
                    if (mOtherViewModel.specialisation.value.isNullOrEmpty()){

                    }else{

                        val specialisation = mOtherViewModel.specialisation.value!!.split(",").map { it.trim() }
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




                }
            }

        }


        /**
         * CHECK STATE
         */



        mCompanyViewModel.specialisation.observe(requireActivity(), Observer {
            specialisationss.addAll(it.map { it.name.toString() })
            specialisationdrowdown(specialisationss)
        })




        mBinding.createCompany.setOnClickListener {
            // mCompanyViewModel.regCompanyUser()
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
            mOtherViewModel.specialisation.value = str

          //  mCompanyViewModel.regCompanyUser()
            if (SharedPrefrenceHelper.isLoggedIn){
                mOtherViewModel.updateOtheruser()
            }else{
                mOtherViewModel.regOtheruser()

            }

            mOtherViewModel.otherstate.observe(viewLifecycleOwner, Observer { state ->
                when(state){
                    NetworkState.LOADING_STARTED ->{

                        mBinding.createCompany.apply {
                            showProgress()
                            isClickable = false
                        }
                        mOtherViewModel.otherstate.removeObservers(requireActivity())

                    }
                    NetworkState.LOADING_STOPPED ->{
                        mBinding.createCompany.apply {
                            hideProgress("Request Your Account")
                            isClickable = true
                        }
                        mOtherViewModel.otherstate.removeObservers(requireActivity())

                    }
                    NetworkState.SUCCESS ->{
                        mBinding.createCompany.apply {
                            hideProgress("Success")
                            isClickable = true
                        }
                        requireActivity().viewModelStore.clear()
                        val navOptions = NavOptions.Builder().setPopUpTo(R.id.otherUserConfirmation, true).build()

                        findNavController().navigate(R.id.otherUserConfirmation, null, navOptions)


                      //  findNavController().navigate(R.id.homeScreen)
                      mOtherViewModel.otherstate.removeObservers(requireActivity())
                    }
                    NetworkState.FAILED ->{
                        mBinding.createCompany.apply {
                            hideProgress("Request Your Account")
                            isClickable = true
                        }
                        mOtherViewModel.otherstate.removeObservers(requireActivity())


                    }
                }

            })


        }
    }



    fun specialisationdrowdown(lists: ArrayList<String>){

        mBinding.specialisationSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.specialisationSpinner)
                return false
            }

        })

        val specialisationSpinner = object : ArrayAdapter<String>(
            requireContext(), R.layout.item_spinner, lists

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

                  if (list.contains(conItem)){
                      Toast.makeText(requireContext(), "Already add $conItem", Toast.LENGTH_SHORT).show()
                  }else{
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
                          mBinding.specialisationChipBox.removeView(chip)
                          list.remove(chip.text.toString())
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




}