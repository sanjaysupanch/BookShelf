package com.example.bookshelf.features.authentication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.features.authentication.data.model.User
import com.example.bookshelf.features.authentication.domain.FetchUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase
) : ViewModel() {

    private val _mCountryList = MutableStateFlow<List<String>>(emptyList())
    val mCountryList: StateFlow<List<String>> get() = _mCountryList

    private var _mUserExist = MutableStateFlow(false)
    val mUserExist: StateFlow<Boolean> get() = _mUserExist

    init {
        getCountryList()
    }

    fun isUserExisting(email: String) {
        viewModelScope.launch {
            try {
                val user = fetchUserUseCase.isUserExisting(email)
                _mUserExist.value = user
            } catch (e: Exception) {
                Log.e("LoginViewModel", e.message.toString())
            }
        }
    }

    suspend fun insertUser(newUser: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                fetchUserUseCase.insertUser(newUser)
                true
            } catch (e: Exception) {
                Log.e("RegistrationViewModel", e.message.toString())
                false
            }
        }
    }

    private fun getCountryList() {
        viewModelScope.launch {
            fetchUserUseCase.getCountryData()
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.e("RegistrationViewModel", it.message.toString())
                }
                .collect { result ->
                    _mCountryList.value = result
                }
        }
    }
}