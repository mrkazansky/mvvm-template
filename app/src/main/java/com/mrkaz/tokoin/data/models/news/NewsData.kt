package com.mrkaz.tokoin.data.models.news

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsData(
    val author: String?,
    val title: String?,
    val description: String?,
    val content: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?
) : Parcelable