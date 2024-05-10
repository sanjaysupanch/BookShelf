package com.example.bookshelf.features.authentication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.features.authentication.data.model.User
import com.example.bookshelf.features.authentication.domain.FetchUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase,
) : ViewModel() {

    private var _mUser = MutableStateFlow(false)
    val mUser: StateFlow<Boolean> get() = _mUser

    fun getUserList(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = fetchUserUseCase.loginUser(email, password)
                _mUser.value = user
            } catch (e: Exception) {
                Log.e("LoginViewModel", e.message.toString())
            }
        }
    }
}