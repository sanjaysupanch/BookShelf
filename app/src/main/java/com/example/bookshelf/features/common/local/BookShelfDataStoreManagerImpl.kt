package com.example.bookshelf.features.common.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BookShelfDataStoreManagerImpl(
    private val context: Context
) : BookShelfDataStoreManager {
    private val Context.appDataStore by preferencesDataStore(name = SHARED_PREFS_KEY)

    override fun setLoginStatus(isLogin: Boolean) {
        runBlocking {
            context.appDataStore.edit { it[prefAuthentication] = isLogin }
        }
    }

    override fun getLoginStatus(): Boolean {
        return runBlocking {
            context.appDataStore.data.first()[prefAuthentication] ?: false
        }
    }

    override fun setCurrentUser(user: String) {
        runBlocking {
            context.appDataStore.edit { it[prefCurrentUser] = user }
        }    }

    override fun getCurrentUser(): String {
        return runBlocking {
            context.appDataStore.data.first()[prefCurrentUser] ?: ""
        }
    }

    override fun clearSession() {
        runBlocking {
            context.appDataStore.edit {
                it.clear()
            }
        }
    }

    companion object {
        private const val SHARED_PREFS_KEY = "authentication_preference"
        private const val PREF_AUTHENTICATION_STATUS = "PREF_AUTHENTICATION_STATUS"
        private const val PREF_CURRENT_USER = "PREF_CURRENT_USER"

        private val prefAuthentication = booleanPreferencesKey(PREF_AUTHENTICATION_STATUS)
        private val prefCurrentUser = stringPreferencesKey(PREF_CURRENT_USER)
    }
}