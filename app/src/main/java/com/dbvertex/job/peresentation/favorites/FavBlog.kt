package com.dbvertex.job.peresentation.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentFavBlogBinding
import com.dbvertex.job.network.response.BlogDTO
import com.dbvertex.job.peresentation.navigate_to_page.blog.BlogAdapter
import com.dbvertex.job.peresentation.navigate_to_page.blog.BlogViewModel
import com.dbvertex.job.peresentation.navigate_to_page.blog.onBlogClick
import com.dbvertex.job.peresentation.navigate_to_page.blog.onFavClick


class FavBlog : Fragment(), onBlogClick, onFavClick {
    private val mBlogViewModel by activityViewModels<BlogViewModel>()
    private lateinit var mBinding : FragmentFavBlogBinding
    private val mFavoritesViewmodel by activityViewModels<FavoritesViewmodel>()
    private lateinit var adapter: BlogAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View {
        mBinding = FragmentFavBlogBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@FavBlog

        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFavoritesViewmodel.getFavBlog()
        mFavoritesViewmodel.listBlog.observe(viewLifecycleOwner, Observer {
             adapter = BlogAdapter( it as ArrayList<BlogDTO>, JobApp.instance.applicationContext, this ,this)
            mBinding.blogRv.adapter = adapter
        })

    }

    override fun navigateToblogDetails(itemview: View, blogDTO: BlogDTO) {
      //  Toast.makeText(requireContext(), "Go to Detail Page ${blogDTO.id}", Toast.LENGTH_SHORT).show()
        val bundle = Bundle()
        bundle.putString("blogid", blogDTO.id)
        findNavController().navigate(R.id.blogDetails, bundle)
    }

    override fun clickonFav(itemview: View, blogDTO: BlogDTO) {
        //Toast.makeText(requireContext(), "Go to Fav ${blogDTO.id} ", Toast.LENGTH_SHORT).show()

        mBlogViewModel.setfav(blogid = blogDTO.id)


        if(mBlogViewModel.favboolean.value == true){
            mBlogViewModel.favboolean.value = false

        }else{
            mBlogViewModel.favboolean.value = true

        }

        adapter.notifyDataSetChanged()
    }

}