package com.dbvertex.job.peresentation.chat

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.webkit.MimeTypeMap
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentChatMessageBinding
import com.dbvertex.job.network.repository.ChatRepository
import com.dbvertex.job.network.response.chat.ChatList
import com.dbvertex.job.network.utils.FileUtils
import com.dbvertex.job.network.utils.ResultWrapper

import com.dbvertex.job.peresentation.auth.NetworkState
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.google.android.gms.common.util.IOUtils.toByteArray
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.*


class ChatMessage : Fragment(), RVHost {
    private lateinit var mBinding: FragmentChatMessageBinding
    private val mChatViewmodel by activityViewModels<ChatViewmodel>()
    private lateinit var mAdapter: ChatMessageAdapter
    private val CAMERA_INTENT_CODE = 101
    private val GALLERY_INTENT_CODE = 100
    private val DOCUMENT_INTENT_CODE = 102
    private var mMediaPlayer: MediaPlayer? = null
    private val mHandler = Handler(Looper.getMainLooper())
    private val AUDIO_INTENT_CODE = 103
    private val PDF_INTENT_CODE = 104
    private val VIDEO_INTENT_CODE = 105
    val playingid = MutableLiveData<Boolean>(false)
    lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentChatMessageBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@ChatMessage
            viewmodel = mChatViewmodel
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()

                }
            }

        mBinding.chatMessageAttach.visibility=View.GONE
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        mBinding.chatmsgback.setOnClickListener {
            findNavController().navigateUp()

        }

        ///we are calling header profile from receiver_id

        val bundle = arguments
        if (bundle != null) {

            //this is profile id of job giver
            val receiver_id = bundle.getString("receiver_id")
            val image = bundle.getString("image")

            if(image!=null){

                Picasso.get().load(image).into(mBinding.chatMessageProfileImg)
            }


            mChatViewmodel.msg_sender_id.value = receiver_id.toString()


            mBinding.chatMessageSend.setColorFilter(

                ContextCompat.getColor(
                    JobApp.instance.applicationContext,
                    R.color.grey
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            );
            mBinding.chatMessageSend.isClickable = false
            ///web view//////
            mBinding.chatMessageWebview.webViewClient = WebViewClient()
            //this url show chats b/w users
            mBinding.chatMessageWebview.loadUrl("https://work.dbvertex.com/thephototribe/welcome/chats/${SharedPrefrenceHelper.user.userid.toString()}/$receiver_id")
            mBinding.chatMessageWebview.setBackgroundColor(0x00000000);
            // this will enable the javascript settings
            mBinding.chatMessageWebview.settings.javaScriptEnabled = true

            // if you want to enable zoom feature
            mBinding.chatMessageWebview.settings.setSupportZoom(false)
            mBinding.pro.visibility=View.VISIBLE
            mChatViewmodel.getChatlist(
                receiver_id.toString(),
                SharedPrefrenceHelper.user.userid.toString()
            )
            //this will load header profile sender image
            mChatViewmodel.getChatHeader("${receiver_id.toString()}")


            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {

                    lifecycleScope.launch {
                        val result = ChatRepository.getpdfurl()
                        when (result) {
                            is ResultWrapper.Success -> {
                                if (!result.response.equals("null")) {
                                    val openURL = Intent(Intent.ACTION_VIEW)
                                    openURL.data = Uri.parse("${result.response}")
                                    startActivity(openURL)
                                }
                            }
                            is ResultWrapper.Failure -> {
                                Log.d("error", result.errorMessage)
                            }
                        }
                    }

                    handler.postDelayed(this, 500)
                }
            }, 500)


        }

        initViewHolderSeekBar()
        //initializing the media player
        mMediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }

        mChatViewmodel.profile_loding.observe(viewLifecycleOwner) {
            if (it) {
                Picasso.get().load(mChatViewmodel.senderprofileimg.value.toString())
                    .into(mBinding.chatMessageProfileImg)
                mBinding.cahtMessageUsername.text = "${mChatViewmodel.sendername.value}"
                mChatViewmodel.profile_loding.value = false

            }

        }
        mChatViewmodel.profile_verified.observe(viewLifecycleOwner) {
            if (it) {
//                mBinding.verifiedProfile.visibility = View.VISIBLE
//                mBinding.verifiedProfileTv.visibility = View.VISIBLE
            } else {
//                mBinding.verifiedProfile.visibility = View.GONE
//                mBinding.verifiedProfileTv.visibility = View.GONE
            }
        }

        mAdapter = ChatMessageAdapter(this)


        mBinding.chatMessageRv.adapter = mAdapter


        mBinding.chatMessageRv.hasFixedSize()
        mChatViewmodel.chatlist.observe(viewLifecycleOwner, Observer {
            mBinding.pro.visibility=View.GONE
            if (it.isNullOrEmpty()) {
                mBinding.chatMessageNochatfound.visibility = View.GONE
                mAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(it))
                mAdapter.notifyDataSetChanged()

                mBinding.chatMessageRv.scrollToPosition(0)


            } else {
                mBinding.chatMessageNochatfound.visibility = View.GONE
                mAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(it))
                mAdapter.notifyDataSetChanged()
                mBinding.chatMessageRv.scrollToPosition(0)

            }
        })

        mBinding.chatMessageSend.setOnClickListener {
            val scaleXAnimator = ObjectAnimator.ofFloat(it, "scaleX", 1f, 1.5f, 1f)
            val scaleYAnimator = ObjectAnimator.ofFloat(it, "scaleY", 1f, 1.5f, 1f)
            val rotationAnimator = ObjectAnimator.ofFloat(it, "rotation", 0f, 0f, 0f)
            AnimatorSet().apply {
                duration = 1000
                interpolator = OvershootInterpolator()
                playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
                start()
            }
            if (mChatViewmodel.msg_message.value.isNullOrEmpty()) {
                mBinding.chatMessageEt.setError("Enter Something here")
            } else {


                mChatViewmodel.sendmessage()
                mChatViewmodel.getChatlist(
                    mChatViewmodel.msg_sender_id.value.toString(),
                    SharedPrefrenceHelper.user.userid.toString()
                )
            }
        }
        mBinding.chatMessageEt.apply {
            doOnTextChanged { text, start, before, count ->
                if (text!!.length < 1) {
                    mBinding.chatMessageSend.setColorFilter(
                        ContextCompat.getColor(
                            JobApp.instance.applicationContext,
                            R.color.grey
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    );
                    mBinding.chatMessageSend.isClickable = false
                } else {
                    mBinding.chatMessageSend.setColorFilter(
                        ContextCompat.getColor(
                            JobApp.instance.applicationContext,
                            R.color.blue_dark
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    );
                    mBinding.chatMessageSend.isClickable = true
                }
            }
        }

        mBinding.chatMessageAttach.setOnClickListener {
            permission()
        }


        mChatViewmodel.chatmsgsendstate.observe(viewLifecycleOwner, Observer { state ->

            when (state) {
                NetworkState.SUCCESS -> {
                    mChatViewmodel.getChatlist(
                        mChatViewmodel.msg_sender_id.value.toString(),
                        SharedPrefrenceHelper.user.userid.toString()
                    )
                    mChatViewmodel.msg_message.value = ""
                    mAdapter.notifyDataSetChanged()
                }


            }

        })

        mChatViewmodel.multimediastate.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                NetworkState.LOADING_STARTED -> {

                }
                NetworkState.LOADING_STOPPED -> {

                }
                NetworkState.SUCCESS -> {
                    mBinding.progressBar.visibility = View.GONE
                    requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    mChatViewmodel.getChatlist(
                        mChatViewmodel.msg_sender_id.value.toString(),
                        SharedPrefrenceHelper.user.userid.toString()
                    )

                }
                NetworkState.FAILED -> {
                    mBinding.progressBar.visibility = View.GONE
                    requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                }
            }

        })

    }


    private fun bottomDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())


        val bottomsheetview = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.bottom_sheet_dialog,
                view?.findViewById(R.id.bottom_sheet)
            ) as LinearLayout

        val buttoncamera = bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_camera)
        val buttonGallery = bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_gallery)
        val buttondocument = bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_document)
        val buttonaudio = bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_audio)
        val bottomSheet_vedio = bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_vedio)
        val bottomSheet_delete_profile =
            bottomsheetview.findViewById<ImageView>(R.id.bottomSheet_delete_profile)
        val bottomSheet_delete_profile_tv =
            bottomsheetview.findViewById<TextView>(R.id.bottomSheet_delete_profile_tv)
        bottomSheet_delete_profile_tv.visibility = View.GONE
        bottomSheet_delete_profile.visibility = View.GONE
        bottomSheetDialog.setContentView(bottomsheetview)
        bottomSheetDialog.show()

        bottomSheet_vedio.setOnClickListener {
            getVideo()
            bottomSheetDialog.dismiss()
        }
        buttoncamera.setOnClickListener {
            getCameraImage()
            bottomSheetDialog.dismiss()
        }
        buttonGallery.setOnClickListener {
            getGalleryImage()
            bottomSheetDialog.dismiss()
        }
        buttondocument.setOnClickListener {
            getPDF()
            bottomSheetDialog.dismiss()
        }
        buttonaudio.setOnClickListener {
            getAudioFile()
            bottomSheetDialog.dismiss()
        }

    }


    fun mtoast(msg: String) {
        Toast.makeText(JobApp.instance.applicationContext, "$msg", Toast.LENGTH_SHORT).show()
    }


    private fun getAudioFile() {
        val intent = Intent()
        intent.type = "audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Audio "), AUDIO_INTENT_CODE)
    }

    private fun getCameraImage() {

        val cameraIntent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivityForResult(cameraIntent, CAMERA_INTENT_CODE)

    }

    private fun getVideo() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "video/*"
        }
        startActivityForResult(intent, VIDEO_INTENT_CODE)
    }

    private fun getPDF() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "application/pdf"
        startActivityForResult(intent, PDF_INTENT_CODE)
    }


    private fun getGalleryImage() {
        val gallery = Intent(Intent.ACTION_PICK).apply {
            this.type = "image/*"
        }
        startActivityForResult(gallery, GALLERY_INTENT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("resultcode", resultCode.toString())


        if (requestCode == GALLERY_INTENT_CODE && data != null) {
            val uri = data.data
            uri ?: return

            requireActivity().contentResolver.openInputStream(uri)?.let { inputStream ->
                val name = FileUtils.getFile(requireContext(), uri).name
                val bytes = inputStream.readBytes()

                mChatViewmodel.Image = bytes to name
            }

            mBinding.progressBar.visibility = View.VISIBLE
            //this is to use no touch window while process
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            );
            Handler().postDelayed({

                mChatViewmodel.sendMultimedia()

            }, 300) // 3000 is the delayed time in milliseconds.
        } else if (requestCode == CAMERA_INTENT_CODE && data != null) {
            val uri = getImageUri(data.extras!!.get("data") as Bitmap)
            uri ?: return

            requireActivity().contentResolver.openInputStream(uri)?.let { inputStream ->
                val name = FileUtils.getFile(requireContext(), uri).name
                val bytes = inputStream.readBytes()

                mChatViewmodel.Image = bytes to name
            }

            mBinding.progressBar.visibility = View.VISIBLE
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            );
            mChatViewmodel.sendMultimedia()

        } else if (requestCode == AUDIO_INTENT_CODE && data != null) {
            val uri = data.data
            uri ?: return
            Log.d("songuri", uri.toString())
            var bytearray = ByteArray(0)
            try {
                val inputStream: InputStream =
                    JobApp.instance.applicationContext.contentResolver.openInputStream(uri)!!
                bytearray = ByteArray(inputStream.available())
                bytearray = toByteArray(inputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            }


            mChatViewmodel.audio = bytearray

            mBinding.progressBar.visibility = View.VISIBLE
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            );
            mChatViewmodel.sendMultimedia()
        } else if (requestCode == PDF_INTENT_CODE && data != null) {
            val uri = data.data
            uri ?: return

            var bytearray = ByteArray(0)
            try {
                val inputStream: InputStream =
                    JobApp.instance.applicationContext.contentResolver.openInputStream(uri)!!
                bytearray = ByteArray(inputStream.available())
                bytearray = toByteArray(inputStream)
                mChatViewmodel.pdf_name.value = queryName(requireContext(), uri)

                Log.d("filename", "name : ${queryName(requireContext(), uri)}")

            } catch (e: Exception) {
                e.printStackTrace()
            }
            mChatViewmodel.pdf = bytearray

            mBinding.progressBar.visibility = View.VISIBLE
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            );
            mChatViewmodel.sendMultimedia()
