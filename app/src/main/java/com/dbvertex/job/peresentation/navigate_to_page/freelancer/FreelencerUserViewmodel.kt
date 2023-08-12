package com.dbvertex.job.peresentation.navigate_to_page.freelancer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.FavoritesRepository
import com.dbvertex.job.network.repository.FreeLancerRepository
import com.dbvertex.job.network.response.FreelencerUserlistDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.utils.SharedPrefrenceHelper
import kotlinx.coroutines.launch

class FreelencerUserViewmodel : ViewModel() {

    val userlist = MutableLiveData<List<FreelencerUserlistDTO>>()

     val filterapply = MutableLiveData<Boolean>(false)

    val favuser = MutableLiveData<Boolean>(false)

    val mcategory = MutableLiveData<String>()
    val mspecialisation = MutableLiveData<String>()
    val mequipment = MutableLiveData<String>()
    val mlocation = MutableLiveData<String>()
    val mfromdate = MutableLiveData<String>()
    val mtodate = MutableLiveData<String>()
    val mbudget = MutableLiveData<String>()
    val mreceiverid = MutableLiveData<String>()
    val mProfileimage = MutableLiveData<String>()
    val mUsername = MutableLiveData<String>()

     val sortbybudget = MutableLiveData<String>("asc")
     val sortbyDate = MutableLiveData<String>("asc")
    val sortbyRating = MutableLiveData<String>("asc")
    val sortbynearby = MutableLiveData<String>(" ")
    val fromcategory = MutableLiveData<String>()

    fun getFavFreelancerUser(){
        viewModelScope.launch {
            val result = FavoritesRepository.getFavFreelancer()
            when(result){
                is ResultWrapper.Success ->{

                    val list = mutableListOf<FreelencerUserlistDTO>()

                    result.response.forEachIndexed { index, freelencerUserlistDTO ->
                        if (freelencerUserlistDTO.user_id != SharedPrefrenceHelper.user.userid){
                            list.add(freelencerUserlistDTO)
                        }
                    }
                    userlist.value = list

                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }


    fun  getUserbyList(){
        viewModelScope.launch {
            val result = FreeLancerRepository.getFreelencerFromCategory(fromcategory.value.toString(),
                sortbyDate.value.toString(), sortbybudget.value.toString(),
            sortbyRating.value.toString(), sortbynearby.value.toString())

            when(result){
                is ResultWrapper.Success ->{

                    val list = mutableListOf<FreelencerUserlistDTO>()
                    result.response.forEachIndexed { index, freelencerUserlistDTO ->
                        if (freelencerUserlistDTO.user_id != SharedPrefrenceHelper.user.userid){
                            list.add(freelencerUserlistDTO)
                        }
                    }

                    userlist.value = list
                    Log.d("freelancerlist; ", list.toString())

                }
                is ResultWrapper.Failure ->{
                    Log.d("freelancerlist er; ", result.errorMessage)
                }
            }
        }
    }


    fun getFilterlist(){
        viewModelScope.launch {
            val result = FreeLancerRepository.getFreelencerFilter(
                category = mcategory.value ?: "",
                specialisation = mspecialisation.value ?: "",
                equipments = mequipment.value ?: "",
                location = mlocation.value ?: "",
                todate = mtodate.value ?: "",
                fromdate = mfromdate.value ?: "",
                budget = mbudget.value ?: "")

            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<FreelencerUserlistDTO>()
                    result.response.forEachIndexed { index, freelencerUserlistDTO ->
                        if (freelencerUserlistDTO.user_id != SharedPrefrenceHelper.user.userid){
                            list.add(freelencerUserlistDTO)
                        }
                    }
                    userlist.value = list


                }
                is ResultWrapper.Failure ->{

                }
            }
        }

    }
}