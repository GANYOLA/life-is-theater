package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GameRepository(
    private val levelDao: LevelDao,
    private val easterEggDao: EasterEggDao
) {
    val levelProgressFlow: Flow<List<LevelProgressEntity>> = levelDao.getAllLevelProgress()
    val easterEggsFlow: Flow<List<EasterEggEntity>> = easterEggDao.getAllEasterEggs()

    suspend fun initializeDefaultDataIfNeeded() {
        val currentLevels = levelDao.getAllLevelProgress().first()
        if (currentLevels.isEmpty()) {
            val initialLevels = AlbumTracklist.levels.map { levelData ->
                LevelProgressEntity(
                    levelNumber = levelData.levelNumber,
                    trackTitle = levelData.trackTitle,
                    isUnlocked = true, // All levels available immediately!
                    isCompleted = false,
                    starsEarned = 0,
                    hintsUsed = 0,
                    solvedTimeMillis = 0L
                )
            }
            levelDao.insertAll(initialLevels)
        } else {
            val lockedLevels = currentLevels.filter { !it.isUnlocked }
            if (lockedLevels.isNotEmpty()) {
                val unlocked = currentLevels.map { it.copy(isUnlocked = true) }
                levelDao.insertAll(unlocked)
            }
        }

        val currentEasterEggs = easterEggDao.getAllEasterEggs().first()
        if (currentEasterEggs.size < 13) {
            val defaultEggs = listOf(
                EasterEggEntity(
                    keyId = "COVER_RAPID_TAP",
                    title = "The Backstage VIP Pass",
                    description = "Rapidly tapped 5 times on the Title Header.",
                    secretLore = "Liner Note Secret: 'Life is Theater' was recorded during stormy nights in a dimly lit theater attic using vintage tape delay.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "COVER_RAPID_TAP" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "VINYL_BACKWARD_SPIN",
                    title = "Reverse Vinyl Frequency",
                    description = "Spun the vinyl record counter-clockwise in the track visualizer.",
                    secretLore = "Hidden Audio Note: Reverse listening reveals backwards vocal whispers in Track 13: 'The curtain never truly falls.'",
                    isDiscovered = currentEasterEggs.find { it.keyId == "VINYL_BACKWARD_SPIN" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "KONAMI_VAULT_CODE",
                    title = "Vault Cipher Master",
                    description = "Entered the secret master cipher 'THEATER' into the Easter Egg Vault.",
                    secretLore = "Unreleased Acoustic Demo: Track 11 'High Heels Make Men Fall Silent' originally featured an acoustic cello prelude.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "KONAMI_VAULT_CODE" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "LILITH_GOTHIC_TAP",
                    title = "Gothic Rune Whisper",
                    description = "Tapped Lilith's ancient rune cipher multiple times.",
                    secretLore = "Mythic Note: Lilith represents the untamed shadow self that refuses to wear a stage mask.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "LILITH_GOTHIC_TAP" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "HINT_STAR_BUYER",
                    title = "Star Hint Alchemist",
                    description = "Exchanged earned stars for extra track puzzle hints in the Easter Egg Vault.",
                    secretLore = "Star Note: Every star spent shines light on the stage's deepest mysteries.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "HINT_STAR_BUYER" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "TRACK_1_SOLVED",
                    title = "Track 1 Secret: The Opening Night",
                    description = "Unmasked Track 1 mini-game.",
                    secretLore = "Track 1 Lore: The overture string section was recorded live in an abandoned velvet orchestra pit.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "TRACK_1_SOLVED" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "TRACK_2_SOLVED",
                    title = "Track 2 Secret: High Heels",
                    description = "Unmasked Track 2 card match memory game.",
                    secretLore = "Track 2 Lore: The rhythm track uses sampled footsteps echoing down a marble hallway at midnight.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "TRACK_2_SOLVED" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "TRACK_3_SOLVED",
                    title = "Track 3 Secret: Royal Majesty",
                    description = "Unmasked Track 3 crossword cipher.",
                    secretLore = "Track 3 Lore: Inspired by 17th century theatrical royalty court plays.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "TRACK_3_SOLVED" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "TRACK_4_SOLVED",
                    title = "Track 4 Secret: Smoke & Mirrors",
                    description = "Unmasked Track 4 audio frequency puzzle.",
                    secretLore = "Track 4 Lore: Utilizes binaural audio phase shifts to create mirror sound effects.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "TRACK_4_SOLVED" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "TRACK_5_SOLVED",
                    title = "Track 5 Secret: Velvet Solitude",
                    description = "Unmasked Track 5 lantern link puzzle.",
                    secretLore = "Track 5 Lore: Written in solitude backstage during an unexpected power outage.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "TRACK_5_SOLVED" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "TRACK_6_SOLVED",
                    title = "Track 6 Secret: Masquerade Mask",
                    description = "Unmasked Track 6 mini-game.",
                    secretLore = "Track 6 Lore: Features Venetian brass harmonies and harpsichord arpeggios.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "TRACK_6_SOLVED" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "TRACK_7_SOLVED",
                    title = "Track 7 Secret: Spotlight Soliloquy",
                    description = "Unmasked Track 7 mini-game.",
                    secretLore = "Track 7 Lore: A lone solo vocal performance recorded under a single warm spotlight.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "TRACK_7_SOLVED" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "TRACK_13_SOLVED",
                    title = "Grand Finale Secret: Life is Theater",
                    description = "Unmasked Track 13 album title climax mini-game.",
                    secretLore = "Grand Finale Lore: The final curtain call where all instruments merge into one triumphant crescendo.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "TRACK_13_SOLVED" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "STAR_COLLECTOR_10",
                    title = "Starlight Apprentice",
                    description = "Earned 10 total stars across album tracks.",
                    secretLore = "Starlight Note: The first ten stars ignite the stage footlights, illuminating hidden theater secrets.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "STAR_COLLECTOR_10" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "STAR_COLLECTOR_25",
                    title = "Constellation Virtuoso",
                    description = "Earned 25 total stars across album tracks.",
                    secretLore = "Constellation Lore: 25 stars form the shape of a golden crown over the grand balcony.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "STAR_COLLECTOR_25" }?.isDiscovered ?: false
                ),
                EasterEggEntity(
                    keyId = "PERFECT_SCORE_TROPHY",
                    title = "Golden Spotlight Maestro",
                    description = "Complete all 13 album track mini-games with 3 stars.",
                    secretLore = "Album Master Lore: You have unmasked the entire 13-track album! The theater belongs to you.",
                    isDiscovered = currentEasterEggs.find { it.keyId == "PERFECT_SCORE_TROPHY" }?.isDiscovered ?: false
                )
            )
            easterEggDao.insertAll(defaultEggs)
        }
    }

    suspend fun completeLevel(levelNumber: Int, stars: Int, hintsUsed: Int, solvedTimeMillis: Long) {
        val existing = levelDao.getLevelByNumber(levelNumber) ?: return
        val updated = existing.copy(
            isCompleted = true,
            starsEarned = maxOf(existing.starsEarned, stars),
            hintsUsed = hintsUsed,
            solvedTimeMillis = if (existing.solvedTimeMillis == 0L) solvedTimeMillis else minOf(existing.solvedTimeMillis, solvedTimeMillis)
        )
        levelDao.updateLevel(updated)

        // Unlock next level if available
        if (levelNumber < 13) {
            val nextLevel = levelDao.getLevelByNumber(levelNumber + 1)
            if (nextLevel != null && !nextLevel.isUnlocked) {
                levelDao.updateLevel(nextLevel.copy(isUnlocked = true))
            }
        }

        // Unlock corresponding level Easter egg if exists
        unlockEasterEgg("TRACK_${levelNumber}_SOLVED")

        // Check cumulative star achievements for Easter egg system
        val allLevels = levelDao.getAllLevelProgress().first()
        val totalStars = allLevels.sumOf { it.starsEarned }
        if (totalStars >= 10) {
            unlockEasterEgg("STAR_COLLECTOR_10")
        }
        if (totalStars >= 25) {
            unlockEasterEgg("STAR_COLLECTOR_25")
        }
        if (allLevels.all { it.isCompleted && it.starsEarned >= 3 }) {
            unlockEasterEgg("PERFECT_SCORE_TROPHY")
        }
    }

    suspend fun unlockEasterEgg(keyId: String): Boolean {
        val egg = easterEggDao.getEasterEggByKey(keyId) ?: return false
        if (!egg.isDiscovered) {
            easterEggDao.updateEasterEgg(
                egg.copy(
                    isDiscovered = true,
                    discoveredTimestamp = System.currentTimeMillis()
                )
            )
            return true
        }
        return false
    }

    suspend fun resetLevelProgress(levelNumber: Int) {
        val existing = levelDao.getLevelByNumber(levelNumber) ?: return
        val reset = existing.copy(
            isCompleted = false,
            starsEarned = 0,
            hintsUsed = 0,
            solvedTimeMillis = 0L
        )
        levelDao.updateLevel(reset)
    }

    suspend fun resetAllProgress() {
        levelDao.deleteAll()
        easterEggDao.deleteAll()
        initializeDefaultDataIfNeeded()
    }
}
