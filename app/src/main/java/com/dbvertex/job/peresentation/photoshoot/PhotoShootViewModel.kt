package com.dbvertex.job.peresentation.photoshoot

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.response.photoshoot.ContractDTO
import com.dbvertex.job.network.response.photoshoot.PresavedDTO
import com.dbvertex.job.network.response.photoshoot.Questionnaire.QuestionnaireDTO
import com.dbvertex.job.network.response.photoshoot.SessionTypeDTO
import com.dbvertex.job.network.response.photoshoot.Upcoming_complete_Photoshoot_DTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.auth.NetworkState
import kotlinx.coroutines.launch

class PhotoShootViewModel : ViewModel() {
    //    POST : title, session, photoshoot_time, address, note, client_name, my_goal

    val sessiontypelist = MutableLiveData<List<SessionTypeDTO>>()
    val upcoming_photoshoot = MutableLiveData<List<Upcoming_complete_Photoshoot_DTO>>()
    val complete_photoshoot = MutableLiveData<List<Upcoming_complete_Photoshoot_DTO>>()
    val pre_saved_msg_list = MutableLiveData<List<PresavedDTO>>()


    val presavedTitle = MutableLiveData<String>()
    val presavedDescription = MutableLiveData<String>()
    val presavedCategoryID = MutableLiveData<String>()
    val presavedLoading = MutableLiveData<Boolean>(false)


    //contract
    val contractdes =MutableLiveData<String>()


    val category_name = MutableLiveData<String>()
    val photoshootQuestinnaire = MutableLiveData<List<QuestionnaireDTO>>()

    val photoshoot_id = MutableLiveData<String>()
    val load_update = MutableLiveData<Boolean>(false)

    val title = MutableLiveData<String>()
    val session = MutableLiveData<String>()
    val photoshoot_time = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val note = MutableLiveData<String>()
    val client_name = MutableLiveData<String>()
    val my_goal = MutableLiveData<String>()

    val categornametemporary = MutableLiveData<String>()


    val creatingState = MutableLiveData<NetworkState>()

    var PosesImage: Pair<ByteArray, String>? = null


     val posesadapterrefresh = MutableLiveData<Boolean>(false)
    val from_poses = MutableLiveData<Boolean>(false)


    val contract_id = MutableLiveData<String>()


