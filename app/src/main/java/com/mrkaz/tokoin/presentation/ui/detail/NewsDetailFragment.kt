package com.mrkaz.tokoin.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import com.mrkaz.tokoin.common.constant.ITEM
import com.mrkaz.tokoin.data.models.news.NewsData
import com.mrkaz.tokoin.di.GlideApp
import com.mrkaz.tokoin.presentation.base.BaseFragment
import com.mrkaz.tokoin.R
import kotlinx.android.synthetic.main.fragment_news_detail.*

class NewsDetailFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_news_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = arguments?.getParcelable(ITEM) as? NewsData
        (activity as? AppCompatActivity)?.run {
            setSupportActionBar(toolbarDetail)
            supportActionBar?.title = item?.title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        GlideApp.with(this).load(item?.urlToImage).into(imgContent)
        txtContent.text = HtmlCompat.fromHtml(
            item?.content ?: "",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        txtUrl.text = item?.url ?: ""
        txtUrl.setOnClickListener {
            val bundle = bundleOf(ITEM to item)
            findNavController().navigate(R.id.action_detail_to_web, bundle)
        }

    }
}