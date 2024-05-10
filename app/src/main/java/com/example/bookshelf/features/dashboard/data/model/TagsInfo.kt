package com.example.bookshelf.features.dashboard.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class TagsInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "bookId") val bookId: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "tag") val tag: String? = null
)