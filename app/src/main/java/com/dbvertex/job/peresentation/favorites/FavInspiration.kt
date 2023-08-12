package com.dbvertex.job.peresentation.favorites

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.network.response.inspiration.InspirationDTO
import com.dbvertex.job.peresentation.inspiration.onInpirationalCLick


class FavInspiration : Fragment(), onInpirationalCLick {
    private var screenWidth = 0
    private val mFavoritesViewmodel by activityViewModels<FavoritesViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_inspiration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val wm = requireActivity()!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        val recycler = view.findViewById<RecyclerView>(R.id.fav_inspiration_rv)
        mFavoritesViewmodel.getFavInspiration()
        mFavoritesViewmodel.listinspiration.observe(viewLifecycleOwner, Observer {
            val adapter = FavInspirationAdapter(JobApp.instance.applicationContext,
                it as ArrayList<InspirationDTO>,
                screenWidth,
                this)
            recycler.adapter = adapter
        })

    }

    override fun onInspirationlikeClick(inspirationDTO: InspirationDTO) {}

    override fun onInspirationCommentClick(inspirationDTO: InspirationDTO) {}

    override fun onInspirationFavClick(inspirationDTO: InspirationDTO) { }

    override fun onInspirationDetail(inspirationDTO: InspirationDTO, positon : Int) {}


}