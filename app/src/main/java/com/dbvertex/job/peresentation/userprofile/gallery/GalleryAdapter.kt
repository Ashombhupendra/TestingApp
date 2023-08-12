package com.dbvertex.job.peresentation.userprofile.gallery

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.bumptech.glide.Glide

class GalleryAdapter(val list : ArrayList<GalleryModel>,
                     context : Context,val
                     onrvItemClick: onRVItemClick? = null):
 RecyclerView.Adapter<GalleryAdapter.myViewHolder>() {

    private val context: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_images, parent, false)
        return myViewHolder(v)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bindItems(list[position],onrvItemClick, position)


    }

    override fun getItemCount(): Int {
        return  list.size
    }




    class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var closerv : ImageView
        fun bindItems(model: GalleryModel, onrvItemClick: onRVItemClick? = null, position: Int) {

             closerv = itemView.findViewById(R.id.item_remove_image)


              closerv.isVisible = onrvItemClick != null

            closerv.setOnClickListener {
                     onrvItemClick!!.onReclyclerItemClicklistner(model,itemView, position )
            }


            val image = itemView.findViewById<ImageView>(R.id.iv_item_gallery)

            image.setOnClickListener {
                onrvItemClick!!.onImageCLick(model)
            }
            Glide.with(itemView.context).load(model.imageView).into(image)

        }

    }



}

interface onRVItemClick {
       fun onReclyclerItemClicklistner(galleryModel: GalleryModel, itemView: View, position: Int)


       fun onImageCLick(galleryModel: GalleryModel)
}

data class GalleryModel(
    val imageid : String,
    val imageView: Uri
)
