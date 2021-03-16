package com.mrkaz.tokoin.presentation.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.mrkaz.tokoin.common.constant.ITEM
import com.mrkaz.tokoin.data.models.news.NewsData
import com.mrkaz.tokoin.presentation.base.BaseFragment
import com.mrkaz.tokoin.R
import kotlinx.android.synthetic.main.fragment_web.*

class WebViewFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_web

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = arguments?.getParcelable(ITEM) as? NewsData
        (activity as? AppCompatActivity)?.run {
            setSupportActionBar(toolbarWeb)
            supportActionBar?.title = item?.title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        webView.apply {
            webChromeClient = WebChromeClient()
            setInitialScale(1)
            settings.apply {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                loadsImagesAutomatically = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                javaScriptEnabled = true
            }
        }
        item?.url?.let {
            webView.loadUrl(it)
        }

    }
}