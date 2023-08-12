package com.dbvertex.job.peresentation.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.ChatRepository
import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.response.chat.ChatList
import com.dbvertex.job.network.response.chat.ChatuserDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.utils.SharedPrefrenceHelper
import kotlinx.coroutines.launch

class ChatViewmodel : ViewModel() {

    val chatuserlist = MutableLiveData<List<ChatuserDTO>>()
    val chatlist = MutableLiveData<List<ChatList>>()
    val profile_loding = MutableLiveData<Boolean>(false)
    val sendername = MutableLiveData<String>()
    val senderprofileimg = MutableLiveData<String>()
     val profile_verified = MutableLiveData<Boolean>(false)
//id who will receive
    val msg_sender_id = MutableLiveData<String>()
    val msg_message = MutableLiveData<String>()

    val chatmsgsendstate = MutableLiveData<NetworkState>()

    val multimediastate = MutableLiveData<NetworkState>()

    var video: Pair<ByteArray, String>? = null
    var Image: Pair<ByteArray, String>? = null
    var pdf: ByteArray? = null
    val pdf_name = MutableLiveData<String>()

    var audio : ByteArray? = null
    val no_data=MutableLiveData<Boolean>(false)

    fun getChatHeader(senderid: String){
        viewModelScope.launch {
            val result = FreelancerProfileRepository.getHeader(senderid
            ,"0")
            when(result){
                is ResultWrapper.Success ->{

                    val output: String = result.response.first_name.trim().substring(0, 1).toUpperCase() + result.response.first_name.substring(1)

                    sendername.value = output.toString()
                    senderprofileimg.value = result.response.profile_pic
                    profile_loding.value = true
                    profile_verified.value = result.response.verified

                }
                is ResultWrapper.Failure ->{
                    Log.d("newflow","senderid:${senderid}")
                   // temp_showToast("${result.errorMessage}")
                }
            }
        }
    }


    fun sendMultimedia(){
         viewModelScope.launch {
             multimediastate.value = NetworkState.LOADING_STARTED
             val result = ChatRepository.sendMultimediamsg(senderid = msg_sender_id.value.toString(), images = Image, video = video, postAudio = audio, pdf = pdf,pdf_name = pdf_name.value.toString())
             multimediastate.value = NetworkState.LOADING_STOPPED
             when(result){
                 is ResultWrapper.Success ->{
                             temp_showToast("Succes")
                     Log.d("resultchat", result.response.toString() )
                     multimediastate.value = NetworkState.SUCCESS
                 }
                 is ResultWrapper.Failure ->{
                        temp_showToast("${result.errorMessage}")
                     Log.d("resultchat", result.errorMessage.toString() )

                     multimediastate.value = NetworkState.FAILED
                 }
             }

         }
    }



    fun sendmessage (){
        chatmsgsendstate.value = NetworkState.LOADING_STARTED
        viewModelScope.launch {
            val result = ChatRepository.sendtextmessage(msg_sender_id.value.toString(), msg_message.value.toString(),SharedPrefrenceHelper.user.userid.toString())
            chatmsgsendstate.value = NetworkState.LOADING_STOPPED
            when(result){
                is ResultWrapper.Success ->{
                    chatmsgsendstate.value = NetworkState.SUCCESS

                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                    chatmsgsendstate.value = NetworkState.FAILED
                }
            }
        }
    }


    fun getChatlist(senderid : String,userid: String){
        viewModelScope.launch {
            val result = ChatRepository.getChatList(senderid,userid)
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<ChatList>()
                    list.addAll(result.response.map { it })
                    chatlist.value = list
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }


    fun getChatUserlist(userid:String) {
        viewModelScope.launch {
            val  result = ChatRepository.getChatUserList(userid)

            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<ChatuserDTO>()
                    list.addAll(result.response.map { it })
                    chatuserlist.value = list

                }
                is ResultWrapper.Failure ->{
                    if(result.responsecode!=0){
                        temp_showToast(result.responsecode.toString())
                            if(result.responsecode==404){
                               no_data.value=true;
                            }

                    }
                    temp_showToast(result.errorMessage)
                  //  temp_showToast("${result.errorMessage}")

                }
            }
        }
    }
}