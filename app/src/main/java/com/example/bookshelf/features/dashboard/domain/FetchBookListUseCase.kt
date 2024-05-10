package com.example.bookshelf.features.dashboard.domain

import com.example.bookshelf.features.authentication.data.local.UserDataBase
import com.example.bookshelf.features.authentication.data.model.User
import com.example.bookshelf.features.dashboard.data.model.BookInfo
import com.example.bookshelf.features.dashboard.data.model.TagsInfo
import com.example.bookshelf.features.dashboard.data.remote.BookListAPI
import com.example.bookshelf.features.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchBookListUseCase @Inject constructor(
    private val bookListAPI: BookListAPI,
    private val userDB: UserDataBase,
) {

    fun getBookListData(): Flow<Results<List<BookInfo>>> = flow {
        emit(Results.Loading)
        try {
            val response = bookListAPI.getBookListData()
            if (response.isSuccessful) {
                emit(Results.Success(response.body()))
            } else {
                emit(Results.Error("Failed to fetch book list: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Results.Error("Failed to fetch book list: ${e.message}"))
        }
    }

    suspend fun getTagsInfo(email: String, bookId: String): List<String> {
        return withContext(Dispatchers.IO) {
            val tags = userDB.tagsDao().getAllTags()
            tags.filter { it.email == email && it.bookId == bookId }
                .map { it.tag ?: "" }
                .distinct()
        }
    }

    suspend fun insertTags(tag: TagsInfo) {
        withContext(Dispatchers.IO) {
            userDB.tagsDao().insertTags(tag)
        }
    }
}