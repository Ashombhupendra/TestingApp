package com.dbvertex.job.peresentation.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentChatHomeBinding
import com.dbvertex.job.network.response.chat.ChatuserDTO
import com.dbvertex.job.utils.SharedPrefrenceHelper


class ChatHome : Fragment(), onChatUserClick {
    private lateinit var mBinding: FragmentChatHomeBinding
    private lateinit var adapter: ChatUserAdapter
    private val mChatViewmodel by activityViewModels<ChatViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentChatHomeBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@ChatHome
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.jobBoardMain)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)




        mChatViewmodel.no_data.observe(requireActivity()) {
            if (it == true) {
                mBinding.noChatuserFound.visibility = View.VISIBLE
            }
        }

        mBinding.chatSearch.doOnTextChanged { text, start, before, count ->


            if (text?.length != 0) {
                adapter.filter.filter(text)
                adapter.notifyDataSetChanged()
                //  mBinding.homeSearchIcon.setImageResource(R.drawable.ic_baseline_search_24)
            } else {
                adapter.filter.filter(text)
                adapter.notifyDataSetChanged()
                // mBinding.homeSearchIcon.setImageResource(R.drawable.ic_baseline_search_24)
            }
        }
        mBinding.root.setOnClickListener {
            mBinding.chatSearch.clearFocus()

        }

        Log.d("newflow", "userid:" + SharedPrefrenceHelper.user.userid.toString())
        mBinding.pbar.visibility=View.VISIBLE
        mChatViewmodel.getChatUserlist(SharedPrefrenceHelper.user.userid.toString())
        mChatViewmodel.chatuserlist.observe(viewLifecycleOwner, Observer {
            mBinding.pbar.visibility=View.GONE
            mBinding.noChatuserFound.isVisible = it.isNullOrEmpty()
            adapter = ChatUserAdapter(
                JobApp.instance.applicationContext,
                it as ArrayList<ChatuserDTO>,
                this
            )
            mBinding.chatUserRv.adapter = adapter
        })
    }

    override fun onChatDetail(chatuserDTO: ChatuserDTO) {
        Log.d("Pagedetail", "clicked")
        Log.d("Pagedetail", chatuserDTO.receiver_id.toString())
        val bundle = Bundle()
        bundle.putString("receiver_id", chatuserDTO.receiver_id)
        bundle.putString("image",chatuserDTO.profile_pic.toString())

        findNavController().navigate(R.id.chatMessage, bundle)
    }
}