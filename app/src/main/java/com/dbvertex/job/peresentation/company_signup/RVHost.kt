package com.dbvertex.job.peresentation.company_signup

import android.view.View
import com.dbvertex.job.network.response.placeList

interface RVHost {

    fun setText(itemView : View, listdata : placeList)

}