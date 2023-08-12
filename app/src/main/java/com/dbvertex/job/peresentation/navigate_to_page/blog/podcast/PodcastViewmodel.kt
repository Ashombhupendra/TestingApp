package com.dbvertex.job.peresentation.navigate_to_page.blog.podcast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dbvertex.job.network.repository.PodcastRepository
import com.dbvertex.job.network.response.PodcastDTO
import com.dbvertex.job.network.utils.ResultWrapper
import kotlinx.coroutines.launch

class PodcastViewmodel : ViewModel() {

     val type = MutableLiveData<Int>(0)
     val favunfavBoolean = MutableLiveData<Boolean>(false)
    val searchpodcastlist = MutableLiveData<List<PodcastDTO>>()
    val searchpodcastActivate = MutableLiveData<Boolean>(false)

    val allpodcastlist : LiveData<PagingData<PodcastDTO>> = when(Podcast.type) {
         0 ->{
             getallpodcast()
         }
        else ->{
            getFavPodcast()
        }
    }




    fun getallpodcast() = PodcastRepository.getAllpostcast().cachedIn(viewModelScope)
    fun getFavPodcast() = PodcastRepository.getFavPodcast().cachedIn(viewModelScope)

    fun searchpodcast(keyword : String){
        viewModelScope.launch {
            val result = PodcastRepository.getSearchPodcast(keyword)
            when(result){
                is ResultWrapper.Success ->{
                     val list = mutableListOf<PodcastDTO>()
                    list.addAll(result.response.map { it })
                    searchpodcastlist.value = list
                    searchpodcastActivate.value = true
                    Log.d("response", result.response.toString())
                }
                is ResultWrapper.Failure ->{
                    Log.d("Error", result.errorMessage)
                }
            }
        }



    }

    fun setFavUnFav(podcast_id : String){
        viewModelScope.launch {
            val result = PodcastRepository.setFavUnFav(podcast_id)
            when(result){
                is ResultWrapper.Success ->{
                    favunfavBoolean.value = result.response!!
                }
                is ResultWrapper.Failure ->{
                    favunfavBoolean.value = false
                }
            }
        }
    }





}