package com.dbvertex.job.peresentation.userprofile.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.MainActivity
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentUserScheduleBinding
import com.dbvertex.job.network.repository.UpdateUserRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.utils.SharedPrefrenceHelper
import kotlinx.coroutines.launch


class UserSchedule : Fragment() {

       private lateinit var mBinding : FragmentUserScheduleBinding
     //  val list = ArrayList<EventDTO>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
     companion object{
         val refreshboolean = MutableLiveData<Boolean>(false)
     }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentUserScheduleBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@UserSchedule

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.backEvent.setOnClickListener {
            findNavController().navigateUp()
        }
        mBinding.createEvent.setOnClickListener {
            findNavController().navigate(R.id.addeventDialog)
        }

           val userid = SharedPrefrenceHelper.user.userid
        if (userid.equals(MainActivity.profileid.value.toString())){
            mBinding.createEvent.visibility = View.VISIBLE
        }else{
            mBinding.createEvent.visibility = View.GONE

        }
        getCalendar()
         refreshboolean.observe(viewLifecycleOwner){
             if (it){
                 getCalendar()
                 refreshboolean.value = false
             }
         }





    }

    fun getCalendar(){
        lifecycleScope.launch {
            val result = UpdateUserRepository.getEvent(MainActivity.profileid.value.toString())
            when(result){
                is ResultWrapper.Success ->{
                    mBinding.noEventFound.visibility  = View.GONE

                    val list  = result.response
                    val adapter = ScheduleAdapter(list)
                    mBinding.eventRecycler.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                is ResultWrapper.Failure ->{
                    mBinding.noEventFound.visibility  = View.VISIBLE
                }
            }
        }

    }




}