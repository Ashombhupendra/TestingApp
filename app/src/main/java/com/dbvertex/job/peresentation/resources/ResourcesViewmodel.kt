package com.dbvertex.job.peresentation.resources

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.ResourcesRepository
import com.dbvertex.job.network.response.resources.ResourcesDTO
import com.dbvertex.job.network.response.resources.ResourcesImageList
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import kotlinx.coroutines.launch

class ResourcesViewmodel : ViewModel() {

    val resourceslist = MutableLiveData<List<ResourcesDTO>>()

    val rdtitle = MutableLiveData<String>()
    val rddescrition = MutableLiveData<String>()
    val rdresourcesname= MutableLiveData<String>()

    val imagelist = MutableLiveData<List<ResourcesImageList>>()



    fun getResourcesdetail(resource_id  : String){
        viewModelScope.launch {
            val result = ResourcesRepository.getResourcesDetail(resource_id)
            when(result){
                is ResultWrapper.Success ->{

                    val res = result.response
                    rdresourcesname.value = res.name
                    rdtitle.value = res.title
                    rddescrition.value = res.description
                   val list = mutableListOf<ResourcesImageList>()
                    list.addAll(res.images.map { it })
                    imagelist.value = list
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }

    fun getresourcesList() {

        viewModelScope.launch {
            val result = ResourcesRepository.getResourcesList()

            when(result){
                is ResultWrapper.Success ->{

                    val list = mutableListOf<ResourcesDTO>()
                    list.addAll(result.response.map { it })

                    resourceslist.value = list

                }
                is ResultWrapper.Failure ->{
                    //  temp_showToast("${result.errorMessage}")
                }
            }
        }


    }
}