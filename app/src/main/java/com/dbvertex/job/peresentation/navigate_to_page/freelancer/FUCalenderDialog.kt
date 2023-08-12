package com.dbvertex.job.peresentation.navigate_to_page.freelancer

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentFUCalenderDialogBinding
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.view.OnSwipeTouchListener
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.util.*


class FUCalenderDialog : DialogFragment() {

               private lateinit var mBinding : FragmentFUCalenderDialogBinding
    lateinit var collapsibleCalendar: CollapsibleCalendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFUCalenderDialogBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@FUCalenderDialog

        }
        return mBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var relativeLayout = view.findViewById<ScrollView>(R.id.scrollView)
        var textView = view.findViewById<TextView>(R.id.tv_date)
        collapsibleCalendar = view.findViewById(R.id.collapsibleCalendarView)


        relativeLayout.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object: OnSwipeTouchListener(requireContext()){
            override fun onSwipeRight() {
                collapsibleCalendar.nextDay()
            }

            override fun onSwipeLeft() {
                collapsibleCalendar.prevDay()
            }

            override fun onSwipeTop() {
                if(collapsibleCalendar.expanded){
                    collapsibleCalendar.collapse(400)
                }
            }

            override fun onSwipeBottom() {
                if(!collapsibleCalendar.expanded){
                    collapsibleCalendar.expand(400)
                }
            }
        })
        //To hide or show expand icon
        collapsibleCalendar.setExpandIconVisible(true)
        val today = GregorianCalendar()
        collapsibleCalendar.addEventTag(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        today.add(Calendar.DATE, 1)
        collapsibleCalendar.selectedDay = Day(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        collapsibleCalendar.addEventTag(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), Color.BLUE)
        collapsibleCalendar.params = CollapsibleCalendar.Params(0, 100)
        collapsibleCalendar.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDayChanged() {



            }

            override fun onClickListener() {
                if(collapsibleCalendar.expanded){
                    collapsibleCalendar.collapse(400)
                }
                else{
                    collapsibleCalendar.expand(400)
                }
            }

            override fun onDaySelect() {
                textView.text = collapsibleCalendar.selectedDay?.toString()
            }

            override fun onItemClick(v: View) {

            }

            override fun onDataUpdate() {

            }

            override fun onMonthChange() {

            }

            override fun onWeekChange(position: Int) {

            }
        })


    }

}