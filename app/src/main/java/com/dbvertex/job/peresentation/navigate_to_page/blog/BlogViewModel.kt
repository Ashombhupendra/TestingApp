package com.dbvertex.job.peresentation.navigate_to_page.blog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.BlogReepository
import com.dbvertex.job.network.response.BlogBannerDto
import com.dbvertex.job.network.response.BlogDTO

import com.dbvertex.job.network.response.blogdetailImageDTO
import com.dbvertex.job.network.utils.ResultWrapper
import kotlinx.coroutines.launch

class BlogViewModel : ViewModel() {

    val blogbannerlist  = MutableLiveData<List<BlogBannerDto>>()
    val allblogs = MutableLiveData<List<BlogDTO>>()
    val searchallblogs = MutableLiveData<List<BlogDTO>>()
    val searchallblogsactivate = MutableLiveData<Boolean>(false)


    val singleblogItem = MutableLiveData<List<blogdetailImageDTO>>()
    val favboolean = MutableLiveData<Boolean>()

    val title = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val blog_lastpositon = MutableLiveData<Int>(0)


    fun setfav(blogid: String){
        viewModelScope.launch {
            val result = BlogReepository.setfav(blogid)

            when(result){
                is ResultWrapper.Success ->{


                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }

    fun getSingleBlog(blogid: String){
        viewModelScope.launch {
            val result = BlogReepository.getSingleBlog(blogid)

            when(result){
                is ResultWrapper.Success ->{

                    val list = mutableListOf<blogdetailImageDTO>()
                    list.addAll(result.response.image.map { it })

                    singleblogItem.value = list
                    title.value = result.response.title
                    date.value = result.response.created
                    description.value = result.response.description
                    favboolean.value = result.response.favorite

                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }


    fun getAllBlogs(){
        viewModelScope.launch {
            val result = BlogReepository.getAllBlogs()

            when(result) {
                is ResultWrapper.Success ->{
                    val list = mutableListOf<BlogDTO>()
                    list.addAll(result.response.map { it })
                    allblogs.value = list
                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }

    fun getSearchBlog(keywords : String){
        viewModelScope.launch {
            val result = BlogReepository.getSearchBlog(keywords)
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<BlogDTO>()
                    list.addAll(result.response.map { it })
                    searchallblogs.value = list
                    searchallblogsactivate.value = true
                }
                is ResultWrapper.Failure ->{
                    Log.d("error", result.errorMessage)

                }
            }
        }
    }

    fun getBlogbanners(){

        viewModelScope.launch {
            val result = BlogReepository.getBlogBanner()
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<BlogBannerDto>()
                    list.addAll(result.response.map { it })

                    blogbannerlist.value = list
                }

                is ResultWrapper.Failure ->{


                }
            }
        }
    }
}