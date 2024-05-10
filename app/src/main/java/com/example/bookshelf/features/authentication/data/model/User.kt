package com.example.bookshelf.features.authentication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "password") val password: String? = null,
    @ColumnInfo(name = "country") val country: String? = null,
)
