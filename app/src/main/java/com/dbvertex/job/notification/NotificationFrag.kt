package com.dbvertex.job.notification

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentNotificationBinding
import com.dbvertex.job.network.repository.NotificationRepository
import com.dbvertex.job.network.response.notification.NotificationDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.peresentation.navigate_to_page.discuss.DiscussViewModel
import kotlinx.coroutines.launch


class NotificationFrag : Fragment(), onNotificationCLick {

    private lateinit var mBinding: FragmentNotificationBinding
    var mlist = ArrayList<NotificationDTO>()
    private val mDiscussViewModel by activityViewModels<DiscussViewModel>()
    val listss = MutableLiveData<List<NotificationDTO>>()

    companion object {
        val datestatic = MutableLiveData<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentNotificationBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@NotificationFrag
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val result = NotificationRepository.getNotificationList()
            when (result) {
                is ResultWrapper.Success -> {

                    val list = mutableListOf<NotificationDTO>()
                    list.addAll(result.response.map { it })
                    listss.value = list
                    mBinding.noNotificationWereFound.visibility = View.GONE


                }
                is ResultWrapper.Failure -> {
                    // temp_showToast("${result.errorMessage}")
                    mBinding.noNotificationWereFound.visibility = View.VISIBLE
                }
            }

        }

        listss.observe(viewLifecycleOwner, Observer {
            val adapter = NotificationAdapter(it as ArrayList<NotificationDTO>, this)
            mBinding.notificationRv.adapter = adapter
        })

        mBinding.notificationRv.setHasFixedSize(true)


    }

    override fun onNotificationDetail(notificationDTO: NotificationDTO) {
        if (notificationDTO.content_type.equals("chat")) {
            val bundle = Bundle()
            bundle.putString("receiver_id", notificationDTO.content_id)
            findNavController().navigate(R.id.chatMessage, bundle)
        } else if (notificationDTO.content_type.equals("discuss")) {
            mDiscussViewModel.getSingleQuestion(notificationDTO.content_id)
            mDiscussViewModel.questionid.value = notificationDTO.content_id
            findNavController().navigate(R.id.discussDetail)
        } else {
            Log.d("no_notification", "fdfdf")
        }
    }
}