package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.DiscusServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object DiscusRepository {

    val discusServices = getRetrofitService(DiscusServices::class.java)

     val user = SharedPrefrenceHelper.user.userid




    suspend fun singlequestion(questionid : String) = safelyCallApi {
        discusServices.getSingleQuestion(user_id = user.toString(),questionid =  questionid)
    }

    suspend fun getAllDiscussion(type : String) = safelyCallApi {
        discusServices.getQuestions(user_id = user.toString(),type)
    }


    suspend fun  getSearchQuestion(keyword : String) = safelyCallApi {
        discusServices.getSearchQuestion(user_id = user.toString(), keyword = keyword)
    }

    suspend fun  setDisfavUnfav(questionid : String) = safelyCallApi {
        discusServices.setDisfavUnfav(user_id = user.toString(), questionid = questionid)
    }

  /*  @Field("question_id") questionid: String,
    @Field("sender_id") sender_id: String,
    @Field("answer")  answer : String*/
  suspend fun postanswer(questionid: String, answer : String, images: List<Pair<ByteArray, String>>) =
      safelyCallApi {
          val quesID = MultipartBody.Part.createFormData("question_id", questionid)
          val senderID = MultipartBody.Part.createFormData("sender_id", user.toString())
          val answer = MultipartBody.Part.createFormData("answer", answer)
          val imageMultipartList = images?.map {
              val imageRequestBody = it.first.toRequestBody("image/*".toMediaType())
              MultipartBody.Part.createFormData("images[]", it.second, imageRequestBody)
          }
          discusServices.sendDiscussAnswer(quesID,senderID, answer, imageMultipartList)

      }
    suspend fun PostQuestion(
        question : String,
        description : String,
        images: List<Pair<ByteArray, String>>
    ) = safelyCallApi {
            val mquestion = MultipartBody.Part.createFormData("question", question)
            val mdescription= MultipartBody.Part.createFormData("description", description)
            val userid = MultipartBody.Part.createFormData("sender_id", user.toString())
            val imageMultipartList = images.map {
            val imageRequestBody = it.first.toRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("images[]", it.second, imageRequestBody)
        }
        discusServices.postDiscuss(
              mquestion, mdescription, userid, imageMultipartList )
    }
}