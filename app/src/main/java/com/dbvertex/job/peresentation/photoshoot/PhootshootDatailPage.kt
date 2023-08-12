package com.dbvertex.job.peresentation.photoshoot

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPhootshootDatailPageBinding

import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.peresentation.auth.NetworkState
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PhootshootDatailPage : Fragment(), DatePickerDialog.OnDateSetListener{

       private lateinit var mBinding : FragmentPhootshootDatailPageBinding
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()
    val session_ids = mutableListOf<Int>()
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
        mBinding = FragmentPhootshootDatailPageBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@PhootshootDatailPage
            viewmodel = mPhotoshootViewModel
            frag = this@PhootshootDatailPage
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mPhotoshootViewModel.getSessionType()
        Glide.with(view).load(R.raw.progress_gif)
            .into(mBinding.categoryLoading)

        mPhotoshootViewModel.creatingState.observe(viewLifecycleOwner){ state ->
            when(state){
                NetworkState.LOADING_STARTED ->
                {
                     mBinding.categoryLoading.visibility = View.VISIBLE
                }
                NetworkState.LOADING_STOPPED ->
                {
                    mBinding.categoryLoading.visibility = View.GONE
                }
                NetworkState.SUCCESS ->
                {
                    mBinding.categoryLoading.visibility = View.GONE
                } NetworkState.FAILED ->
                {
                   mBinding.categoryLoading.visibility = View.GONE
                }
            }

        }
        mPhotoshootViewModel.sessiontypelist.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                session_ids.clear()
                 it.forEach {
                     session_ids.add(it.id.toInt())
                 }
                sessiontypedropdown(it.map { it.name } as ArrayList<String>)

                mBinding.photoshootDateTime.text = "${mPhotoshootViewModel.photoshoot_time.value}"
            }
        }




    }

    fun sessiontypedropdown(lists: ArrayList<String>){


        mBinding.photoshootSessionType.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.photoshootSessionType)
                return false
            }

        })
        val specialisationSpinner = object : ArrayAdapter<String>(
            requireContext(), R.layout.item_spinner, lists

        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv : TextView = super.getDropDownView(position, convertView, parent) as TextView
                if (position.toLong() == mBinding.photoshootSessionType.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }
                return tv
            }
        }

        mBinding.photoshootSessionType.adapter = specialisationSpinner
      //  Thread.sleep(1000)
        if (!mPhotoshootViewModel.session.value.isNullOrEmpty()){
            mBinding.photoshootSessionType.setSelection(session_ids.indexOf(mPhotoshootViewModel.session.value!!.toInt()))
            mPhotoshootViewModel.category_name.value = specialisationSpinner.getItem(session_ids.indexOf(mPhotoshootViewModel.session.value!!.toInt()))

        }
          /* mPhotoshootViewModel.load_update.observe(viewLifecycleOwner){
            if (it){
                val text = session_ids.indexOf(mPhotoshootViewModel.session.value!!.toInt())
            //    binding.proCity.setSelection(conditionadapter.getPosition(citypos.value.toString()))


             Log.d("category_name", "$text,  ${specialisationSpinner.getItem(text)}")

               mPhotoshootViewModel.load_update.value = false
            }
        }*/
        mBinding.photoshootSessionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = specialisationSpinner.getItem(position)
                val indexs = lists.indexOf(conItem)

                if (indexs >=1) {
                ///    Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                    mPhotoshootViewModel.session.value = session_ids.get(position).toString()
                    mPhotoshootViewModel.categornametemporary.value = conItem
                }
                else{

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

     fun datepicker(){
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

        mBinding.photoshootDateTime.setText("$myDay-$myMonth-$myYear")
        date = "$myYear-$myMonth-$myDay"
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
           // showtimepicker()
        getTime()

      /*  mTimePicker.show()
        mTimePicker.setCancelable(false)*/


    }



    private fun getTime(){


        val cal = Calendar.getInstance()
        TimePickerDialog(context,R.style.MyDatePickerStyle, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()


    }
    private val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
       val cal =  Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)

        val time  = SimpleDateFormat("HH:mm:ss").format(cal.time)
        mBinding.photoshootDateTime.append(", $time")
        mPhotoshootViewModel.photoshoot_time.value = "$date  $time"

    }
   private fun showtimepicker(){
       val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.time_picker_dialog, null)
       //AlertDialogBuilder
       val mBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.dialog_background)
           .setView(mDialogView)
       val mAlertDialog = mBuilder.create()

        mAlertDialog.show()
     val timePicker = mAlertDialog.findViewById<TimePicker>(R.id.timePicker1)
       val btn_ok = mDialogView.findViewById<TextView>(R.id.time_picker_ok)
       val btn = mDialogView.findViewById<TextView>(R.id.time_picker_cancel)
       var time : String = ""
       timePicker?.setOnTimeChangedListener { view, hourOfDay, minute ->
           var hour = hourOfDay
           var am_pm = ""
           // AM_PM decider logic
           when {hour == 0 -> { hour += 12
               am_pm = "AM"
           }
               hour == 12 -> am_pm = "PM"
               hour > 12 -> { hour -= 12
                   am_pm = "PM"
               }
               else -> am_pm = "AM"
           }
           val fhour = if (hour < 10) "0" + hour else hour
           val fmin = if (minute < 10) "0" + minute else minute
           // display format of time
           val msg = "$fhour:$fmin $am_pm"
           time =  formatDateFromDateString("h:mm a","HH:mm:ss", msg)!!


       }
       btn_ok.setOnClickListener {
           mBinding.photoshootDateTime.append(", $time")
           mPhotoshootViewModel.photoshoot_time.value = "$date  $time"
           mAlertDialog.cancel()
       }
       btn.setOnClickListener {
           mAlertDialog.cancel()

       }
   }
    @Throws(ParseException::class)
    fun formatDateFromDateString(
        inputDateFormat: String?, outputDateFormat: String?,
        inputDate: String?
    ): String? {
        val mParsedDate: Date
        val mOutputDateString: String
        val mInputDateFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())
        val mOutputDateFormat = SimpleDateFormat(outputDateFormat, Locale.getDefault())
        mParsedDate = mInputDateFormat.parse(inputDate)
        mOutputDateString = mOutputDateFormat.format(mParsedDate)
        return mOutputDateString
    }
}