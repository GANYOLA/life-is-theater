package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SynthAudioPlayer
import com.example.data.AlbumTracklist
import com.example.data.AppDatabase
import com.example.data.EasterEggEntity
import com.example.data.GameRepository
import com.example.data.LevelData
import com.example.data.LevelProgressEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object LevelSelection : Screen()
    data class Game(val levelNumber: Int) : Screen()
    data class TrackDetail(val levelNumber: Int) : Screen()
    object EasterEggVault : Screen()
}

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = GameRepository(database.levelDao(), database.easterEggDao())
    val audioPlayer = SynthAudioPlayer()

    val levelProgressList: StateFlow<List<LevelProgressEntity>> = repository.levelProgressFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val easterEggList: StateFlow<List<EasterEggEntity>> = repository.easterEggsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isAudioPlaying: StateFlow<Boolean> = audioPlayer.isPlaying

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _newlyDiscoveredEgg = MutableStateFlow<EasterEggEntity?>(null)
    val newlyDiscoveredEgg: StateFlow<EasterEggEntity?> = _newlyDiscoveredEgg.asStateFlow()

    private var coverTapCount = 0
    private var coverTapLastTime = 0L

    init {
        viewModelScope.launch {
            repository.initializeDefaultDataIfNeeded()
        }
    }

    fun stopAudio() {
        audioPlayer.stopAudio()
    }

    fun navigateTo(screen: Screen) {
        audioPlayer.stopAudio()
        _currentScreen.value = screen
    }

    fun dismissNewlyDiscoveredEgg() {
        _newlyDiscoveredEgg.value = null
    }

    fun onAlbumCoverTapped() {
        val now = System.currentTimeMillis()
        if (now - coverTapLastTime < 1000) {
            coverTapCount++
        } else {
            coverTapCount = 1
        }
        coverTapLastTime = now

        if (coverTapCount >= 5) {
            coverTapCount = 0
            triggerEasterEggUnlock("COVER_RAPID_TAP")
        }
    }

    fun onVinylSpunCounterClockwise() {
        audioPlayer.playReverseEffect()
        triggerEasterEggUnlock("VINYL_BACKWARD_SPIN")
    }

    fun checkVaultSecretCode(code: String): Boolean {
        val trimmed = code.trim()
        if (trimmed.equals("THEATER", ignoreCase = true) ||
            trimmed.equals("THEATRE", ignoreCase = true) ||
            trimmed.equals("FEAR", ignoreCase = true)
        ) {
            triggerEasterEggUnlock("KONAMI_VAULT_CODE")
            return true
        }
        return false
    }

    fun onLilithRuneTapped() {
        triggerEasterEggUnlock("LILITH_GOTHIC_TAP")
    }

    private fun triggerEasterEggUnlock(keyId: String) {
        viewModelScope.launch {
            val unlocked = repository.unlockEasterEgg(keyId)
            if (unlocked) {
                audioPlayer.playEasterEggChime()
                val egg = easterEggList.value.find { it.keyId == keyId }
                if (egg != null) {
                    _newlyDiscoveredEgg.value = egg
                }
            }
        }
    }

    private val _purchasedHintsCount = MutableStateFlow(0)
    val purchasedHintsCount: StateFlow<Int> = _purchasedHintsCount.asStateFlow()

    private val _unlockedTopics = MutableStateFlow<Set<Int>>(emptySet())
    val unlockedTopics: StateFlow<Set<Int>> = _unlockedTopics.asStateFlow()

    fun buyHintWithStar(): Boolean {
        val totalStars = levelProgressList.value.sumOf { it.starsEarned }
        val spentOnTopics = _unlockedTopics.value.size * 2
        val remainingStarsAvailable = totalStars - _purchasedHintsCount.value - spentOnTopics
        if (remainingStarsAvailable >= 1) {
            _purchasedHintsCount.value += 1
            triggerEasterEggUnlock("HINT_STAR_BUYER")
            return true
        }
        return false
    }

    fun revealTopicForTrack(levelNumber: Int): Boolean {
        if (_unlockedTopics.value.contains(levelNumber)) return true
        val totalStars = levelProgressList.value.sumOf { it.starsEarned }
        val spentOnHints = _purchasedHintsCount.value
        val spentOnTopics = _unlockedTopics.value.size * 2
        val availableStars = totalStars - spentOnHints - spentOnTopics
        if (availableStars >= 2) {
            _unlockedTopics.value = _unlockedTopics.value + levelNumber
            audioPlayer.playEasterEggChime()
            return true
        }
        return false
    }

    fun completeLevel(levelNumber: Int, starsEarned: Int, hintsUsed: Int, timeElapsedMillis: Long) {
        viewModelScope.launch {
            audioPlayer.playVictoryFanfare()
            repository.completeLevel(levelNumber, starsEarned, hintsUsed, timeElapsedMillis)
        }
    }

    fun getLevelData(levelNumber: Int): LevelData {
        return AlbumTracklist.levels.find { it.levelNumber == levelNumber }
            ?: AlbumTracklist.levels.first()
    }

    fun playTrackAudioTeaser(levelNumber: Int) {
        val data = getLevelData(levelNumber)
        audioPlayer.playAudioTeaserUrl(data.audioTeaserUrl, data.audioFrequencies)
    }

    fun resetLevelProgress(levelNumber: Int) {
        viewModelScope.launch {
            repository.resetLevelProgress(levelNumber)
        }
    }

    fun resetGameProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
            _currentScreen.value = Screen.Home
        }
    }
}
