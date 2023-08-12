package com.dbvertex.job.network.repository

import androidx.paging.PagingSource
import com.dbvertex.job.network.response.PodcastDTO
import com.dbvertex.job.network.services.PodcastServices
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException

class PodcastPaginSource(private val serivces: PodcastServices,private val type: Int) :
   PagingSource<Int, PodcastDTO>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PodcastDTO> {
        val userid = SharedPrefrenceHelper.user.userid
       return  try {

           val result =  when(type ){
               0 ->  serivces.getAllpodcast(userid.toString())
               else -> serivces.getFavPodcast(userid.toString())
           }
           LoadResult.Page(
              data =  result,
              prevKey = null,
               nextKey = null
           )
       }catch (ex: HttpException) {
           if (ex.code() == 404) {
               LoadResult.Page(
                   data = emptyList(),
                   prevKey = null,
                   nextKey = null
               )
           } else {
               LoadResult.Error(ex)
           }
       } catch (ex: IOException) {
           LoadResult.Error(ex)
       }catch (ex : UnknownHostException){
           LoadResult.Error(ex)

       } catch (ex: JsonSyntaxException) {
           LoadResult.Error(ex)
       }catch (ex : SocketTimeoutException){
           LoadResult.Error(ex)
       }catch (ex : UnknownServiceException){
           LoadResult.Error(ex)
       }catch (ex : IOException){
           LoadResult.Error(ex)
       }
    }


}