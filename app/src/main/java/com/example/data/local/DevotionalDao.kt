package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DevotionalDao {

    // Favorites
    @Query("SELECT * FROM favorites ORDER BY addedTimestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)

    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: String)

    // Jap Mala
    @Query("SELECT * FROM jap_mala_records ORDER BY dateTimestamp DESC")
    fun getAllMalaRecords(): Flow<List<JapMalaRecordEntity>>

    @Query("SELECT SUM(roundsCompleted) FROM jap_mala_records")
    fun getTotalLifetimeRounds(): Flow<Int?>

    @Query("SELECT SUM(beadsCompleted) FROM jap_mala_records")
    fun getTotalLifetimeBeads(): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMalaRecord(record: JapMalaRecordEntity)

    @Query("DELETE FROM jap_mala_records")
    suspend fun deleteAllMalaRecords()

    @Query("DELETE FROM favorites")
    suspend fun deleteAllFavorites()

    @Query("DELETE FROM bookmarks")
    suspend fun deleteAllBookmarks()

    // Recent Plays
    @Query("SELECT * FROM recent_plays ORDER BY playedTimestamp DESC LIMIT 10")
    fun getRecentPlays(): Flow<List<RecentPlayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentPlay(recentPlay: RecentPlayEntity)
}
