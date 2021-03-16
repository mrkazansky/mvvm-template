package com.mrkaz.tokoin.usecase.news

import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.data.repository.NewsRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class NewsUseCase : KoinComponent {

    private val newsRepository : NewsRepository by inject()

    suspend fun fetchTopHeadlines(page: Int, query: String? = null, country: String? = null) : NewsResponse {
        return newsRepository.fetchTopHeadlines(page, query, country)
    }
}