package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EasterEggDao {
    @Query("SELECT * FROM easter_eggs ORDER BY title ASC")
    fun getAllEasterEggs(): Flow<List<EasterEggEntity>>

    @Query("SELECT * FROM easter_eggs WHERE keyId = :keyId")
    suspend fun getEasterEggByKey(keyId: String): EasterEggEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(easterEggs: List<EasterEggEntity>)

    @Update
    suspend fun updateEasterEgg(easterEgg: EasterEggEntity)

    @Query("DELETE FROM easter_eggs")
    suspend fun deleteAll()
}
