package com.dbvertex.job.peresentation.photoshoot.contract

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R

class PreviewContract : Fragment() {
             private lateinit var webView: WebView
             private lateinit var title :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         val back = view.findViewById<ImageView>(R.id.back_preview_contract)
        back.setOnClickListener {
            findNavController().navigateUp()
        }
        webView= view.findViewById(R.id.webView_preview_contract)
        title  = view.findViewById(R.id.preview_contract_title)
        webView.webViewClient = WebViewClient()




          val bundle = arguments
        if (bundle != null){
            val link = bundle.getString("preview_link").toString()
            val previewtype = bundle.getString("preview_type").toString()
            if (previewtype.equals("invoice")){
                title.setText("Preview Invoice")
            }else{
                title.setText("Preview Contract")
            }
// this will load the url of the website
            Log.d("linkssss", link.toString())
            webView.loadUrl("$link")
            // this will enable the javascript settings
            webView.settings.javaScriptEnabled = true

            // if you want to enable zoom feature
            webView.settings.setSupportZoom(true)
        }
    }
}