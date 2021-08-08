package com.mrkaz.tokoin.data.repository.impl

import com.mrkaz.tokoin.BuildConfig
import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.data.network.news.NewsService
import com.mrkaz.tokoin.data.repository.INewsRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class NewsRepository : INewsRepository, KoinComponent {

    private val newsService: NewsService by inject()

    override suspend fun fetchTopHeadlines(
        page: Int,
        query: String?,
        country: String?
    ): NewsResponse {
        return newsService.getTopHeadlines(page, BuildConfig.API_KEY, query, country)
    }
}