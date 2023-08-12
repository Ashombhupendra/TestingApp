package com.dbvertex.job.peresentation.navigate_to_page.blog.podcast

import android.Manifest
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.database.PodcastTableModel
import com.dbvertex.job.databinding.FragmentPodcastSearchBinding
import com.dbvertex.job.network.repository.PodcastRepository
import com.dbvertex.job.network.response.PodcastDTO
import com.dbvertex.job.network.utils.hideKeyboard
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PodcastSearch : Fragment(), RVPODCAST {
      private lateinit var mBinding : FragmentPodcastSearchBinding
    private val mPodcastViewModel by activityViewModels<PodcastViewmodel>()
    private var mMediaPlayer: MediaPlayer? = null
    private lateinit var mPodcastAdapter : PodcastAdapter
    private val mHandler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentPodcastSearchBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@PodcastSearch

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          mBinding.discussBack.setOnClickListener {
              findNavController().navigateUp()
          }
        mBinding.discusEtSearch.setOnEditorActionListener { textview, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(requireContext(), mBinding.discusEtSearch)
                if (!textview.text.isNullOrEmpty()){
                    mPodcastViewModel.searchpodcast(textview.text.toString())

                }else{
                    mPodcastViewModel.searchpodcastlist.postValue(emptyList())
                }
                //Toast.makeText(requireContext(), "Search : ${textview.text}", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }

        mPodcastAdapter = PodcastAdapter(rvpodcast = this).apply {
            registerAdapterDataObserver(object  : RecyclerView.AdapterDataObserver(){
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    //     val count = mPodcastAdapter.itemCount

                }
            })
        }
        mBinding.discussSearchRecycler.adapter = mPodcastAdapter
        mBinding.discussSearchRecycler.hasFixedSize()

         mPodcastViewModel.searchpodcastActivate.observe(viewLifecycleOwner){
             if (it){

                 mPodcastViewModel.searchpodcastlist.observe(viewLifecycleOwner){
                     if (it.isNullOrEmpty()){
                         mBinding.noDiscussFound.visibility = View.VISIBLE
                     }else{
                         mBinding.noDiscussFound.visibility= View.GONE
                     }

                     mPodcastAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(it))
                     mPodcastAdapter.notifyDataSetChanged()
                 }

                 mPodcastViewModel.favunfavBoolean.observe(viewLifecycleOwner){
                     if (it){
                         mPodcastAdapter.notifyDataSetChanged()
                     }
                 }
             }
         }
        //initializing the media player
        mMediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
        initViewHolderSeekBar()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPodcastAdapter.selfLifecycleDestroyed()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    private fun initViewHolderSeekBar() {
        requireActivity().runOnUiThread(object : Runnable {
            override fun run() {
                val isPlaying = mMediaPlayer?.isPlaying ?: false
                if (isPlaying) {
                    val progress = mMediaPlayer?.currentPosition
                    if (progress != null)  {
                        Log.d("seekbar3", progress.toString())
                        mPodcastAdapter.audioholder?.setProgress(progress)
                    }
                }
                mHandler.postDelayed(this, 100)
            }
        })
    }

    override fun onPlay(position: Int, playing: Boolean, progress: Int) {
        if (playing){
            val podItem = mPodcastAdapter.getPodcast(position)
            mMediaPlayer?.apply {
                reset()

                setDataSource(podItem?.audio)
                prepareAsync()
                mPodcastAdapter.audioholder?.isLoading?.value = true
                setOnCompletionListener {
                    mPodcastAdapter.audioholder?.resetUI()
                }
                setOnPreparedListener {
                    start()
                    seekTo(progress)
                    Log.d("seekbar", "$progress")
                    mPodcastAdapter.audioholder?.apply {
                        setMaxDuration(podItem!!.duration)
                        //Log.d("seelbarmax", "")
                        isLoading.value = false
                    }
                }
                setOnBufferingUpdateListener { _, percent ->
                    mPodcastAdapter.audioholder?.isLoading?.value =
                        percent < 100
                }
            }

        }else{
            mMediaPlayer?.reset()
        }
    }

    override fun stop() {
        mMediaPlayer?.reset()
    }

    override fun seek(progress: Int) {
        mMediaPlayer?.seekTo(progress)

    }

    override fun setpodcastfav(podcastDTO: PodcastDTO) {
        mPodcastViewModel.setFavUnFav(podcastDTO.id)

    }

    override fun setDownloadpodcast(podcastDTO: PodcastDTO) {
        //    val title = podcastDTO.title.subSequence(0, 20)
        permission(podcastDTO)
    }

    override fun onPause()  {
        super.onPause()
        mBinding.discussSearchRecycler.isSaveEnabled
    }

    override fun onResume() {
        super.onResume()
        mPodcastAdapter.refresh()
        mPodcastViewModel.searchpodcastlist.postValue(emptyList())
        mPodcastViewModel.searchpodcastActivate.value = false

    }

    private fun startDownload(url : PodcastDTO) {
        Toast.makeText(requireContext(), "Downloading Start...", Toast.LENGTH_SHORT).show()
        val request = DownloadManager.Request(Uri.parse(url.audio))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("TPA_${System.currentTimeMillis()}.mp3")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        try {
            val path = Environment.DIRECTORY_DOWNLOADS
            val time = "TPA_${System.currentTimeMillis()}.mp3"
            Log.d("pathfile", path.toString())
            request.setDestinationInExternalPublicDir(path, time)

            Podcast.oncompletedonwload.observe(viewLifecycleOwner){
                if (it== true){
                    GlobalScope.launch {
                        PodcastRepository.savepodcast(
                            PodcastTableModel(
                            url.id, url.title,url.description, url.created, time,url.duration, url.favourite
                        )
                        )
                    }
                    Podcast.oncompletedonwload.value = false
                }
            }


        } catch (e: Exception) {
            Log.d("downloaderror", e.toString())
            /* request.setDestinationInExternalPublicDir(
                 Environment.DIRECTORY_DOWNLOADS,
                 "TPA_${System.currentTimeMillis()}.mp3"
             )*/
        }


        val manager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)



    }




    private fun createdirectory(url : PodcastDTO){

        val downloadpod = PodcastRepository.getSavepodcast()

        startDownload(url)




    }


    private fun permission(url: PodcastDTO){
        Dexter.withContext(JobApp.instance.applicationContext)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                    if (report.areAllPermissionsGranted()) {
                        //  startDownload(url, song_title)

                        createdirectory(url)
                    } else {
                        showRationalDialogForPermissions()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                    token?.continuePermissionRequest()
                    showRationalDialogForPermissions()
                }
            }).check()
    }

    private fun showRationalDialogForPermissions() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        // set alert dialog message text color
        alertDialog.setTitle("Need Permissions")
        val message =
            SpannableString("This app needs permission to use this feature. You can grant them in app settings.")
        message.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            message.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton(
            "GO TO SETTINGS"
        ) { _, _ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", "com.app.phototribe", null)
                intent.data = uri
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
        alertDialog.setNegativeButton(
            "CANCEL"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }






}
