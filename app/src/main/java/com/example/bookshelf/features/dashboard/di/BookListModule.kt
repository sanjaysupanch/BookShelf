package com.example.bookshelf.features.dashboard.di

import com.example.bookshelf.features.dashboard.data.remote.BookListAPI
import com.example.bookshelf.features.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookListModule {

    @Singleton
    @Provides
    @Named("bookListRetrofit")
    fun providesBookListRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BOOK_LIST_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesBookListAPI(@Named("bookListRetrofit") retrofit: Retrofit): BookListAPI {
        return retrofit.create(BookListAPI::class.java)
    }
}