package com.example.bookshelf.features.authentication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookshelf.features.authentication.data.model.User
import com.example.bookshelf.features.dashboard.data.local.TagsDao
import com.example.bookshelf.features.dashboard.data.model.TagsInfo

@Database(entities = [User::class, TagsInfo::class], version = 2)
abstract class UserDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tagsDao(): TagsDao
}