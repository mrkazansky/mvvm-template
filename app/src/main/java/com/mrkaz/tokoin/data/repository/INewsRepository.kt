package com.mrkaz.tokoin.data.repository

import com.mrkaz.tokoin.data.models.news.NewsResponse

interface INewsRepository {

    suspend fun fetchTopHeadlines(page: Int, query: String?, country: String?): NewsResponse

}