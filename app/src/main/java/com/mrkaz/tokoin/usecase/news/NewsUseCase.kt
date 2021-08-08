package com.mrkaz.tokoin.usecase.news

import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.data.repository.INewsRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class NewsUseCase : KoinComponent {

    private val newsRepository : INewsRepository by inject()

    suspend fun fetchTopHeadlines(page: Int, query: String? = null, country: String? = null) : NewsResponse {
        return newsRepository.fetchTopHeadlines(page, query, country)
    }
}