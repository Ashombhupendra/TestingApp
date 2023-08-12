package com.dbvertex.job.network.repository.photoshootposes

import android.util.Log
import androidx.paging.PagingSource
import com.dbvertex.job.network.response.photoshoot.getPhotoshootPosesImagesDTO
import com.dbvertex.job.network.services.PhotoShootServices
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException

class PosesPagingSorces(private val services: PhotoShootServices,private val photoshootID : String):
   PagingSource<Int, getPhotoshootPosesImagesDTO>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, getPhotoshootPosesImagesDTO> {
        val position = params.key ?: 0

        return  try {
            val result =  services.getPhotoshootPosesImages(photoshoot_id = photoshootID , position)
            Log.d("poseslist", "hello repo $result")
            LoadResult.Page(
                data =  result,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (result.isEmpty()) null else position + 1
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