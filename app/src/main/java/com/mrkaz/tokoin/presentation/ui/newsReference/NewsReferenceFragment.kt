package com.mrkaz.tokoin.presentation.ui.newsReference


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrkaz.tokoin.R
import com.mrkaz.tokoin.common.constant.ITEM
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.models.news.NewsData
import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.presentation.base.BaseFragment
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.mrkaz.tokoin.presentation.base.view.OnRecyclerViewScrollListener
import com.mrkaz.tokoin.presentation.ui.news.NewsAdapter
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news_ref.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class NewsReferenceFragment : BaseFragment() {

    //region variable
    val viewModel: NewsRefVM by viewModel()
    private val adapter: NewsAdapter by lazy {
        NewsAdapter(arrayListOf()) {
            val bundle = bundleOf(ITEM to it)
            findNavController().navigate(R.id.action_news_ref_to_detail, bundle)
        }
    }
    private val rcvScrollListener: OnRecyclerViewScrollListener by lazy {
        OnRecyclerViewScrollListener {
            viewModel.fetchMore()
        }
    }
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

    private val referencesObserver =
        Observer<LiveDataWrapper<Pair<UserEntity?, List<ReferenceEntity>>>> { result ->
            when (result?.responseStatus) {
                LiveDataWrapper.ResponseStatus.LOADING -> {
                }
                LiveDataWrapper.ResponseStatus.ERROR -> {
                    viewHolder.visibility = View.VISIBLE
                    txtError.text = result.error?.message
                }
                LiveDataWrapper.ResponseStatus.SUCCESS -> {
                    result.response?.let {
                        populateReference(it.first, it.second)
                    }
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
    private val referenceObserver = Observer<String> {
        viewModel.updateReference(it)
        viewModel.refresh()
    }
    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.newsLiveData.observe(viewLifecycleOwner, newsObserver)
        viewModel.referencesLiveData.observe(viewLifecycleOwner, referencesObserver)
        viewModel.loadingLiveData.observe(viewLifecycleOwner, loadingObserver)
        viewModel.getAllReference()
        (activity as? AppCompatActivity)?.run {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        rcvItems.layoutManager = LinearLayoutManager(activity)
        rcvItems.adapter = adapter
        rcvItems.addOnScrollListener(rcvScrollListener)
        srlItems.setOnRefreshListener {
            rcvScrollListener.isLoadable = true
            viewModel.refresh()
        }
    }

    override fun onDestroyView() {
        rcvItems.adapter = null
        viewModel.reference.removeObserver(referenceObserver)
        super.onDestroyView()
    }

    override fun getLayoutId() = R.layout.fragment_news_ref

    private fun populateReference(userEntity: UserEntity?, items: List<ReferenceEntity>) {
        activity?.let {
            spinner.adapter =
                ArrayAdapter(
                    it,
                    R.layout.item_text,
                    items.map { item -> item.reference.toUpperCase(Locale.ROOT) })
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.reference.value = items[p2].reference
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
        userEntity?.reference?.let {
            val position = items.indexOfFirst { item -> item.reference == it }
            if (position >= 0) {
                spinner.setSelection(position)
            }
        } ?: spinner.setSelection(0)
        viewModel.reference.observe(viewLifecycleOwner, referenceObserver)
    }

    private fun populateData(items: MutableList<NewsData>, page: Int, totalResults: Int) {
        if (page == 1)
            adapter.updateItems(items)
        else
            adapter.addItems(items)
        rcvScrollListener.isLoadable = totalResults > adapter.itemCount
    }
    //endregion
}
