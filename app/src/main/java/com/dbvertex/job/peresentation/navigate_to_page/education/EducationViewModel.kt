package com.dbvertex.job.peresentation.navigate_to_page.education

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.EducationRepository
import com.dbvertex.job.network.response.*
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import kotlinx.coroutines.launch

class EducationViewModel : ViewModel() {

      val highlightlist = MutableLiveData<List<EducationHighlightDTO>>()

      val contentlist = MutableLiveData<List<EducationSeriesContent>>()
    val serieslist = MutableLiveData<List<SingleSerieslist>>()

    val seriesname = MutableLiveData<String>()

    val isVideo = MutableLiveData(false)

    val mvedioID = MutableLiveData<String>()
    val mtitle = MutableLiveData<String>()
    val mdescription = MutableLiveData<String>()
    val msubtitle = MutableLiveData<String>()
    val mfav = MutableLiveData<Boolean>()


    fun getSeries(seriesid: String){
        viewModelScope.launch {
            val result = EducationRepository.getSeries(seriesid)
            when (result){
                is ResultWrapper.Success ->{


                    val list = mutableListOf<SingleSerieslist>()
                    list.addAll(result.response.data.map { it })
                    serieslist.value = list
                    seriesname.value = result.response.series_name

                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }




    fun getSingleContent(seriesid : String){

        viewModelScope.launch {
            val result = EducationRepository.getSingleContent(seriesid)
            when(result){
                is ResultWrapper.Success ->{
                    mvedioID.value = result.response.video_id
                    mtitle.value = result.response.title.trim()
                    mdescription.value = result.response.description.trim()
                    msubtitle.value = result.response.subtitle.trim()
                    mfav.value = result.response.favourite
                    isVideo.value = true

                }
                is ResultWrapper.Failure ->{
                        temp_showToast("${result.errorMessage}")
                }
            }
        }

    }



    fun  getSeriesContent(){

        viewModelScope.launch {
            val result = EducationRepository.getSeriesContent()
            when(result){
                is ResultWrapper.Success ->{

                    val list = mutableListOf<EducationSeriesContent>()
                    list.addAll(result.response.map { it })

                    contentlist.value = list



                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }

    fun getHighlightContent() {

        viewModelScope.launch {
            val result = EducationRepository.getEducationHighlights()

            when(result){
                is ResultWrapper.Success ->{

                    val list = mutableListOf<EducationHighlightDTO>()
                    list.addAll(result.response.map { it })

                    highlightlist.value = list
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")


                }
            }
        }



    }
}

fun SeriesrcontenttoSerieslist(seriesContent: EducationSingleSeriesContent) = SingleSerieslist(
   content_id =   seriesContent.data.map { it.content_id }.toString(),
    series_name =  seriesContent.data.map { it.series_name }.toString(),
    title =  seriesContent.data.map { it.title }.toString(),
  subtitle =   seriesContent.data.map { it.subtitle }.toString(),
   image =    seriesContent.data.map { it.image }.toString(),
   series_id =  seriesContent.data.map { it.series_id }.toString(),
   favourite =  seriesContent.data.map { it.favourite } as Boolean
)