package com.dbvertex.job.peresentation.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.google.android.material.progressindicator.LinearProgressIndicator

class WebviewFragment : Fragment() {
    private lateinit var webView: WebView
    private lateinit var backweb : ImageView
    private lateinit var linearProgressIndicator: LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.web_view)
        backweb = view.findViewById(R.id.logo)
        backweb.setOnClickListener {
            findNavController().navigateUp()
        }
        // Here is I make transparent background for WebView
        webView.webViewClient = ppWebViewClient()
        webView.setBackgroundColor(0x00000000);



        linearProgressIndicator = view.findViewById(R.id.progress_indicator)
         val bundle = arguments
        if (bundle != null){
            val type = bundle.getInt("type",0)
           /* lifecycleScope.launch {
                val html = when(type){
                    1-> AboutUsRepository.getAboutUs()
                    2 -> AboutUsRepository.getPrivacyPolicy()
                    3 ->AboutUsRepository.getTermsAndConditions()
                    else-> "Some Error Occurred"
                }
               *//* withContext(Dispatchers.Main){
                   // loadHtmlData(html)
                    webView.loadData(html)
                    linearProgressIndicator.visibility = View.GONE
                }*//*

                webView.settings.javaScriptEnabled = true

                // if you want to enable zoom feature
                webView.settings.setSupportZoom(true)
            }
*/
            val html = when(type){
                1-> "https://work.dbvertex.com/thephototribe/welcome/aboutus"
                2 -> "https://work.dbvertex.com/thephototribe/welcome/privacypolicy"
                3 ->"https://work.dbvertex.com/thephototribe/welcome/termscondition"
                else-> "Some Error Occurred"
            }

              webView.loadUrl(html)
            webView.settings.javaScriptEnabled = true

            // if you want to enable zoom feature
            webView.settings.setSupportZoom(true)

        }
    }
/*    private fun loadHtmlData(html: String) {
        webView.loadData(
            """<html>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <body>
                            <font color='black'>
                                $html
                            </font>
                        </body>
                    </html>
                    """.trimIndent(),
            "text/html; charset=UTF-8", null
        )
    }*/
    inner class ppWebViewClient : android.webkit.WebViewClient() {

        // Load the URL
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        // ProgressBar will disappear once page is loaded
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
             linearProgressIndicator.visibility = View.GONE
                     }
    }
}