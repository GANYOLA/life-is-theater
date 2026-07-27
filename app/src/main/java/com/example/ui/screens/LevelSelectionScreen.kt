package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AlbumTracklist
import com.example.data.LevelProgressEntity
import com.example.ui.theme.DarkBurgundy
import com.example.ui.theme.IvoryText
import com.example.ui.theme.LockGray
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageSurface
import com.example.ui.theme.TheaterBackground
import com.example.ui.theme.UnlockedGreen
import com.example.ui.theme.VelvetCrimson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectionScreen(
    levelProgressList: List<LevelProgressEntity>,
    onBackClick: () -> Unit,
    onSelectLevel: (Int) -> Unit,
    onViewTrackDetail: (Int) -> Unit,
    onResetLevel: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "13 ALBUM TRACKS & MINI-GAMES",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        ),
                        color = IvoryText
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = StageGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TheaterBackground)
            )
        },
        containerColor = TheaterBackground,
        modifier = Modifier.testTag("level_selection_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(AlbumTracklist.levels, key = { it.levelNumber }) { levelData ->
                val progress = levelProgressList.find { it.levelNumber == levelData.levelNumber }
                val isUnlocked = progress?.isUnlocked == true
                val isCompleted = progress?.isCompleted == true
                val stars = progress?.starsEarned ?: 0

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            when {
                                isCompleted -> UnlockedGreen
                                isUnlocked -> StageGold
                                else -> LockGray
                            },
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            if (isCompleted) {
                                onViewTrackDetail(levelData.levelNumber)
                            } else {
                                onSelectLevel(levelData.levelNumber)
                            }
                        }
                        .testTag("level_card_${levelData.levelNumber}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCompleted) UnlockedGreen.copy(alpha = 0.15f) else StageSurface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Level Badge Number
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isCompleted) UnlockedGreen else VelvetCrimson
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${levelData.levelNumber}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = IvoryText
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "TRACK #${levelData.levelNumber}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = StageGold
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = if (isCompleted) levelData.trackTitle else "??? (Solve to reveal)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isCompleted) IvoryText else StageGold.copy(alpha = 0.9f)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            if (isCompleted) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    repeat(3) { starIdx ->
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = if (starIdx < stars) StageGold else LockGray,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "UNMASKED • View Details",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = UnlockedGreen
                                    )
                                }
                            } else {
                                Text(
                                    text = "Tap to play & solve mini-game",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = StageGold.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { onResetLevel(levelData.levelNumber) },
                                modifier = Modifier.size(36.dp).testTag("reset_level_btn_${levelData.levelNumber}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset Level",
                                    tint = StageGold.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))

                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Completed",
                                    tint = UnlockedGreen,
                                    modifier = Modifier.size(28.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Level",
                                    tint = StageGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