    fun  postPosesImage(){
        viewModelScope.launch {
            val  result = PhotoShootRepository.postposesImage(
                photoshoot_id = photoshoot_id.value.toString(),
              PosesImage!!)

            when(result){
                is ResultWrapper.Success ->{
                  //  getPhotoShootPosesImages()
                   // Thread.sleep(1000)
                    posesadapterrefresh.value = true
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }

    fun getContractList() : MutableLiveData<List<ContractDTO>>{
        val list = MutableLiveData<List<ContractDTO>>()
        val photoshootid = if(photoshoot_id.value.isNullOrEmpty()) "0" else photoshoot_id.value.toString()
        viewModelScope.launch {
            val result = PhotoShootRepository.getContractList(photoshootid)
            when(result){
                is ResultWrapper.Success ->{
                   val  lissss = mutableListOf<ContractDTO>()
                    lissss.addAll(result.response.map { it })

                    list.value = lissss

                }
                is ResultWrapper.Failure ->{
                     Log.d("Contract", result.errorMessage)
                }
            }
        }
        return list
    }

    fun  getContract(contractID : String){
        viewModelScope.launch {
            val result  = PhotoShootRepository.getContract(contractID)
            when(result){
                is ResultWrapper.Success ->{
                    contractdes.value = result.response.toString()
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }



    fun getPhotoShootPosesImages()=

         PhotoShootRepository.getAllPhotoShootPoses(photoshoot_id.value.toString()).cachedIn(viewModelScope)


    fun savePresavedMessage(){
        presavedLoading.value = false
        viewModelScope.launch {
            val result = PhotoShootRepository.savePreSavedMsg(presavedCategoryID.value.toString(),
            photoshoot_id = photoshoot_id.value.toString(), presavedTitle.value.toString(), presavedDescription.value.toString())
            when(result){
                is ResultWrapper.Success ->{
                    presavedLoading.value = true

                }
                is ResultWrapper.Failure ->{
                    presavedLoading.value = true

                }
            }
        }

    }


    fun getSessionType(){
        viewModelScope.launch {
            val result = PhotoShootRepository.getSessionType()
            when(result){
                is ResultWrapper.Success ->{
                    val list  = mutableListOf<SessionTypeDTO>()
                    list.addAll(result.response.map { it })
                    list.add(0, SessionTypeDTO(
                        "-1", "Select Session Type", "12:12:21"
                    )
                    )
                    sessiontypelist.value = list
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }

    fun createPhotoShoot(){
        creatingState.value = NetworkState.LOADING_STARTED
        viewModelScope.launch {

            val result = PhotoShootRepository.createPhotoShoot(
                photoshoot_id= photoshoot_id.value ?: "",
                title = title.value ?: "",
                session = session.value ?: "",
                photoshoot_time = photoshoot_time.value ?: "",
                address = address.value ?: "",
                note = note.value ?: "",
                client_name = client_name.value ?: "",
                my_goal = my_goal.value ?: ""
            )
            creatingState.value = NetworkState.LOADING_STOPPED

            when(result){
                is ResultWrapper.Success ->{


                    if (photoshoot_id.value == result.response.toString()){
                        temp_showToast("Photoshoot Updated ")
                    }else{
                        temp_showToast("Photoshoot Saved")
                        photoshoot_id.value = result.response.toString()
                        category_name.value = categornametemporary.value.toString()
                    }
                    creatingState.value = NetworkState.SUCCESS



                }
                is ResultWrapper.Failure ->{
                    creatingState.value = NetworkState.FAILED
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }

    fun getUpcomingPhotoshoot(){
        viewModelScope.launch {
            val result = PhotoShootRepository.getUpcomingPhotoshoot()
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<Upcoming_complete_Photoshoot_DTO>()
                    list.addAll(result.response.map { it })
                    upcoming_photoshoot.value = list
                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }

    fun getCompletePhotoshoot(){
        viewModelScope.launch {
            val result = PhotoShootRepository.getCompletePhotoshoot()
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<Upcoming_complete_Photoshoot_DTO>()
                    list.addAll(result.response.map { it })
                    complete_photoshoot.value = list
                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }

    fun getSinglePhotoShoot(){

        viewModelScope.launch {
            val result = PhotoShootRepository.getSinglePhotoshoot(photoshoot_id.value.toString())
            when(result){
                is ResultWrapper.Success ->{
                     title.value = result.response.title
                     session.value = result.response.session_id
                     address.value = result.response.address
                    client_name.value = result.response.client_name
                    photoshoot_time.value = result.response.photoshoot_time
                    note.value = result.response.note
                    my_goal.value = result.response.my_goal
                    load_update.value = true

                }
                is ResultWrapper.Failure ->{
                     temp_showToast("${result.errorMessage}")
                }
            }
        }
    }

      fun getPhotoshootQuestionnaire(){
          val photo_id = if (photoshoot_id.value.isNullOrEmpty()) "0" else photoshoot_id.value.toString()
          val session_id = if (session.value.isNullOrEmpty()) "1" else session.value.toString()
         viewModelScope.launch {
             val result = PhotoShootRepository.getPhotoshootQuestionnaire(photo_id, session_id)
             when(result){
                 is ResultWrapper.Success ->{
                     val list = mutableListOf<QuestionnaireDTO>()
                     list.addAll(result.response.map { it })
                     photoshootQuestinnaire.value = list
                 }
                 is ResultWrapper.Failure ->{

                 }
             }
         }
     }

    fun  getPresavedMessage(){
        viewModelScope.launch {
            val id = if (photoshoot_id.value.isNullOrEmpty()) "0" else photoshoot_id.value.toString()
            val result = PhotoShootRepository.getPresavedMsg(id)
            when(result){
                is ResultWrapper.Success ->{
                    val list  = mutableListOf<PresavedDTO>()
                    list.addAll(result.response.map { it })
                    pre_saved_msg_list.value = list
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }


}