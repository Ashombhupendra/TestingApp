package com.dbvertex.job.peresentation.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.FavoritesRepository
import com.dbvertex.job.network.response.BlogDTO
import com.dbvertex.job.network.response.Serieslist
import com.dbvertex.job.network.response.inspiration.InspirationDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import kotlinx.coroutines.launch

class FavoritesViewmodel : ViewModel(){

    val listBlog = MutableLiveData<List<BlogDTO>>()
    val educationlist = MutableLiveData<List<Serieslist>>()
    val listinspiration = MutableLiveData<List<InspirationDTO>>()


    fun getFavInspiration(){
        viewModelScope.launch {
            val result = FavoritesRepository.getFavInspiration()
            when(result){
                is ResultWrapper.Success ->{

                    val list = mutableListOf<InspirationDTO>()
                    list.addAll(result.response.map { it })
                    listinspiration.value = list
                }

                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }

    fun getFavEducation(){
        viewModelScope.launch {
            val result = FavoritesRepository.getFavEducation()
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<Serieslist>()
                    list.addAll(result.response.map { it })
                    educationlist.value = list
                    Log.d("educationfav", result.response.toString())
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")

                }            }
        }
    }

    fun getFavBlog(){
        viewModelScope.launch {
            val result = FavoritesRepository.getFavBlog()
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<BlogDTO>()
                    list.addAll(result.response.map { it })
                    listBlog.value = list
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }
}