//           mtoast("result  : ${FileUtils.getFile(requireContext(), uri).name}")
        } else if (requestCode == VIDEO_INTENT_CODE && data != null) {
            val uri = data?.data

            val size = getVedioSize(JobApp.instance.applicationContext, uri)
            val sizeinmb = size!!.toInt() / 1024 / 1024


            Log.d("size", size.toString())
            if (sizeinmb < 15) {

                uri?.let { u ->
                    requireContext().contentResolver.openInputStream(u)?.let {
                        val name = FileUtils.getFile(requireContext(), u).name
                        val bytes = it.readBytes()
                        mChatViewmodel.video = bytes to name
                    }
                }
                mBinding.progressBar.visibility = View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                );
                mChatViewmodel.sendMultimedia()
                // mtoast("Vedio size correct. $sizeinmb")
            } else {
                mtoast("Vedio size should be less than 15MB  $sizeinmb")

            }

        } else {
            Log.d("result_chat", data.toString())
        }

    }

    //*******PDF CONTENT START*******//


    //*****END****//
    fun getImageUri(inImage: Bitmap): Uri? {

        val wrapper = ContextWrapper(JobApp.instance.applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "P_${System.currentTimeMillis()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.fromFile(file)
    }

    private fun permission() {
        Dexter.withContext(JobApp.instance.applicationContext)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                    if (report.areAllPermissionsGranted()) {
                        bottomDialog()
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


    fun getMimeType(url: String): String {
        var type: String? = null

        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase())
        }
        if (type == null) {
            type = "image/*" // fallback type. You might set it to */*
        }
        return type
    }

    override fun onPlay(position: Int, playing: Boolean, progress: Int) {

        if (playing) {
            val messageitem = mAdapter.getChatMessage(position)
            mMediaPlayer?.apply {
                reset()
                setDataSource(messageitem?.content)
                prepareAsync()
                ///  adapter.viewholder?.playing?.value = true
                setOnCompletionListener {
                    val userid = SharedPrefrenceHelper.user.userid.toString()
                    if (messageitem?.sender_id.equals(userid)) {
                        mAdapter.myMessageplayviewholder?.resetUI()

                    } else {
                        mAdapter.otherMessageplayviewholder?.resetUI()
                    }

                    Log.d("msgplayer", "preparedcomplete ${messageitem?.content}")

                }
                setOnPreparedListener {
                    start()
                    seekTo(progress)
                    Log.d("msgplayer", "preparedcomplete111 ${messageitem?.content}")
                    val userid = SharedPrefrenceHelper.user.userid.toString()
                    if (messageitem?.sender_id.equals(userid)) {
                        playingid.value = true
                        mAdapter.myMessageplayviewholder?.setMaxDuration(messageitem!!.duration)

                    } else {
                        playingid.value = false

                        mAdapter.otherMessageplayviewholder?.setMaxDuration(messageitem!!.duration)

                    }


                }
                setOnBufferingUpdateListener { _, percent ->

                }
            }
        } else {
            mMediaPlayer?.reset()

        }
    }


    override fun stop() {

        mMediaPlayer?.reset()

    }

    override fun seek(progress: Int) {
        mMediaPlayer?.seekTo(progress)
    }

    override fun onPdfClick(chatList: ChatList) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://docs.google.com/viewerng/viewer?url=${chatList.content}")
        startActivity(openURL)
    }

    fun initViewHolderSeekBar() {
        requireActivity().runOnUiThread(object : Runnable {
            override fun run() {

                val isPlaying = mMediaPlayer?.isPlaying ?: false
                if (isPlaying) {
                    val progress = mMediaPlayer?.currentPosition
                    if (progress != null) {
                        if (playingid.value!!.equals(true)) {
                            mAdapter.myMessageplayviewholder!!.setProgress(progress)

                        } else {
                            mAdapter.otherMessageplayviewholder!!.setProgress(progress)

                        }

                    }
                }

                mHandler.postDelayed(this, 100)
            }
        })
    }

    private fun queryName(context: Context, uri: Uri): String? {
        val returnCursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    fun getVedioSize(context: Context, uri: Uri?): String? {
        var fileSize: String? = null
        val cursor: Cursor = context.contentResolver.query(uri!!, null, null, null, null, null)!!
        try {
            if (cursor != null && cursor.moveToFirst()) {

                // get file size
                val sizeIndex: Int = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (!cursor.isNull(sizeIndex)) {
                    fileSize = cursor.getString(sizeIndex)
                }
            }
        } finally {
            cursor.close()
        }
        return fileSize
    }

}