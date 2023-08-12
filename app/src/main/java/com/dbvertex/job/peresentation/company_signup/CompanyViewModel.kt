package com.dbvertex.job.peresentation.company_signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.data.SpecialisationItem
import com.dbvertex.job.network.repository.AuthRepository
import com.dbvertex.job.network.repository.FreeLancerRepository
import com.dbvertex.job.network.repository.UpdateUserRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.toSpecialisationItem
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.utils.SharedPrefrenceHelper
import kotlinx.coroutines.launch

class CompanyViewModel : ViewModel() {

    val loadcomplete = MutableLiveData<Boolean>(false)
    val Companyname = MutableLiveData<String>()
    val Companyaddress = MutableLiveData<String>()
    val CompanyExperience = MutableLiveData<String>()
    val CompanySpecification = MutableLiveData<String>()
    val CompanyUrl = MutableLiveData<String>()
    val takeInternationalAssignment = MutableLiveData<Int>(0)

    val AboutYourCompany = MutableLiveData<String>()

    val specialisation = MutableLiveData<List<SpecialisationItem>>()
   val companystate = MutableLiveData<NetworkState>()


    fun regCompanyUser(){
        val user = SharedPrefrenceHelper.user
        viewModelScope.launch {
            companystate.value = NetworkState.LOADING_STARTED
            val result = AuthRepository.regCompanyUser(user.userid.toString(), Companyname.value.toString(), Companyaddress.value.toString(),
                CompanySpecification.value.toString(),
                      CompanyExperience.value.toString(), CompanyUrl.value.toString(),takeInternationalAssignment.value!!.toInt(),
            AboutYourCompany.value.toString())
            companystate.value = NetworkState.LOADING_STOPPED
            when(result){
                is ResultWrapper.Success ->{

                    Log.d("Result", result.response.toString())
                    companystate.value = NetworkState.SUCCESS

                }
                is ResultWrapper.Failure ->{
                    Log.d("Resultf", result.errorMessage )
                    companystate.value = NetworkState.FAILED
                }
            }
        }
    }

//specialization
    fun getSpecialisation(){

        viewModelScope.launch {
            val result = FreeLancerRepository.getSpecialisationList()
            when(result){
                is ResultWrapper.Success ->{
                    val specilisationlist = mutableListOf<SpecialisationItem>()
                  specilisationlist.add(SpecialisationItem("Select your specialisation"))
                    specilisationlist.addAll(result.response.map { toSpecialisationItem(it) })

                     specialisation.value = specilisationlist

                    Log.d("Specialisation", result.response.toString())


                }
                is ResultWrapper.Failure ->{
                    Log.d("Specialisation", result.errorMessage.toString())
                }
            }
        }
    }


    fun getCompanyUser(user_id : String){
        viewModelScope.launch {
            val result = AuthRepository.getUserCompany(user_id)
            when(result){
                is ResultWrapper.Success ->{
                    Companyname.value = result.response.user_company_name
                    Companyaddress.value = result.response.user_company_address
                    AboutYourCompany.value = result.response.user_company_about
                    CompanyExperience.value = result.response.user_company_experience
                    CompanyUrl.value = result.response.user_company_socialid
                    takeInternationalAssignment.value = result.response.user_company_assignment.toInt()
                    CompanySpecification.value = result.response.user_company_specialisations
                  loadcomplete.value = true

                }
                is ResultWrapper.Failure ->{
                    Log.d("error", result.errorMessage)
                }
            }
        }
    }
    fun updateCompanyUser(){
        val user = SharedPrefrenceHelper.user
        viewModelScope.launch {
            companystate.value = NetworkState.LOADING_STARTED
            val result = UpdateUserRepository.updateCompanyUser(user.userid.toString(), Companyname.value.toString(), Companyaddress.value.toString(),
                CompanySpecification.value.toString(),
                CompanyExperience.value.toString(), CompanyUrl.value.toString(),takeInternationalAssignment.value!!.toInt(),
                AboutYourCompany.value.toString())
            companystate.value = NetworkState.LOADING_STOPPED
            when(result){
                is ResultWrapper.Success ->{

                    Log.d("Result", result.response.toString())
                    companystate.value = NetworkState.SUCCESS

                }
                is ResultWrapper.Failure ->{
                    Log.d("Resultf", result.errorMessage )
                    companystate.value = NetworkState.FAILED
                }
            }
        }
    }


}