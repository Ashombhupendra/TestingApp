package com.dbvertex.job.peresentation.freelancer_signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.AuthRepository
import com.dbvertex.job.network.repository.FreeLancerRepository
import com.dbvertex.job.network.response.EqModelsdto
import com.dbvertex.job.network.response.EquipmentDto
import com.dbvertex.job.network.response.ManufatureDto
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.utils.SharedPrefrenceHelper
import kotlinx.coroutines.launch

class FreelancerViewModel : ViewModel() {


    val category = MutableLiveData<String>()
    val specialisation = MutableLiveData<String>()
    val experience = MutableLiveData<String>()
    val equipments = MutableLiveData<String>()
    val passport = MutableLiveData<Int>()
    val manufature = MutableLiveData<String>()
    val instaurl = MutableLiveData<String>()
    val model = MutableLiveData<String>()
    val budget = MutableLiveData<String>()

    val aboutme = MutableLiveData<String>()

    val finalequipments = MutableLiveData<List<Equipments>>()
    val list = mutableListOf<Equipments>()
    val equipmentList = MutableLiveData<List<EquipmentDto>>()
    val manufactureList = MutableLiveData<List<ManufatureDto>>()
    val EqModelList = MutableLiveData<List<EqModelsdto>>()



    val freelancestate = MutableLiveData<NetworkState>()


    fun getEquipment(){

        viewModelScope.launch {
            val result = FreeLancerRepository.getEquipmentList()

            when(result){
                is ResultWrapper.Success ->{
                    val specilisationlist = mutableListOf<EquipmentDto>()

                    specilisationlist.addAll(result.response.map { it })

                    equipmentList.value = specilisationlist

                }
                is ResultWrapper.Failure ->{

                    Log.d("Equipment", result.errorMessage)

                }
            }
        }

    }



    fun getManufacturers(equipmentID : String){
        viewModelScope.launch {
            val result = FreeLancerRepository.getManufacturedList(equipentID = equipmentID)

            when(result){
                is ResultWrapper.Success ->{
                    val specilisationlist = mutableListOf<ManufatureDto>()

                    specilisationlist.addAll(result.response.map { it })

                    manufactureList.value = specilisationlist

                }
                is ResultWrapper.Failure ->{
                    Log.d("Equipment manu", result.errorMessage)
                }
            }
        }


    }

    fun getModel(Manufacture : String){
        viewModelScope.launch {
            val result = FreeLancerRepository.getModels(manufacturedID = Manufacture)

            when(result){
                is ResultWrapper.Success ->{
                    val specilisationlist = mutableListOf<EqModelsdto>()

                    specilisationlist.addAll(result.response.map { it })

                    EqModelList.value = specilisationlist

                }
                is ResultWrapper.Failure ->{
                    Log.d("Equipment manu", result.errorMessage)
                }
            }
        }


    }


    fun createFreelancer(equipmentslist11 : String){
        val user = SharedPrefrenceHelper.user
        Log.d("aboutme", aboutme.value.toString())
        val equipment = "${equipments.value.toString()}, ${manufature.value.toString()}, ${model.value.toString()}"

                 freelancestate.value = NetworkState.LOADING_STARTED


        viewModelScope.launch {
            val result = AuthRepository.regFreelanceruser(user.userid.toString(), category = category.value.toString(),
            specialisation.value.toString(), experience = experience.value.toString(), equipment = equipmentslist11, passport = passport.value!!.toInt(),
            aboutme = aboutme.value.toString(), budget = budget.value.toString())


            freelancestate.value = NetworkState.LOADING_STOPPED
       when(result){
           is ResultWrapper.Success ->{

               temp_showToast("Login Succcess")
               freelancestate.value = NetworkState.SUCCESS
           }
           is ResultWrapper.Failure ->{
               temp_showToast("Failed : ${result.errorMessage}")
               freelancestate.value = NetworkState.FAILED
           }
       }

        }
    }


}

data class Equipments(
    val equipmentname : String,
    val equipmentcompany : String,
    val equipmentModel : String
)