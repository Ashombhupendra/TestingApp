package com.dbvertex.job.network.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dbvertex.job.database.PodcastDatabase
import com.dbvertex.job.database.PodcastTableModel
import com.dbvertex.job.network.response.PodcastDTO
import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.PodcastServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object PodcastRepository {

     val podcastdao = PodcastDatabase.getInstance().podcast_Dao()
    val podcastservices = getRetrofitService(PodcastServices::class.java)
    val userid = SharedPrefrenceHelper.user.userid
     fun getAllpostcast(): LiveData<PagingData<PodcastDTO>>{
        val config = PagingConfig(
            pageSize = 100,
            enablePlaceholders = false
        )
        return Pager(
           config = config,
            pagingSourceFactory = {
              PodcastPaginSource(podcastservices, 0)
            }
        ).liveData
    }


    fun getSavepodcast() = podcastdao.getAllPodcast()

    suspend fun deleteSavePodcast(){
        podcastdao.deleteAllPodcast()
    }
    suspend fun savepodcast(podcastDTO: PodcastTableModel){
        podcastdao.insert(podcastDTO)
    }

    suspend fun getSearchPodcast(keyword : String) = safelyCallApi {
           podcastservices.searchPodcast(userid.toString(), keyword)
    }
    fun getFavPodcast() : LiveData<PagingData<PodcastDTO>>{
        val config = PagingConfig(
            pageSize = 100,
            enablePlaceholders = false
        )
        return Pager(
            config = config,
            pagingSourceFactory = {
                PodcastPaginSource(podcastservices, 1)
            }
        ).liveData
    }


    suspend fun setFavUnFav(podcast_id : String) = safelyCallApi {
         podcastservices.getSetFavUNFav(podcast_id, userid.toString())
    }


}