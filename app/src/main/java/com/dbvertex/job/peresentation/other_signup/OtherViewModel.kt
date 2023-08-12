package com.dbvertex.job.peresentation.other_signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.AuthRepository
import com.dbvertex.job.network.repository.UpdateUserRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.utils.SharedPrefrenceHelper
import kotlinx.coroutines.launch

class OtherViewModel : ViewModel() {


    val specialisation = MutableLiveData<String>()
    val instaurl = MutableLiveData<String>()

    val note = MutableLiveData<String>()
    val loader = MutableLiveData<Boolean>(false)


    val otherstate = MutableLiveData<NetworkState>()


    fun getOtherInforemation(){
        viewModelScope.launch {
            val result = UpdateUserRepository.getOtherUserDetail()
            when(result){
                is ResultWrapper.Success ->{

                    Log.d("specilisatinoother1", "${result.response}")
                    instaurl.value = result.response.user_other_sociallink
                   note.value =result.response.user_other_review
                    specialisation.value = result.response.user_other_specialisation

                    loader.value = true

                }
                is ResultWrapper.Failure  ->{
                    temp_showToast("try again")
                }
            }
        }
    }


    fun  regOtheruser(){
        val user = SharedPrefrenceHelper.user
        otherstate.value = NetworkState.LOADING_STARTED
        viewModelScope.launch {
            val result = AuthRepository.regOtheruser(user.userid.toString(), specialisation.value.toString(), instaurl.value.toString(),
            note.value.toString())

            otherstate.value = NetworkState.LOADING_STOPPED
            when(result){
                is ResultWrapper.Success ->{
                    temp_showToast("Login Success")
                    otherstate.value = NetworkState.SUCCESS
                }
                is ResultWrapper.Failure ->{
                    temp_showToast(result.errorMessage)
                    otherstate.value = NetworkState.FAILED
                }
            }
        }
    }


    fun  updateOtheruser(){
        val user = SharedPrefrenceHelper.user
        otherstate.value = NetworkState.LOADING_STARTED
        viewModelScope.launch {

            val result = UpdateUserRepository.updateOtheruser(user.userid.toString(), specialisation.value.toString(), instaurl.value.toString(),
                note.value.toString())

            otherstate.value = NetworkState.LOADING_STOPPED
            when(result){
                is ResultWrapper.Success ->{
                    temp_showToast(result.response.toString())
                    otherstate.value = NetworkState.SUCCESS
                }
                is ResultWrapper.Failure ->{
                    temp_showToast(result.errorMessage)
                    otherstate.value = NetworkState.FAILED
                }
            }
        }
    }
}