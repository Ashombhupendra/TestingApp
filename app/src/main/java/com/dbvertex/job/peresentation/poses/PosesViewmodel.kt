package com.dbvertex.job.peresentation.poses

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.PosesRepository
import com.dbvertex.job.network.response.poses.CategoryDTO
import com.dbvertex.job.network.response.poses.PosesImagesDTO
import com.dbvertex.job.network.response.poses.SubcategoryDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.auth.NetworkState
import kotlinx.coroutines.launch

class PosesViewmodel : ViewModel() {

    val subcategorylist = MutableLiveData<List<SubcategoryDTO>>()
    val posesImagesList = MutableLiveData<List<PosesImagesDTO>>()
    val PosescategoryList = MutableLiveData<List<CategoryDTO>>()

       val categoryloading = MutableLiveData<NetworkState>()
    val subcategoryloading  = MutableLiveData<NetworkState>()
    val ImagesLoading  = MutableLiveData<NetworkState>()


    fun getPosesCategory(){
        categoryloading.value = NetworkState.LOADING_STARTED
        viewModelScope.launch {
            val result = PosesRepository.getPosesCatetgory()
            when(result){
                is ResultWrapper.Success ->{
                    categoryloading.value = NetworkState.LOADING_STOPPED
                    val list = mutableListOf<CategoryDTO>()
                    list.addAll(result.response.map { it })
                    PosescategoryList.value = list
                }
                is ResultWrapper.Failure ->{
                    categoryloading.value = NetworkState.LOADING_STOPPED

                    Log.d("error", result.errorMessage)
                }
            }
        }
    }


    fun getSubcategory(category : String){
        viewModelScope.launch {
            val result = PosesRepository.getSubcategory(category)
            when(result){
                is ResultWrapper.Success ->{

                    val list  = mutableListOf<SubcategoryDTO>()
                    list.addAll(result.response.map { it })
                    subcategorylist.value = list
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }

    fun getPosesImages(subcategry_id : String){
        ImagesLoading.value = NetworkState.LOADING_STARTED
        viewModelScope.launch {
            val result = PosesRepository.getPosesImages(subcategry_id)
            when(result){
                is ResultWrapper.Success ->{
                     val list = mutableListOf<PosesImagesDTO>()
                    list.addAll(result.response.map { it })
                    posesImagesList.value = list
                    ImagesLoading.value = NetworkState.LOADING_STOPPED

                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                    ImagesLoading.value = NetworkState.LOADING_STOPPED

                }
            }
        }
    }

    fun setLikeUnlike(imageID : String){
        viewModelScope.launch {
            val result = PosesRepository.SetLikeUnlike(imageID)
            when(result){
                is ResultWrapper.Success ->{
                    Log.d("error", result.response.toString())
                }
                is ResultWrapper.Failure ->{
                    Log.d("error", result.errorMessage)
                }
            }
        }
    }
}