package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "easter_eggs")
data class EasterEggEntity(
    @PrimaryKey val keyId: String,
    val title: String,
    val description: String,
    val secretLore: String,
    val isDiscovered: Boolean = false,
    val discoveredTimestamp: Long = 0L
)
