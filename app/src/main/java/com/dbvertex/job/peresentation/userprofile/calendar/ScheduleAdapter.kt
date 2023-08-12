package com.dbvertex.job.peresentation.userprofile.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemEventsScheduleBinding
import com.dbvertex.job.network.response.EventDTO

class ScheduleAdapter(val list: List<EventDTO>): RecyclerView.Adapter<ScheduleAdapter.EventViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = ItemEventsScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
              return EventViewHolder(view)

    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class EventViewHolder(private val binding : ItemEventsScheduleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(eventDTO: EventDTO) {
               binding.itemEventDate.text = "From ${eventDTO.from_date} \nTo       ${eventDTO.to_date}"
            binding.itemEventVenue.text = eventDTO.event
            binding.itemEventStatus.text = eventDTO.status
            if (eventDTO.status.equals("Ongoing")){
               binding.itemEventStatus.setTextColor(JobApp.instance.applicationContext?.resources?.getColorStateList(R.color.green))
                binding.eventListContainer.setBackgroundResource(R.drawable.ongoing)
            }else if (eventDTO.status.equals("Upcoming")){
                binding.itemEventStatus.setTextColor(JobApp.instance.applicationContext?.resources?.getColorStateList(R.color.upcoming))
                binding.eventListContainer.setBackgroundResource(R.drawable.upcoming)

            }else{
                binding.itemEventStatus.setTextColor(JobApp.instance.applicationContext?.resources?.getColorStateList(R.color.orange_dark))
                binding.eventListContainer.setBackgroundResource(R.drawable.expired)

            }

        }

    }
}