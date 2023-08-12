package com.dbvertex.job.peresentation.navigate_to_page.discuss

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.DiscusRepository
import com.dbvertex.job.network.repository.FavoritesRepository
import com.dbvertex.job.network.response.discuss.AllDiscussionDTO
import com.dbvertex.job.network.response.discuss.DDImage
import com.dbvertex.job.network.response.discuss.DiscusAnswerDTO
import com.dbvertex.job.network.utils.ResultWrapper
import kotlinx.coroutines.launch
enum class NetworkState {
    LOADING_STARTED, LOADING_STOPPED, SUCCESS, FAILED
}
class DiscussViewModel : ViewModel() {

    val homescreenfragposition = MutableLiveData<Int>(0)

    val alldiscussion = MutableLiveData<List<AllDiscussionDTO>>()
    val mquestion = MutableLiveData<String>()
    val mdescription = MutableLiveData<String>()
    val images: MutableList<Pair<ByteArray, String>> = mutableListOf()
    val discussanswerlist = MutableLiveData<List<DiscusAnswerDTO>>()


    val discussFavboolean = MutableLiveData<Boolean>(false)

    /**
     * discussdetail
     */
    val ddName = MutableLiveData<String>()
    val ddCategory = MutableLiveData<String>()
    val ddQuestion = MutableLiveData<String>()
    val ddanswer = MutableLiveData<String>()
    val ddDate = MutableLiveData<String>()
    val ddAnswerCount = MutableLiveData<String>()
    val ddLastseen = MutableLiveData<String>()
    val ddProfileImage = MutableLiveData<String>()
    val ddFav = MutableLiveData<Boolean>(false)
    val ddVerified = MutableLiveData<Boolean>(false)

      val searchdiscussrefresh = MutableLiveData<Boolean>(false)
    val replyanswer = MutableLiveData<String>()
    val addquestionstate = MutableLiveData<NetworkState>()
    val addcommentstate = MutableLiveData<NetworkState>()

    val et_comment = MutableLiveData<String>()

    val ddimages = MutableLiveData<List<DDImage>>()

    val questionid = MutableLiveData<String>()
    var ansImage: MutableList<Pair<ByteArray, String>> = mutableListOf()


    fun getFavDiscuss(){
        viewModelScope.launch {
            val result = FavoritesRepository.getFavDiscuss()
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<AllDiscussionDTO>()
                    list.addAll(result.response.map { it })
                    alldiscussion.value = list
                }
                is ResultWrapper.Failure ->{

                }
            }
        }


    }

    fun setFavUnfavDiscuss(questionid: String){
        viewModelScope.launch {
            val result = DiscusRepository.setDisfavUnfav(questionid)
            when(result){
                is ResultWrapper.Success ->{


                }
                is ResultWrapper.Failure ->{

                }
            }
        }


    }


    fun sendAnswer(answer : String){
        addcommentstate.value = NetworkState.LOADING_STARTED
        viewModelScope.launch {
            val result = DiscusRepository.postanswer(questionid.value.toString(), answer, ansImage)
            addcommentstate.value = NetworkState.LOADING_STOPPED
            when(result){
                 is ResultWrapper.Success ->{
                     addcommentstate.value = NetworkState.SUCCESS
                  //   temp_showToast("${result.response}")

                 }
                is ResultWrapper.Failure ->{
                  ///  temp_showToast("${result.errorMessage}")
                    addcommentstate.value = NetworkState.FAILED
                }

            }
        }
    }

    fun getSingleQuestion(questionid : String){
        viewModelScope.launch {

            val result = DiscusRepository.singlequestion(questionid = questionid)
            when(result){
                is ResultWrapper.Success ->{

                    val question = result.response.questions
                    ddName.value = question.sender_name
                    ddCategory.value = question.sender_category
                    ddQuestion.value = question.question
                    ddanswer.value = question.description
                    ddDate.value = question.created
                    ddAnswerCount.value = "${question.total_answers} Answers"
                    ddLastseen.value = question.last_answered
                    ddProfileImage.value = question.profile_pic
                    ddFav.value =question.favourite
                    ddVerified.value = question.verified


                    val list = mutableListOf<DDImage>()
                    list.addAll(result.response.imags)

                    ddimages.value = list


                    val answerlist = mutableListOf<DiscusAnswerDTO>()
                    answerlist.addAll(result.response.answers)
                    discussanswerlist.value = answerlist

                }
                is ResultWrapper.Failure ->{

                }
            }
        }

    }


    fun postQuestion(){

        viewModelScope.launch {

            addquestionstate.value = NetworkState.LOADING_STARTED
            val result = DiscusRepository.PostQuestion(
                question = mquestion.value.toString(),
                description = mdescription.value.toString(),
                images = images,
                )
            addquestionstate.value = NetworkState.LOADING_STOPPED
            when(result){
                is ResultWrapper.Success ->{

                  //  temp_showToast("Question added successfully")
                    addquestionstate.value = NetworkState.SUCCESS
                }
                is ResultWrapper.Failure ->{
                  //  temp_showToast("${result.errorMessage}")
                    addquestionstate.value = NetworkState.FAILED

                }
            }
        }

    }

   fun getAllDiscussion(type : String){
        viewModelScope.launch {
            val result = DiscusRepository.getAllDiscussion(type)
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<AllDiscussionDTO>()
                    list.addAll(result.response.map { it })
                    alldiscussion.value = list
                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }

    fun  getSearchDiscussion(key : String){
          viewModelScope.launch {
              val result = DiscusRepository.getSearchQuestion(key)
              when(result){
                  is ResultWrapper.Success ->{
                      val list = mutableListOf<AllDiscussionDTO>()
                      list.addAll(result.response.map { it })
                      alldiscussion.value = list
                      searchdiscussrefresh.value = true
                  }
                  is ResultWrapper.Failure ->{

                  }
              }
          }
    }
}