package com.dbvertex.job.peresentation.createuser

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentSignUpBinding
import com.dbvertex.job.network.repository.AutoCompleteRepository
import com.dbvertex.job.network.response.placeList
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.peresentation.auth.AuthViewModel
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.peresentation.company_signup.AutoCompleteAdapter
import com.dbvertex.job.peresentation.company_signup.RVHost
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class SignUp : Fragment(), DatePickerDialog.OnDateSetListener, RVHost {
    private lateinit var mBinding: FragmentSignUpBinding
    private val mAuthViewModel by activityViewModels<AuthViewModel>()
    var type: Int = 0
    val usertype = MutableLiveData<Int>(2)
    var searchboolean: Boolean = true
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var progressBar: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSignUpBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@SignUp
            viewModel = mAuthViewModel


        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = ProgressDialog(requireContext())

        mBinding.back.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        mBinding.mainSignupLayout.setOnClickListener {
            hideKeyboard(requireContext(), mBinding.mainSignupLayout)
        }
        usertype.observe(viewLifecycleOwner) {
            mBinding.usertype = it
            when (it) {
                1 -> {
                    mAuthViewModel.registertype.value = "company"
                }
                2 -> {
                    mAuthViewModel.registertype.value = "freelancer"
                }
                3 -> {
                    mAuthViewModel.registertype.value = "other"
                }
                else -> {
                    Log.d("errpr", it.toString())
                }
            }
        }
        mBinding.comProCompany.setOnClickListener {
            hideKeyboard(requireContext(), it)
            usertype.value = 1
        }
        mBinding.comProFreelancer.setOnClickListener {
            hideKeyboard(requireContext(), it)
            usertype.value = 2
        }
        mBinding.comProOthers.setOnClickListener {
            hideKeyboard(requireContext(), it)
            usertype.value = 3
        }

        mAuthViewModel.RegNetworkState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { state ->
                when (state) {
                    NetworkState.LOADING_STARTED -> {

                        mBinding.createMyAcc.apply {
                            showProgress()
                            isEnabled = false
                        }
                    }
                    NetworkState.LOADING_STOPPED -> {

                        mBinding.createMyAcc.apply {
                            hideProgress("Create My Account")
                            isEnabled = true
                        }
                    }
                    NetworkState.SUCCESS -> {
                        SharedPrefrenceHelper.isSignupComleted = true
                        //here we want to make SharedPrefrenceHelper.isLoggedIn=true
                        //and navigate to job dashboard
                        SharedPrefrenceHelper.isLoggedIn=true
//                        if (mAuthViewModel.registertype.value.equals("company")) {
//                            findNavController().navigate(R.id.companyFragment)
//                        } else

                            if (mAuthViewModel.registertype.value.equals("freelancer")) {
                            //findNavController().navigate(R.id.frrelancerFragment)
                                findNavController().navigate(R.id.jobBoardMain)

                            //here go to jobdahsboard
                        }

                            else {
                                Log.d("newflow","register type is not freelancer")
                            //findNavController().navigate(R.id.otherSignup)
                        }

                    }
                    NetworkState.FAILED -> {
                        mBinding.createMyAcc.apply {
                            hideProgress("Create My Account")
                            isEnabled = true
                        }
                    }
                }
            })


        mBinding.createMyAcc.setOnClickListener {
            validation()
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
            searchboolean = true
        }

        mBinding.comProManualaddress.apply {
            addTextChangedListener {
                if (searchboolean) {
                    getPlaceList(it.toString())
                }
            }

        }




        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val currentToken = it.result
                currentToken?.let { token ->
                    Log.d("token", token)
                    mAuthViewModel.stoken.value = token


                }
            }
        }
    }

    fun validation() {
        val firstname = mAuthViewModel.firstname.value.isNullOrEmpty()
        val lastname = mAuthViewModel.lastname.value.isNullOrEmpty()
        val dob = mAuthViewModel.dateofbirth.value.isNullOrEmpty()

        val username = mAuthViewModel.username.value.isNullOrEmpty()
        val type = mAuthViewModel.registertype.value.isNullOrEmpty()

        if (firstname) {
            mBinding.comProFirstName.setError("Enter first name ")
        } else if (lastname) {
            mBinding.comProLastName.setError("Enter last name")
        } else if (dob) {
            mBinding.comProDob.setError("Enter date")
        } else if (username) {
            mBinding.comProUsername.setError("Enter username")
        } else if (type) {
            mBinding.typeError.visibility = View.VISIBLE
        } else {
            mAuthViewModel.registeruser()
        }

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
        val myDay = dayOfMonth
        val myYear = myear
        val myMonth = mmonth + 1
        mAuthViewModel.dateofbirth.value = "$myDay-$myMonth-$myYear"
        mBinding.comProDob.setText("$myDay-$myMonth-$myYear")
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
                        this@SignUp
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
        //  Toast.makeText(requireContext(), "${listdata.description}", Toast.LENGTH_SHORT).show()
        mBinding.companyRecycler.visibility = View.GONE
        mAuthViewModel.per_address.value = "${listdata.description}"
        searchboolean = false
        hideKeyboard(requireContext(), itemView)
    }

}