package com.dbvertex.job.peresentation.navigate_to_page.blog.podcast

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class OnCompleteDownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

       // Toast.makeText(context, "Donwloading Complete", Toast.LENGTH_SHORT).show()
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE){
            Podcast.oncompletedonwload.value = true
            Toast.makeText(context, "Donwloading Complete", Toast.LENGTH_SHORT).show()
        }
    }
}