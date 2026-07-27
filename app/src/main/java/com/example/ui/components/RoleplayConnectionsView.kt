package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBurgundy
import com.example.ui.theme.IvoryText
import com.example.ui.theme.StageGold
import com.example.ui.theme.UnlockedGreen
import com.example.ui.theme.VelvetCharcoal
import com.example.ui.theme.VelvetCrimson

@Composable
fun RoleplayConnectionsView(
    targetAnswer: String = "Roleplay",
    onSolved: () -> Unit
) {
    var selectedTiles by remember { mutableStateOf<List<String>>(emptyList()) }
    var isSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Word options to mix
    val wordOptions = remember {
        listOf("ROLE", "PLAY", "ACT", "STAGE", "SCRIPT", "DRAMA")
    }

    fun normalize(s: String) = s.replace(" ", "").lowercase()

    fun handleTileClick(word: String) {
        if (isSuccess) return

        if (selectedTiles.contains(word)) {
            // Deselect
            selectedTiles = selectedTiles - word
            errorMessage = ""
        } else {
            if (selectedTiles.size < 2) {
                val newSelection = selectedTiles + word
                selectedTiles = newSelection

                if (newSelection.size == 2) {
                    val combined = newSelection[0] + newSelection[1]
                    val combinedAlt = newSelection[1] + newSelection[0]

                    if (normalize(combined) == normalize(targetAnswer) || normalize(combinedAlt) == normalize(targetAnswer)) {
                        isSuccess = true
                        errorMessage = ""
                        onSolved()
                    } else {
                        errorMessage = "${newSelection[0]} + ${newSelection[1]} = ${newSelection[0]}${newSelection[1]} (Incorrect pair). Try mixing two different words!"
                    }
                }
            } else {
                // Reset and select clicked tile as first
                selectedTiles = listOf(word)
                errorMessage = ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("roleplay_connections_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.TheaterComedy,
                contentDescription = null,
                tint = StageGold,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "WORD MIXING • ROLEPLAY",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = StageGold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Mix two word tiles together to form the complete track title (No typing required):",
            style = MaterialTheme.typography.bodySmall,
            color = IvoryText.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Selected Mixture Preview Box
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.5.dp, StageGold, RoundedCornerShape(12.dp)),
            color = DarkBurgundy
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val firstWord = selectedTiles.getOrNull(0) ?: "___"
                val secondWord = selectedTiles.getOrNull(1) ?: "___"
                val combinedText = if (selectedTiles.size == 2) "${selectedTiles[0]}${selectedTiles[1]}" else "???"

                Text(
                    text = "$firstWord  +  $secondWord  =  $combinedText",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = if (isSuccess) UnlockedGreen else StageGold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6 Word Tiles Grid (2 columns x 3 rows)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            wordOptions.chunked(2).forEach { rowWords ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowWords.forEach { word ->
                        val isSelected = selectedTiles.contains(word)

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    2.dp,
                                    when {
                                        isSuccess && isSelected -> UnlockedGreen
                                        isSelected -> StageGold
                                        else -> StageGold.copy(alpha = 0.4f)
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { handleTileClick(word) },
                            color = when {
                                isSuccess && isSelected -> Color(0xFF2E7D32)
                                isSelected -> DarkBurgundy
                                else -> VelvetCharcoal
                            }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = if (isSuccess) Color.White else StageGold,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = word,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = if (isSuccess && isSelected) Color.White else IvoryText
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorMessage,
                color = VelvetCrimson,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }

        if (isSuccess) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, UnlockedGreen, RoundedCornerShape(12.dp)),
                color = Color(0xFF2E7D32)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WORDS MIXED: ROLEPLAY SOLVED!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}
