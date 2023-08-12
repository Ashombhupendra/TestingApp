package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.BlogServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object BlogReepository {

    private val blogService = getRetrofitService(BlogServices::class.java)

    val userID = SharedPrefrenceHelper.user.userid

    suspend fun getBlogBanner() = safelyCallApi {
        blogService.getBlogBanner(userID.toString())
    }

    suspend fun getAllBlogs() = safelyCallApi {
        blogService.getAllBlogs(userID.toString())
    }

    suspend fun getSearchBlog(keywords : String) = safelyCallApi {
        blogService.searchblog(userID.toString(), keyword = keywords)
    }


    suspend fun getSingleBlog(blogId : String) = safelyCallApi {
        blogService.getSingleBlog(blogId, userID.toString())
    }


    suspend fun setfav(blogId: String) = safelyCallApi {
        blogService.addtofav(blogId, userID.toString())
    }
}