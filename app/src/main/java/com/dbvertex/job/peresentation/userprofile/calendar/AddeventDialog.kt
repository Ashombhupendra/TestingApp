package com.dbvertex.job.peresentation.userprofile.calendar

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.dbvertex.job.R
import com.dbvertex.job.databinding.DialogAddEventBinding
import com.dbvertex.job.network.repository.UpdateUserRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kotlinx.coroutines.launch
import java.util.*


class AddeventDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {
         private lateinit var mBinding : DialogAddEventBinding
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var todateboolean : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogAddEventBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@AddeventDialog
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

              bindProgressButton(mBinding.addEventBtn)
        mBinding.addEventCancel.setOnClickListener {
            dismiss()
        }
         mBinding.addEventBtn.setOnClickListener {
             mBinding.addEventBtn.apply {
                 showProgress()
                 isClickable = false
             }
             if (mBinding.addEventCheckbox.isChecked){
                 setEvent(1)

             }else{
                 setEvent(0)
             }
         }
        mBinding.addEventTodateIv.setOnClickListener {
            todateboolean = true
            datepicker()
        }
        mBinding.addEventFromdateIv.setOnClickListener {
             todateboolean = false
            datepicker()
        }
        mBinding.addEventToDate.setOnClickListener {
            todateboolean = true
            datepicker()
        }
        mBinding.addEventFromDate.setOnClickListener {
            todateboolean = false
            datepicker()
        }
    }
    fun  setEvent(reminderboolean : Int){
        val todate = mBinding.addEventToDate.text
        val fromdate = mBinding.addEventFromDate.text
        val event = mBinding.addEventNote.text
           //  showSnackBar(todate.toString())
        if (todate.isNullOrEmpty()){
            mBinding.addEventToDate.setError("Select to date")
        }else if (fromdate.isNullOrEmpty()){
            mBinding.addEventFromDate.setError("Select from date")
        }else if (event.isNullOrEmpty()){
            mBinding.addEventNote.setError("Enter event detail")
        }
        else{



            lifecycleScope.launch {

                val result  = UpdateUserRepository.setEvent( fromdate.toString(), todate.toString(),event.toString(), reminderboolean)
                when(result){
                    is ResultWrapper.Success ->{
                        mBinding.addEventBtn.apply {
                            hideProgress("Add")
                            isClickable = true
                        }
                        if (result.response){
                            UserSchedule.refreshboolean.value = true
                            dismiss()
                        }else{

                        }
                    }
                    is ResultWrapper.Failure ->{
                        temp_showToast(result.errorMessage)
                        mBinding.addEventBtn.apply {
                            hideProgress("Add")
                            isClickable = true
                        }
                    }
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

        if (todateboolean){
            mBinding.addEventToDate.setText("$myDay-$myMonth-$myYear")

            todateboolean = false
        }else{
            mBinding.addEventFromDate.setText("$myDay-$myMonth-$myYear")

        }
      //  mBinding.comProDob.setText("$myDay-$myMonth-$myYear")
    }




}