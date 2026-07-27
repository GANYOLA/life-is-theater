package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.EasterEggDialog
import com.example.ui.screens.EasterEggVaultScreen
import com.example.ui.screens.GameScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LevelSelectionScreen
import com.example.ui.screens.TrackDetailScreen
import com.example.ui.theme.LifeIsTheaterTheme
import com.example.ui.theme.TheaterBackground
import com.example.ui.viewmodel.GameViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifeIsTheaterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = TheaterBackground
                ) {
                    LifeIsTheaterApp()
                }
            }
        }
    }
}

@Composable
fun LifeIsTheaterApp(viewModel: GameViewModel = viewModel()) {
    val levelProgressList by viewModel.levelProgressList.collectAsStateWithLifecycle()
    val easterEggList by viewModel.easterEggList.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val newlyDiscoveredEgg by viewModel.newlyDiscoveredEgg.collectAsStateWithLifecycle()
    val purchasedHints by viewModel.purchasedHintsCount.collectAsStateWithLifecycle()
    val isAudioPlaying by viewModel.isAudioPlaying.collectAsStateWithLifecycle()

    val totalStars = levelProgressList.sumOf { it.starsEarned }

    // Easter egg unlock popup
    newlyDiscoveredEgg?.let { egg ->
        EasterEggDialog(
            egg = egg,
            onDismiss = { viewModel.dismissNewlyDiscoveredEgg() }
        )
    }

    when (val screen = currentScreen) {
        is Screen.Home -> {
            HomeScreen(
                levelProgressList = levelProgressList,
                easterEggList = easterEggList,
                onPlayClick = { viewModel.navigateTo(Screen.LevelSelection) },
                onTracklistClick = { viewModel.navigateTo(Screen.LevelSelection) },
                onVaultClick = { viewModel.navigateTo(Screen.EasterEggVault) },
                onAlbumCoverTapped = { viewModel.onAlbumCoverTapped() },
                onResetAllProgress = { viewModel.resetGameProgress() }
            )
        }

        is Screen.LevelSelection -> {
            LevelSelectionScreen(
                levelProgressList = levelProgressList,
                onBackClick = { viewModel.navigateTo(Screen.Home) },
                onSelectLevel = { levelNum -> viewModel.navigateTo(Screen.Game(levelNum)) },
                onViewTrackDetail = { levelNum -> viewModel.navigateTo(Screen.TrackDetail(levelNum)) },
                onResetLevel = { levelNum -> viewModel.resetLevelProgress(levelNum) }
            )
        }

        is Screen.Game -> {
            val levelData = viewModel.getLevelData(screen.levelNumber)
            GameScreen(
                levelData = levelData,
                totalStars = totalStars,
                purchasedHints = purchasedHints,
                isAudioPlaying = isAudioPlaying,
                onBuyHintWithStar = { viewModel.buyHintWithStar() },
                onBackClick = { viewModel.navigateTo(Screen.LevelSelection) },
                onCompleteLevel = { stars, hintsUsed, timeTakenMillis ->
                    viewModel.completeLevel(screen.levelNumber, stars, hintsUsed, timeTakenMillis)
                },
                onPlayAudioTeaser = { viewModel.playTrackAudioTeaser(screen.levelNumber) },
                onStopAudio = { viewModel.stopAudio() },
                onViewDetailClick = { viewModel.navigateTo(Screen.TrackDetail(screen.levelNumber)) },
                onNextLevelClick = {
                    if (screen.levelNumber < 13) {
                        viewModel.navigateTo(Screen.Game(screen.levelNumber + 1))
                    } else {
                        viewModel.navigateTo(Screen.LevelSelection)
                    }
                },
                onLilithRuneTapped = { viewModel.onLilithRuneTapped() },
                onResetLevel = { levelNum -> viewModel.resetLevelProgress(levelNum) }
            )
        }

        is Screen.TrackDetail -> {
            val levelData = viewModel.getLevelData(screen.levelNumber)
            val levelProgress = levelProgressList.find { it.levelNumber == screen.levelNumber }
            TrackDetailScreen(
                levelData = levelData,
                levelProgress = levelProgress,
                onBackClick = { viewModel.navigateTo(Screen.LevelSelection) },
                onPlayAudioTeaser = { viewModel.playTrackAudioTeaser(screen.levelNumber) },
                onReverseSpin = { viewModel.onVinylSpunCounterClockwise() }
            )
        }

        is Screen.EasterEggVault -> {
            val unlockedTopics by viewModel.unlockedTopics.collectAsStateWithLifecycle()
            EasterEggVaultScreen(
                totalStars = totalStars,
                purchasedHints = purchasedHints,
                unlockedTopics = unlockedTopics,
                onRevealTopic = { levelNum -> viewModel.revealTopicForTrack(levelNum) },
                onBackClick = { viewModel.navigateTo(Screen.Home) }
            )
        }
    }
}

