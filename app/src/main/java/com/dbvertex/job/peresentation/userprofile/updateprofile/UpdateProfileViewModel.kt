package com.dbvertex.job.peresentation.userprofile.updateprofile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.repository.UpdateUserRepository
import com.dbvertex.job.network.response.getSingleEquipments
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.peresentation.freelancer_signup.Equipments
import com.dbvertex.job.utils.SharedPrefrenceHelper
import kotlinx.coroutines.launch

class UpdateProfileViewModel : ViewModel() {

    val firstname = MutableLiveData<String>()
    val lastname = MutableLiveData<String>()
    val dateofbirth = MutableLiveData<String>()
    val per_address = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val mobile = MutableLiveData<String>()
    val profile_pic = MutableLiveData<String>()
    val profile_back = MutableLiveData<String>()

    val loading=MutableLiveData<Boolean>(false)

    var Image: Pair<ByteArray, String>? = null
    val pro_type  = MutableLiveData<String>()
    val uploadprofilestate = MutableLiveData<NetworkState>()
    val uploadpersonalprofilestate = MutableLiveData<NetworkState>()


    /**   Freelancer Content  start */

    val uploadprofessionalprofilestate = MutableLiveData<NetworkState>()

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

    val equipmentlist = MutableLiveData<List<getSingleEquipments>>()
    val list = mutableListOf<Equipments>()

    val profiledeleted = MutableLiveData<Boolean>(false)
    val checkiamgetype = MutableLiveData<Boolean>(false)
    val checkiamgetypeback = MutableLiveData<Boolean>(false)

    fun getProfessionalSingleUser(){
        viewModelScope.launch {
            val result = UpdateUserRepository.getSingleProfessionalUser(SharedPrefrenceHelper.user.userid.toString())
            when(result){
                is ResultWrapper.Success ->{

                    aboutme.value = result.response.user_freelancer_aboutme
                    budget.value = result.response.user_freelancer_budget
                    category.value = result.response.user_freelancer_category
                    specialisation.value = result.response.user_freelancer_specialisation
                    experience.value = result.response.user_freelancer_experience
                    passport.value = result.response.passport.toInt()


                    val list = mutableListOf<getSingleEquipments>()
                    list.addAll(result.response.user_freelancer_equipments.map { it })
                    equipmentlist.value = list



                }
                is ResultWrapper.Failure ->{
                   temp_showToast("${result.errorMessage}")
                }
            }

        }

    }




    /**   Freelancer Content  End */

    fun UploadProfileImage(){
        viewModelScope.launch {
            val result = FreelancerProfileRepository.uploadprofile_img(Image, type = pro_type.value.toString())
            when(result){
                is ResultWrapper.Success->{
                    temp_showToast("Profile Uploaded")
                    Log.d("profileuploaded", result.response.toString())
                    uploadprofilestate.value = NetworkState.SUCCESS
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("Profile Uploading failed..")
                    uploadprofilestate.value = NetworkState.FAILED
                }
            }
        }
    }

   fun removeProfileImage(){
       viewModelScope.launch {
           val result =  FreelancerProfileRepository.removeProfileImage(pro_type.value.toString())
           when(result){
               is ResultWrapper.Success ->{
                   profiledeleted.value = true
                   temp_showToast("Profile Deleted..")

               }
               is ResultWrapper.Failure ->{
                   profiledeleted.value = false
                   temp_showToast("Profile Deleting failed..")

               }
           }
       }
   }




    fun getUserPersonaldetail(uid:String){
        loading.value=true
        viewModelScope.launch {
            val result = UpdateUserRepository.getUserPersonaldetail(uid)
            when(result){
                is ResultWrapper.Success ->{
                    loading.value=false
                    Log.d("updatrepersonaldetail", result.response.toString())
                    firstname.value = result.response.first_name
                    lastname.value = result.response.last_name
                    dateofbirth.value = result.response.dob
                    per_address.value = result.response.apilocation
                    address.value = result.response.manuallocation
                    username.value= result.response.username
                    mobile.value=result.response.phone.toString()
                                profile_pic.value=result.response.profile_pic.toString()
                    profile_pic.value=result.response.profile_pic.toString()
                    profile_back.value=result.response.profile_back.toString()



                }
                is ResultWrapper.Failure ->{
                    loading.value=false
                    Log.d("newflow","error")
                    temp_showToast("new${result.errorMessage}")
                }


            }
        }
    }




    fun updatepersonalprofile(){

          uploadpersonalprofilestate.value = NetworkState.LOADING_STARTED
        viewModelScope.launch {
            val result = UpdateUserRepository.Updatepersonaluser(firstname.value.toString(), lastname.value.toString() ,
            dateofbirth.value.toString(),  address.value.toString(), per_address.value.toString(),   username.value.toString() )
            uploadpersonalprofilestate.value = NetworkState.LOADING_STOPPED
            when(result){
                is ResultWrapper.Success ->{
                    uploadpersonalprofilestate.value = NetworkState.SUCCESS

                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                    uploadpersonalprofilestate.value = NetworkState.FAILED
                }
            }
        }
    }
/* http://thephototribe.in/api/webservice/updatefreelanceruser
    POST : user_id, category, specialisation, experience, equipments, passport(Boolean), aboutme*/

    fun UpdatePRofeesionalFreelancerProfile(equipmentslist11 : String){

          uploadprofessionalprofilestate.value = NetworkState.LOADING_STARTED

        viewModelScope.launch {
               val result = UpdateUserRepository.PostFreelancerUpdateUser(category.value.toString(), specialisation.value.toString(), experience.value.toString() , equipmentslist11
               , passport.value!!.toInt(), aboutme.value.toString())
            uploadprofessionalprofilestate.value = NetworkState.LOADING_STOPPED
            when(result){
                is ResultWrapper.Success ->{
                    temp_showToast("Professional Profile Updated")
                    uploadprofessionalprofilestate.value = NetworkState.SUCCESS
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                    uploadprofessionalprofilestate.value = NetworkState.FAILED
                }
            }
        }

    }


}