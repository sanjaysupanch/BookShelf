package com.example.bookshelf.features.common.local

import android.content.Context

interface BookShelfDataStoreManager {

    fun setLoginStatus(isLogin: Boolean)

    fun getLoginStatus(): Boolean

    fun setCurrentUser(user: String)

    fun getCurrentUser(): String

    fun clearSession()

    companion object {
        private lateinit var INSTANCE: BookShelfDataStoreManager

        @JvmStatic
        fun get(context: Context): BookShelfDataStoreManager {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not())
                    INSTANCE = BookShelfDataStoreManagerImpl(context)

                return INSTANCE
            }
        }
    }
}