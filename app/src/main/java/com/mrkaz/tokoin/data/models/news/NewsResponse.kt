package com.mrkaz.tokoin.data.models.news

data class NewsResponse(
    var page: Int,
    val totalResults : Int,
    val status : String,
    val articles : List<NewsData>
)