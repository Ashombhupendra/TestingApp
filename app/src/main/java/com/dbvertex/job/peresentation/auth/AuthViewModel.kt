package com.dbvertex.job.peresentation.auth

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.JobApp
import com.dbvertex.job.network.repository.AuthRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.peresentation.company_signup.CompanyFragment
import com.dbvertex.job.utils.SharedPrefrenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel : ViewModel() {

    val phoneNum = MutableLiveData<String>()
    val otp = MutableLiveData<String>()
    val verifyotp = MutableLiveData<String>()
    val stoken = MutableLiveData<String>()
    val firstname = MutableLiveData<String>()
    val lastname = MutableLiveData<String>()
    val dateofbirth = MutableLiveData<String>()
    val country = MutableLiveData<String>()
    val userid = MutableLiveData<String>()
    val per_address = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val registertype = MutableLiveData<String>()
    val username = MutableLiveData<String>()

    val status = MutableLiveData<String>()
     val otherconfirmornot = MutableLiveData<Boolean>(false)
    val loginNetworkState = MutableLiveData<NetworkState>()
    val RegNetworkState = MutableLiveData<NetworkState>()

    fun login (){
        loginNetworkState.value = NetworkState.LOADING_STARTED
        viewModelScope.launch {
            val result = AuthRepository.login(phone = phoneNum.value.toString(), device_id = stoken.value.toString())


            loginNetworkState.value = NetworkState.LOADING_STOPPED
            when(result){

                is ResultWrapper.Success ->{
                    temp_showToast(result.response.otp.toString())
                    Log.d("LOGIN" , result.response.toString())
                    //temp_showToast("${result.response.toString()}")
                    if (result.response.status){
                      //   temp_showToast(result.response.otp.toString())
                        verifyotp.value = result.response.otp
                        temp_showToast(result.response.otp.toString())

                        if (result.response.data!!.user_id.isNullOrEmpty()){
                        //********** for new user *************//
                            status.value = "signup"
                        }else {
                            //this is for first reg complete
                            if (result.response.data.profile_status) {
                                //this is for reg complete
                                status.value = "complete"
                                otherconfirmornot.value = result.response.data.active_status

                            } else {
                                //For complete professional profile
                                status.value = result.response.data.user_category
                            }

                        }
                        loginNetworkState.value = NetworkState.SUCCESS
                    }else{

                       loginNetworkState.value = NetworkState.FAILED
                    }
                }
                is ResultWrapper.Failure ->{
                    temp_showToast(result.errorMessage)
                    Log.d("LOGIN" , result.errorMessage)
                    loginNetworkState.value = NetworkState.FAILED
                }
            }

        }
    }
//stoken is firebase token
    fun registeruser(){
        viewModelScope.launch {
            RegNetworkState.value = NetworkState.LOADING_STARTED
            val result = AuthRepository.registeruser(firstname.value.toString(),
            lastname.value.toString(), phoneNum.value.toString(),dateofbirth.value.toString(), address.value.toString(),per_address.value.toString(), "freelancer",username.value.toString(),stoken.value.toString())
            RegNetworkState.value = NetworkState.LOADING_STOPPED
            when(result){
                is ResultWrapper.Success ->{

               //     temp_showToast("${result.response.toString()}")
                    CompanyFragment.userid = result.response.data!!.id.toInt()
                    RegNetworkState.value = NetworkState.SUCCESS


                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage.toString()}")
                    RegNetworkState.value = NetworkState.FAILED

                }
            }
        }
    }


    private suspend fun temp_showToast(text: String) = withContext(Dispatchers.Main) {
        Toast.makeText(
            JobApp.instance,
            text,
            Toast.LENGTH_LONG
        ).show() //todo remove this toast
    }

}

enum class NetworkState {
    LOADING_STARTED, LOADING_STOPPED, SUCCESS, FAILED
}