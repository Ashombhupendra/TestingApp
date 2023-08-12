package com.dbvertex.job.peresentation.photoshoot.timeline

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPhotoShootAddTimeLineBinding
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class PhotoShootAddTimeLine : Fragment() , DatePickerDialog.OnDateSetListener{
         private lateinit var mBinding : FragmentPhotoShootAddTimeLineBinding
         private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()
            var  time : String = ""

    var day = 0
    var month: Int = 0
    var year: Int = 0

    var hour: Int = 0
    var minute: Int = 0
    var second : Int = 0

    var date : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPhotoShootAddTimeLineBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@PhotoShootAddTimeLine
        }
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.backOfflinePodcast.setOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.savePhotoshootTimeline.setOnClickListener {
            hideKeyboard(JobApp.instance.applicationContext, it)
            if (mBinding.etPsTimelineAdd.text.isNullOrEmpty()){
                mBinding.etPsTimelineAdd.setError("Enter Event Here")
            }else if (time.isNullOrEmpty()){
                Toast.makeText(JobApp.instance.applicationContext, " Select time", Toast.LENGTH_SHORT).show()
            }else{
                savePSTimeLine(mBinding.etPsTimelineAdd.text.toString(), time)
            }
        }

        mBinding.itemPhotoshootPresavedAddNewMsg.setOnClickListener {
            hideKeyboard(JobApp.instance.applicationContext, it)
            datepicker()
        }
    }

    private fun savePSTimeLine(titlew : String , time : String){
        lifecycleScope.launch {
            val result = PhotoShootRepository.SavePhotoShootTimeline(titlew, time, mPhotoshootViewModel.photoshoot_id.value.toString())
           when(result){
               is ResultWrapper.Success ->{
                   temp_showToast("Success Fully Added")
                   findNavController().navigateUp()
               }
               is ResultWrapper.Failure ->{
                   temp_showToast("${result.errorMessage}")
               }
           }
        }
    }
   private fun datepicker(){
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(requireContext(), R.style.MyDatePickerStyle, this ,year , month, day)
        datePickerDialog.show()
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val  myDay = dayOfMonth
        val myYear = year
        val  myMonth = month+1

      //  mBinding.photoshootDateTime.setText("$myDay-$myMonth-$myYear")
        date = "$myYear-$myMonth-$myDay"

        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        getTime()

    }

    private fun getTime(){


        val cal = Calendar.getInstance()
        TimePickerDialog(context,R.style.MyDatePickerStyle, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()


    }
    private val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        val cal =  Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)

         time  = SimpleDateFormat("HH:mm:ss").format(cal.time)
        val sectime  = SimpleDateFormat("HH:mm:ss").format(cal.time)

        if (time.isNullOrEmpty()){
            mBinding.psTimelineTimeTv.setText("Select time")
            Toast.makeText(JobApp.instance.applicationContext, "Please Select Date/Time", Toast.LENGTH_SHORT).show()
        }else{
            mBinding.psTimelineTimeTv.setText("$time")
        }

        // mBinding.photoshootDateTime.append(", $time")

    }

}