package com.dbvertex.job.peresentation.favorites

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemFavInspirationSecondaryBinding
import com.dbvertex.job.network.response.inspiration.InspirationImagesDTO
import com.squareup.picasso.Picasso




class InspirationGridAdapter(context: Context, val list : ArrayList<InspirationImagesDTO>,
                             screenWidth: Int):
  RecyclerView.Adapter<InspirationGridAdapter.ImageViewHolder>(){
      private val context = context
    private val screenWidth = screenWidth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemFavInspirationSecondaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {


            holder.bind(list[position], position,screenWidth)



    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ImageViewHolder(val mBinding : ItemFavInspirationSecondaryBinding): RecyclerView.ViewHolder(mBinding.root) {
        fun bind(imagesDTO: InspirationImagesDTO, position: Int, screenWidth: Int) {
            var height: Int;

                if (position % 2 == 0  ) {
                    Log.d("position1", position.toString())
                    height = 445;
                    Picasso.get()
                        .load(imagesDTO.images)
                        .resize( screenWidth/ 2, height)
                        .centerCrop()
                        .into((mBinding.itemFavInspirationSecondary));
                } else {
                    Log.d("position", position.toString())
                    height = 220;
                    Picasso.get()
                        .load(imagesDTO.images)
                        .resize( screenWidth/ 2, height)
                        .centerCrop()
                        .into((mBinding.itemFavInspirationSecondary));
                }


            }


        }


}