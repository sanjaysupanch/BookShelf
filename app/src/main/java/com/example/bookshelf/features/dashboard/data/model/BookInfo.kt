package com.example.bookshelf.features.dashboard.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookInfo(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("score")
    val score: Double? = null,
    @SerializedName("popularity")
    val popularity: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("publishedChapterDate")
    var publishedChapterDate: Long = 0
) : Parcelable