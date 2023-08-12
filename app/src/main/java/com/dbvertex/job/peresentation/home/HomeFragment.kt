package com.dbvertex.job.peresentation.home

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dbvertex.job.data.UsersList
import com.dbvertex.job.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class HomeFragment : Fragment() {
    private lateinit var mBinding: FragmentHomeBinding
    val STORAGE_PERMISSION_CODE = 0
    val list = ArrayList<UsersList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@HomeFragment

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


              //readuser()


    }
    fun readuser(){
        list.clear()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("Users")


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val id = it.child("id").value.toString()
                    val username = it.child("username").value.toString()
                    val imageurl = it.child("imageURL").value.toString()
                    list.add(
                        UsersList(
                            id,
                            username,
                            imageurl
                        )
                    )
                }
                val adapter = UserAdapter(requireContext(), list)
                mBinding.userRecycler.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun checkpermission(){
        Dexter.withContext(requireContext()).withPermissions(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        startdownloading()
                    } else {

                        requestPermissions(
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION_CODE
                        )
                    }


                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()

                }

            }).onSameThread().check()


    }

    private fun startdownloading() {
        val url = mBinding.audioLink.text.toString()

        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${System.currentTimeMillis()}.mp3"
        )

        val manager  = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startdownloading()
                } else {
                    Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

}