package com.dbvertex.job.peresentation.inspiration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.InspirationRepository
import com.dbvertex.job.network.response.inspiration.InspirationDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast

import kotlinx.coroutines.launch

class InspirationViewmodel : ViewModel() {

    val inspirationlist = MutableLiveData<List<InspirationDTO>>()

    val bodytext = MutableLiveData<String>()
    val likeunlike = MutableLiveData<Boolean>()
    val favUnfav = MutableLiveData<Boolean>()
    val time = MutableLiveData<String>()
    val minspiration_id = MutableLiveData<String>()


    fun getSingleInspiration(inpiration_id : String) {
        viewModelScope.launch {
            val result = InspirationRepository.getSingleInspiration(inpiration_id)
            when(result){
                is ResultWrapper.Success ->{

                    bodytext.value = "${result.response.body}"
                    likeunlike.value = result.response.user_liked
                    favUnfav.value = result.response.user_saved
                    time.value = "${result.response.created}"
                    minspiration_id.value = "${result.response.id}"

                }
                is ResultWrapper.Failure ->{

                    temp_showToast("${result.errorMessage}")

                }
            }
        }

    }


    fun setlikeUnlike(inspiration_id: String){
        viewModelScope.launch {
            val result = InspirationRepository.setlikeUnlike(inspiration_id)
            when(result){
                is ResultWrapper.Success ->{
                  //  temp_showToast("Liked")
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }

            }
        }
    }
    fun setFavUnFav(inspiration_id: String){
        viewModelScope.launch {
            val result = InspirationRepository.setFavUnFav(inspiration_id)
            when(result){
                is ResultWrapper.Success ->{
                  //  temp_showToast("Fav")
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }

            }
        }
    }




    fun getInspirationsList(){
        viewModelScope.launch {
            val result = InspirationRepository.getInspirationlist()
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<InspirationDTO>()
                    list.addAll(result.response.map {  it })
                    inspirationlist.value = list

                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }
}