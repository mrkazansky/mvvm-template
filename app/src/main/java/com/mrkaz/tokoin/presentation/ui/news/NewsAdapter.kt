package com.mrkaz.tokoin.presentation.ui.news

import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.RecyclerView
import com.mrkaz.tokoin.R
import com.mrkaz.tokoin.data.models.news.NewsData
import com.mrkaz.tokoin.di.GlideApp
import com.mrkaz.tokoin.presentation.base.view.GenericAdapter
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter(items: ArrayList<NewsData>, listener: ((NewsData) -> Unit)) :
    GenericAdapter<NewsData>(items, listener) {

    override fun getLayoutId(position: Int, obj: NewsData): Int = R.layout.item_news

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder = NewsHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        (holder as? NewsHolder)?.bind(items[position])
    }

    class NewsHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun bind(item: NewsData){
            GlideApp
                .with(itemView.context)
                .load(item.urlToImage)
                .into(itemView.imgContent)

            itemView.txtTitle.text = item.title
            itemView.txtAuthor.text = item.author
            item.description?.let {
                itemView.txtContent.text = HtmlCompat.fromHtml(it, FROM_HTML_MODE_LEGACY)
            }
        }
    }
}