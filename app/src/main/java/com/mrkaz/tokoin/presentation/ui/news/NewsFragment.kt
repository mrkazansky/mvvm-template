package com.mrkaz.tokoin.presentation.ui.news


import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrkaz.tokoin.common.constant.ITEM
import com.mrkaz.tokoin.data.models.news.NewsData
import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.presentation.base.BaseFragment
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.mrkaz.tokoin.presentation.base.view.OnRecyclerViewScrollListener
import com.mrkaz.tokoin.R
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.android.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment() {

    //region variable
    private val viewModel: NewsVM by viewModel()
    private val adapter: NewsAdapter by lazy {
        NewsAdapter(arrayListOf()) {
            val bundle = bundleOf(ITEM to it)
            findNavController().navigate(R.id.action_news_to_detail, bundle)
        }
    }
    private val rcvScrollListener: OnRecyclerViewScrollListener by lazy {
        OnRecyclerViewScrollListener {
            viewModel.fetchMore()
        }
    }

    private var paging = 1
    //endregion

    //region state
    private val newsObserver = Observer<LiveDataWrapper<NewsResponse>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.ResponseStatus.LOADING -> {
            }
            LiveDataWrapper.ResponseStatus.ERROR -> {
                rcvItems.visibility = View.GONE
                viewHolder.visibility = View.VISIBLE
                txtError.text = result.error?.message
            }
            LiveDataWrapper.ResponseStatus.SUCCESS -> {
                rcvItems.visibility = View.VISIBLE
                viewHolder.visibility = View.GONE
                val response = result.response as NewsResponse
                val items = response.articles
                populateData(items.toMutableList(), response.page, response.totalResults)
            }
        }
    }

    private val loadingObserver = Observer<Boolean> { visible ->
        when {
            visible -> {
                rcvScrollListener.isLoading = true
                progress.visibility = View.VISIBLE
            }
            else -> {
                rcvScrollListener.isLoading = false
                srlItems.isRefreshing = false
                progress.visibility = View.GONE
            }
        }
    }
    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.newsLiveData.observe(viewLifecycleOwner, this.newsObserver)
        viewModel.loadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)
        rcvItems.layoutManager = LinearLayoutManager(activity)
        rcvItems.adapter = adapter
        rcvItems.addOnScrollListener(rcvScrollListener)
        srlItems.setOnRefreshListener {
            rcvScrollListener.isLoadable = true
            paging = 1
            viewModel.refresh()
        }
        viewModel.refresh()
    }

    override fun onDestroyView() {
        rcvItems.adapter = null
        super.onDestroyView()
    }

    override fun getLayoutId() = R.layout.fragment_news

    private fun populateData(items: MutableList<NewsData>, page: Int, totalResults: Int) {
        if (page == 1)
            adapter.updateItems(items)
        else
            adapter.addItems(items)
        rcvScrollListener.isLoadable = totalResults > adapter.itemCount
    }
    //endregion
}
