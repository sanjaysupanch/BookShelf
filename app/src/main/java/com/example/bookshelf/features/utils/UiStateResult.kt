package com.example.bookshelf.features.utils

sealed class UiStateResult {
    data class IsLoading(val isLoading: Boolean) : UiStateResult()
    data class Error(val message: String) : UiStateResult()
    data object Init : UiStateResult()
}