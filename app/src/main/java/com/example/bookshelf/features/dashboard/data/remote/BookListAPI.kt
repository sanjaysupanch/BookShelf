package com.example.bookshelf.features.dashboard.data.remote

import com.example.bookshelf.features.dashboard.data.model.BookInfo
import retrofit2.Response
import retrofit2.http.GET

interface BookListAPI {

    @GET("b/CNGI")
    suspend fun getBookListData(): Response<List<BookInfo>>
}