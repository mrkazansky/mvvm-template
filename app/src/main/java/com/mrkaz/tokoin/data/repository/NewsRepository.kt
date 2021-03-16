package com.mrkaz.tokoin.data.repository

import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.data.network.news.NewsService
import com.mrkaz.tokoin.BuildConfig
import org.koin.core.KoinComponent
import org.koin.core.inject

class NewsRepository : KoinComponent {

    private val newsService: NewsService by inject()

    suspend fun fetchTopHeadlines(page: Int, query: String?, country: String?): NewsResponse {
        return newsService.getTopHeadlines(page, BuildConfig.API_KEY, query, country)
    }
}