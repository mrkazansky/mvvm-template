package com.mrkaz.tokoin.presentation.base.view


import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class OnRecyclerViewScrollListener(val onLoadMore: (() -> Unit)) : RecyclerView.OnScrollListener() {

    var previousTotal = 0
    var isLoading = true
    var isLoadable = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.layoutManager!!.itemCount
        val firstVisibleItem =
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (isLoading) {
            if (totalItemCount > previousTotal) {
                isLoading = false
                previousTotal = totalItemCount
            }
        }
        val visibleThreshold = 2
        if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold && isLoadable) {
            onLoadMore.invoke()
            isLoading = true
        }
    }
}