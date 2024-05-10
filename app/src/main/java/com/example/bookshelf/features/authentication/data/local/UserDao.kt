package com.example.bookshelf.features.authentication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookshelf.features.authentication.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getAllUser(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}