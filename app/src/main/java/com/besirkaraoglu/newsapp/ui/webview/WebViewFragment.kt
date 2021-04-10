package com.besirkaraoglu.newsapp.ui.webview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebView
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.besirkaraoglu.newsapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    private lateinit var webview: WebView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webview = requireActivity().findViewById(R.id.webView)

        val ibBack = requireActivity().findViewById<ImageButton>(R.id.ibBackWeb)
        ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        val bundle = requireArguments()
        val url = bundle.getString("url")
        url?.let { webview.loadUrl(it) }
    }

}