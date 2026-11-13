package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val titleGujarati: String,
    val category: String,
    val subtitle: String,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String,
    val scriptureId: String,
    val chapterIndex: Int,
    val verseIndex: Int,
    val titleGujarati: String,
    val snippetGujarati: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "jap_mala_records")
data class JapMalaRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mantraName: String,
    val beadsCompleted: Int,
    val roundsCompleted: Int,
    val dateTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "recent_plays")
data class RecentPlayEntity(
    @PrimaryKey val id: String,
    val titleGujarati: String,
    val category: String,
    val deityName: String,
    val durationSeconds: Int,
    val playedTimestamp: Long = System.currentTimeMillis()
)
