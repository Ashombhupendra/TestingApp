package com.dbvertex.job.notification

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemNotificationBinding
import com.dbvertex.job.network.response.notification.NotificationDTO
import com.dbvertex.job.network.utils.convertLongToTime
import com.squareup.picasso.Picasso
import java.util.*


var snd : Boolean = true

class NotificationAdapter(val list: ArrayList<NotificationDTO>, val onNotificationCLick: onNotificationCLick): RecyclerView.Adapter<NotificationAdapter.myNotViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myNotViewholder {
        val v = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return myNotViewholder(v)
    }

    override fun onBindViewHolder(holder: myNotViewholder, position: Int) {
        holder.bind(list[position], onNotificationCLick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class myNotViewholder(val mBinding: ItemNotificationBinding): RecyclerView.ViewHolder(mBinding.root) {

        fun bind(notificationDTO: NotificationDTO, onNotificationCLick: onNotificationCLick) {
              mBinding.itemNotification.text = Html.fromHtml("${notificationDTO.message}")

            mBinding.itemNotification.setOnClickListener {
                onNotificationCLick.onNotificationDetail(notificationDTO)
            }

            val string = convertLongToTime(System.currentTimeMillis())
            Log.d("hello", "$string")

            if (notificationDTO.read_status.equals("1")){
                 mBinding.readUnread.visibility = View.GONE
            }else{
                mBinding.readUnread.visibility = View.VISIBLE
            }
            val c = Calendar.getInstance()
            if (!notificationDTO.user_profile.isNullOrEmpty())
            {
                Picasso.get().load(notificationDTO.user_profile).into(mBinding.notProfile)
            }

            mBinding.itemNotificationDate.text = "${notificationDTO.created}"

           /* val dateatring = formatDateFromDateString(
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd",
                notificationDTO.created
            )
            val dateyesterdayatring = formatDateFromDateString(
                "yyyy-MM-dd HH:mm:ss",
                "dd",
                notificationDTO.created
            )
            Log.d("hello1", "$dateatring")

            val mdate = c.get(Calendar.DAY_OF_MONTH)-1
            Log.d("hello1", "$dateyesterdayatring == $mdate , ${datestatic.value}")*/
           /* if (dateatring.equals(string)){
                if (!datestatic.value.equals(dateatring)){
                    datestatic.value = dateatring.toString()
                    mBinding.itemNotificationDayContainer.visibility  = View.VISIBLE
                    mBinding.itemNotificationDay.setText("Today")

                }else{
                    mBinding.itemNotificationDayContainer.visibility  = View.GONE
                    datestatic.value = dateatring.toString()
                }

            }else if(dateyesterdayatring.equals(mdate.toString()) ){

                if (!datestatic.value.equals(dateatring)){
                    datestatic.value = dateatring.toString()
                    mBinding.itemNotificationDayContainer.visibility  = View.VISIBLE
                    mBinding.itemNotificationDay.setText("Yesterday")

                }else{
                    mBinding.itemNotificationDayContainer.visibility  = View.GONE
                    datestatic.value = dateatring.toString()
                }
            }else if ( datestatic.value != dateatring){
                if (!datestatic.value.equals(dateatring)){
                    datestatic.value = dateatring.toString()
                    mBinding.itemNotificationDayContainer.visibility  = View.VISIBLE
                    mBinding.itemNotificationDay.setText("$dateatring")

                }else{
                    mBinding.itemNotificationDayContainer.visibility  = View.GONE
                    datestatic.value = dateatring.toString()
                }

            }else{
                mBinding.itemNotificationDayContainer.visibility  = View.GONE
                datestatic.value = dateatring.toString()
            }
*/

        }

    }
}

interface onNotificationCLick{
      fun onNotificationDetail(notificationDTO: NotificationDTO)
}