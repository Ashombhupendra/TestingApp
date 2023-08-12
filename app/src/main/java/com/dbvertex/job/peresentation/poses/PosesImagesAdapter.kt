package com.dbvertex.job.peresentation.poses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.network.response.poses.PosesImagesDTO
import com.dbvertex.job.network.utils.setButtonAnimation
import com.bumptech.glide.Glide

class PosesImagesAdapter(
   val  context: Context,
    val list: ArrayList<PosesImagesDTO>,
    val onPosesImagesClick: onPosesImagesClick,
    val screenWidth: Int
):
        RecyclerView.Adapter<PosesImagesAdapter.posesviewholder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): posesviewholder {
        if (screenWidth== 0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poses_images, parent, false)
            return posesviewholder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poses_images_detail, parent, false)
            return posesviewholder(view)
        }

    }

    override fun onBindViewHolder(holder: posesviewholder, position: Int) {
        holder.bind(list[position], onPosesImagesClick, position)
    }

    override fun getItemCount(): Int {
       return list.size
    }


    class posesviewholder(itemview : View): RecyclerView.ViewHolder(itemview) {
        val layoutManager = MutableLiveData<Boolean>(false)
        fun bind(
            imagesDTO: PosesImagesDTO,
            onPosesImagesClick: onPosesImagesClick,
            posistion: Int
        ) {
               val imageview = itemView.findViewById<ImageView>(R.id.item_poses_image_pic)
            val like = itemView.findViewById<ImageView>(R.id.item_poses_iamge_Fav)
            val unlike = itemView.findViewById<ImageView>(R.id.item_poses_images_unfav)
            val add = itemView.findViewById<ImageView>(R.id.item_poses_images_add)

            add.setOnClickListener {
                onPosesImagesClick.onPosesAdd(imagesDTO)
            }

            Glide.with(itemView.context)
                .load(imagesDTO.image)
                .placeholder(R.drawable.place_holder_img)
                .into(imageview)


            imageview.setOnClickListener {
                onPosesImagesClick.onImageClick(imagesDTO, posistion)

            }

            if (imagesDTO.liked){
                unlike.visibility = View.INVISIBLE
                like.visibility = View.VISIBLE
            }else{
                unlike.visibility = View.VISIBLE
                like.visibility = View.INVISIBLE
            }

            unlike.setOnClickListener {
                onPosesImagesClick.onImageLike(imagesDTO)
                unlike.visibility = View.INVISIBLE
                like.visibility = View.VISIBLE
                setButtonAnimation(like)
            }
            like.setOnClickListener {
                onPosesImagesClick.onImageLike(imagesDTO)
                like.visibility = View.INVISIBLE
                unlike.visibility = View.VISIBLE
               setButtonAnimation(unlike)
            }
        }

    }
}

interface onPosesImagesClick{
     fun onImageLike(imagesDTO: PosesImagesDTO)

     fun onImageClick(imagesDTO: PosesImagesDTO, manage : Int)

     fun onPosesAdd(imagesDTO: PosesImagesDTO)
}