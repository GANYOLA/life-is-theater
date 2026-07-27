package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelDao {
    @Query("SELECT * FROM level_progress ORDER BY levelNumber ASC")
    fun getAllLevelProgress(): Flow<List<LevelProgressEntity>>

    @Query("SELECT * FROM level_progress WHERE levelNumber = :levelNumber")
    suspend fun getLevelByNumber(levelNumber: Int): LevelProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(levels: List<LevelProgressEntity>)

    @Update
    suspend fun updateLevel(level: LevelProgressEntity)

    @Query("UPDATE level_progress SET isUnlocked = 1 WHERE levelNumber = :levelNumber")
    suspend fun unlockLevel(levelNumber: Int)

    @Query("DELETE FROM level_progress")
    suspend fun deleteAll()
}
