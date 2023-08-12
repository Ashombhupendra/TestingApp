package com.dbvertex.job.peresentation.photoshoot.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemPsTimelineBinding
import com.dbvertex.job.network.response.photoshoot.PhotoshootTimelineDTO
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PhotoshootTimelineAdapter(val list: List<PhotoshootTimelineDTO>, val onTimelineClick: onTimelineClick):
RecyclerView.Adapter<PhotoshootTimelineAdapter.timelineViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): timelineViewHolder {
        val binding = ItemPsTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return timelineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: timelineViewHolder, position: Int) {
        holder.bind(list[position], onTimelineClick)
    }

    override fun getItemCount(): Int {
       return list.size
    }


    class timelineViewHolder(val binding : ItemPsTimelineBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(timelineDTO: PhotoshootTimelineDTO, onTimelineClick: onTimelineClick) {
              binding.itemPsTimelineTitle.text = timelineDTO.title
            binding.itemPsTimelineSubTitle.text = formatDateFromDateString("yyyy-MM-dd hh:mm:ss","h:mm a", timelineDTO.time )
            binding.itemPsTimelineSwitch.isChecked = timelineDTO.status

            binding.itemPsTimelineSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (timelineDTO.status){
                    timelineDTO.status = false
                }else{
                    timelineDTO.status = true
                }
                onTimelineClick.onItemClick(timelineDTO)
            }


        }

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

interface  onTimelineClick{
    fun onItemClick(photoshootTimelineDTO: PhotoshootTimelineDTO)
}