package com.dbvertex.job.peresentation.navigate_to_page.discuss

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentSearchDiscussBinding
import com.dbvertex.job.network.response.BlogDTO
import com.dbvertex.job.network.response.discuss.AllDiscussionDTO
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.peresentation.navigate_to_page.blog.BlogAdapter
import com.dbvertex.job.peresentation.navigate_to_page.blog.BlogViewModel
import com.dbvertex.job.peresentation.navigate_to_page.blog.onBlogClick
import com.dbvertex.job.peresentation.navigate_to_page.blog.onFavClick


class SearchDiscuss : Fragment(), onDiscussClick,  onBlogClick, onFavClick {

     private lateinit var  mBinding : FragmentSearchDiscussBinding
    private val mDiscussViewModel by activityViewModels<DiscussViewModel>()
    private val mBlogViewModel by activityViewModels<BlogViewModel>()

    val refresh  : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentSearchDiscussBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@SearchDiscuss

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


         mBinding.discussBack.setOnClickListener {
             findNavController().navigateUp()

         }

        val bundle = arguments
        if (bundle !=null) {
             val type = bundle.getString("type")
              if (type.equals("blog")){
                  mBinding.discusEtSearch.setHint("Search Blog")
              }
            mBinding.discusEtSearch.setOnEditorActionListener { textview, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(requireContext(), mBinding.discusEtSearch)
                    if (type.equals("discuss")){
                        mDiscussViewModel.getSearchDiscussion(textview.text.toString())

                    }else{
                        mBlogViewModel.getSearchBlog(textview.text.toString())
                    }
                    //Toast.makeText(requireContext(), "Search : ${textview.text}", Toast.LENGTH_SHORT).show()
                    true
                } else {
                    false
                }
            }


            mDiscussViewModel.searchdiscussrefresh.observe(viewLifecycleOwner) {
                if (it) {

                    mDiscussViewModel.alldiscussion.observe(viewLifecycleOwner) {
                        if (it.isNullOrEmpty()) {
                            mBinding.noDiscussFound.visibility = View.VISIBLE

                        } else {
                            mBinding.noDiscussFound.visibility = View.GONE
                        }
                        val adapter = AllDiscussionAdapter(
                            JobApp.instance.applicationContext,
                            it,
                            this
                        )
                        mBinding.discussSearchRecycler.adapter = adapter
                        mBinding.discussSearchRecycler.setHasFixedSize(true)

                    }


                }


            }



            mBlogViewModel.searchallblogsactivate.observe(viewLifecycleOwner){
                if (it){
                    mBlogViewModel.searchallblogs.observe(viewLifecycleOwner){
                        val adapter = BlogAdapter( it  , JobApp.instance.applicationContext, this ,this)
                         mBinding.discussSearchRecycler.adapter = adapter
                        adapter.notifyDataSetChanged()


                    }
                }
            }

        }



    }

    override fun onDiscussclick(allDiscussionDTO: AllDiscussionDTO) {
        mDiscussViewModel.getSingleQuestion(allDiscussionDTO.questionid)
        mDiscussViewModel.questionid.value = allDiscussionDTO.questionid
        findNavController().navigate(R.id.discussDetail)
    }

    override fun onDiscusslike(allDiscussionDTO: AllDiscussionDTO) {
    }

    override fun navigateToblogDetails(itemview: View, blogDTO: BlogDTO) {
        val bundle = Bundle()
        bundle.putString("blogid", blogDTO.id)
        findNavController().navigate(R.id.blogDetails, bundle)
    }

    override fun clickonFav(itemview: View, blogDTO: BlogDTO) {
        mBlogViewModel.setfav(blogid = blogDTO.id)
        if(mBlogViewModel.favboolean.value == true){
            mBlogViewModel.favboolean.value = false

        }else{
            mBlogViewModel.favboolean.value = true

        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mBlogViewModel.searchallblogs.postValue(emptyList())
        mDiscussViewModel.alldiscussion.postValue(emptyList())
        mBlogViewModel.searchallblogsactivate.value = false
        mDiscussViewModel.searchdiscussrefresh.value = false
    }


}