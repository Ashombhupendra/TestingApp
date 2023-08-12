package com.dbvertex.job.peresentation.userprofile.updateprofile

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentUpdatePersonalProBinding
import com.dbvertex.job.network.repository.AutoCompleteRepository
import com.dbvertex.job.network.response.placeList
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.peresentation.company_signup.AutoCompleteAdapter
import com.dbvertex.job.peresentation.company_signup.RVHost
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kotlinx.coroutines.launch
import java.util.*


class UpdatePersonalPro : Fragment(), DatePickerDialog.OnDateSetListener, RVHost {
    private lateinit var mBinding: FragmentUpdatePersonalProBinding
    private val mUpdateProfileViewmodel by activityViewModels<UpdateProfileViewModel>()
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var searchboolean: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            FragmentUpdatePersonalProBinding.inflate(layoutInflater, container, false).apply {
                lifecycleOwner = this@UpdatePersonalPro
                viewModel = mUpdateProfileViewmodel
            }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindProgressButton(mBinding.createMyAcc)
        mUpdateProfileViewmodel.getUserPersonaldetail(SharedPrefrenceHelper.user.userid.toString())
        mBinding.back.setOnClickListener {
//            findNavController().popBackStack(R.id.updatePersonalPro,true)
//            findNavController().navigate(R.id.profileFragment)
//            requireActivity().viewModelStore.clear()

            findNavController().navigateUp()
        }

        mBinding.calender.setOnClickListener {
            hideKeyboard(requireContext(), mBinding.calender)
            datepicker()
        }
        mBinding.clickdob.setOnClickListener {
            hideKeyboard(requireContext(), mBinding.comProDob)
            datepicker()
        }
        mBinding.comProManualaddress.setOnClickListener {
            //  searchboolean = true
        }
        mBinding.comProManualaddress.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                searchboolean = true
            }
        }
        mBinding.comProManualaddress.apply {
            addTextChangedListener {
                if (searchboolean) {
                    getPlaceList(it.toString())
                }
            }
        }
        mBinding.createMyAcc.setOnClickListener {
            mUpdateProfileViewmodel.updatepersonalprofile()
        }
        mUpdateProfileViewmodel.uploadpersonalprofilestate.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { state ->
                when (state) {
                    NetworkState.LOADING_STARTED -> {
                        mBinding.createMyAcc.apply {
                            showProgress()
                            isClickable = false
                        }
                    }
                    NetworkState.LOADING_STOPPED -> {
                        mBinding.createMyAcc.apply {
                            hideProgress("Update")
                            isClickable = true
                        }
                    }
                    NetworkState.SUCCESS -> {

                        showSnackBar("Profile Updated")

                        mBinding.createMyAcc.apply {
                            hideProgress("Update")
                            isClickable = true
                        }
                        requireActivity().viewModelStore.clear()
                        findNavController().navigateUp()
                    }
                    NetworkState.FAILED -> {
                        mBinding.createMyAcc.apply {
                            hideProgress("Update")
                            isClickable = true
                        }
                    }
                }

            })


    }

    private fun datepicker() {
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.MyDatePickerStyle,
            this,
            year - 18,
            month,
            day
        )
        val maxDate = Calendar.getInstance()
        maxDate[Calendar.DAY_OF_MONTH] = day
        maxDate[Calendar.MONTH] = month
        maxDate[Calendar.YEAR] = year - 18
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, myear: Int, mmonth: Int, dayOfMonth: Int) {
        val smyDay = dayOfMonth
        val smyYear = myear
        val smyMonth = mmonth + 1

        mUpdateProfileViewmodel.dateofbirth.value = "$smyDay-$smyMonth-$smyYear"
        mBinding.comProDob.setText("$smyDay-$smyMonth-$smyYear")


    }

    fun getPlaceList(input: String) {
        lifecycleScope.launch {
            val result = AutoCompleteRepository.getAutocompletelist(input)
            when (result) {
                is ResultWrapper.Success -> {

                    Log.d("Places", result.response.prediction.map { it.description }.toString())

                    val adapter = AutoCompleteAdapter(
                        result.response.prediction as ArrayList<placeList>,
                        requireContext(),
                        this@UpdatePersonalPro
                    )
                    mBinding.companyRecycler.adapter = adapter
                    mBinding.companyRecycler.visibility = View.VISIBLE


                }
                is ResultWrapper.Failure -> {
                    Log.d("Placesf", result.errorMessage.toString())
                }
            }
        }
    }

    override fun setText(itemView: View, listdata: placeList) {
        mBinding.companyRecycler.visibility = View.GONE
        mUpdateProfileViewmodel.per_address.value = "${listdata.description}"
        searchboolean = false
        hideKeyboard(requireContext(), itemView)
    }


}