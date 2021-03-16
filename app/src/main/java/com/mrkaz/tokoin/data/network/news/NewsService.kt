package com.mrkaz.tokoin.data.network.news

import com.mrkaz.tokoin.data.models.news.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String,
        @Query("q") query: String?,
        @Query("country") country: String?
    ): NewsResponse

}