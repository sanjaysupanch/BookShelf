package com.example.bookshelf.features.dashboard.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.features.authentication.data.model.User
import com.example.bookshelf.features.dashboard.data.model.BookInfo
import com.example.bookshelf.features.dashboard.data.model.TagsInfo
import com.example.bookshelf.features.dashboard.domain.FetchBookListUseCase
import com.example.bookshelf.features.utils.Results
import com.example.bookshelf.features.utils.UiStateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashViewModel @Inject constructor(
    private val fetchBookListUseCase: FetchBookListUseCase
) : ViewModel() {
    private val state = MutableStateFlow<UiStateResult>(UiStateResult.Init)
    val mState: StateFlow<UiStateResult> get() = state

    private var _mBookList = MutableStateFlow<List<BookInfo>?>(emptyList())
    val mBookList: StateFlow<List<BookInfo>?> get() = _mBookList

    private var _mTagInfo = MutableStateFlow<List<String>>(emptyList())
    val mTagInfo: StateFlow<List<String>> get() = _mTagInfo

    init {
        getBookList()
    }

    private fun getBookList() {
        viewModelScope.launch {
            fetchBookListUseCase.getBookListData()
                .flowOn(Dispatchers.IO)
                .onStart { setLoading() }
                .catch {
                    hideLoading()
                    showError(it.message.toString())
                    Log.e("DashViewModel", "Error fetching book list: ${it.message}")
                }
                .collect { result ->
                    when (result) {
                        is Results.Loading -> setLoading()
                        is Results.Error -> {
                            hideLoading()
                            showError(result.message)
                        }

                        is Results.Success -> {
                            hideLoading()
                            _mBookList.value = result.data
                        }
                    }
                }
        }
    }

    fun getTagsList(email: String, bookId: String) {
        viewModelScope.launch {
            try {
                val tagsInfo = fetchBookListUseCase.getTagsInfo(email, bookId)
                _mTagInfo.value = tagsInfo
            } catch (e: Exception) {
                Log.e("DashViewModel", e.message.toString())
            }
        }
    }

    suspend fun insertTags(tag: TagsInfo): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                fetchBookListUseCase.insertTags(tag)
                true
            } catch (e: Exception) {
                Log.e("DashViewModel", e.message.toString())
                false
            }
        }
    }

    private fun setLoading() {
        state.value = UiStateResult.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = UiStateResult.IsLoading(false)
    }

    private fun showError(message: String) {
        state.value = UiStateResult.Error(message)
    }
}