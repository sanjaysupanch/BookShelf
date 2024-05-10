package com.example.bookshelf.features.authentication.domain

import android.util.Log
import com.example.bookshelf.features.authentication.data.model.User
import com.example.bookshelf.features.authentication.data.local.UserDataBase
import com.example.bookshelf.features.authentication.data.remote.CountryAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchUserUseCase @Inject constructor(
    private val userDB: UserDataBase,
    private val countryAPI: CountryAPI
) {

    suspend fun insertUser(newUser: User) {
        withContext(Dispatchers.IO) {
            userDB.userDao().insertUser(newUser)
        }
    }

    suspend fun loginUser(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val users = userDB.userDao().getAllUser()
            val user = users.find { it.email == email && it.password == password }
            user != null
        }
    }

    suspend fun isUserExisting(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            val users = userDB.userDao().getAllUser()
            val user = users.find { it.email == email }
            user != null
        }
    }

    fun getCountryData(): Flow<List<String>> = flow {
        val result = countryAPI.getCountryData()
        val ok = "OK"
        if (result.status == ok) {
            val countryNames = result.data.values.map { it.country }
            emit(countryNames)
        } else {
            emit(emptyList())
        }
    }.catch { exception ->
        Log.e("Country Data", exception.message.toString())
        emit(emptyList())
    }.flowOn(Dispatchers.IO)
}