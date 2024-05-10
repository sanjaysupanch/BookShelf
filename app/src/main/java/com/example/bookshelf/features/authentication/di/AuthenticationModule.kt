package com.example.bookshelf.features.authentication.di

import android.content.Context
import androidx.room.Room
import com.example.bookshelf.features.authentication.data.local.UserDataBase
import com.example.bookshelf.features.authentication.data.remote.CountryAPI
import com.example.bookshelf.features.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Singleton
    @Provides
    fun provideUserDB(@ApplicationContext context: Context): UserDataBase {
        return Room.databaseBuilder(
            context,
            UserDataBase::class.java,
            "bookshelf_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    @Named("authenticationRetrofit")
    fun providesAuthenticationRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesCountryAPI(@Named("authenticationRetrofit") retrofit: Retrofit): CountryAPI {
        return retrofit.create(CountryAPI::class.java)
    }
}