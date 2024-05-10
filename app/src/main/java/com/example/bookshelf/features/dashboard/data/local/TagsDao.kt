package com.example.bookshelf.features.dashboard.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookshelf.features.dashboard.data.model.TagsInfo

@Dao
interface TagsDao {

    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<TagsInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTags(tags: TagsInfo)
}