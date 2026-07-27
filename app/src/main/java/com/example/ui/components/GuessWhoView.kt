package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBurgundy
import com.example.ui.theme.IvoryText
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageSurface
import com.example.ui.theme.UnlockedGreen
import com.example.ui.theme.VelvetCharcoal
import com.example.ui.theme.VelvetCrimson

@Composable
fun GuessWhoView(
    targetAnswer: String, // "Hollywood"
    onSolved: () -> Unit
) {
    var typedText by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var revealedClues by remember { mutableStateOf(setOf(0)) }

    val clues = listOf(
        "🎬 Clue #1: Famous boulevard filled with brass-rimmed stars on the sidewalk.",
        "🏆 Clue #2: The global capital of silver screen cinema, Oscars & red carpet glamour.",
        "⛰️ Clue #3: Iconic 45-foot white landmark letters overlooking Los Angeles."
    )

    fun normalize(s: String) = s.replace(" ", "").lowercase()

    fun verify() {
        if (isSuccess) return
        if (normalize(typedText) == normalize(targetAnswer)) {
            isSuccess = true
            isError = false
            onSolved()
        } else {
            isError = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("guess_who_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GUESS WHO?",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            ),
            color = StageGold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Clue Cards Stack
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            clues.forEachIndexed { index, clueText ->
                val isRevealed = revealedClues.contains(index)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            if (isRevealed) StageGold else StageGold.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            if (!isRevealed) {
                                revealedClues = revealedClues + index
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isRevealed) DarkBurgundy else StageSurface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (index == 1) Icons.Default.Movie else Icons.Default.Star,
                            contentDescription = null,
                            tint = StageGold,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        if (isRevealed) {
                            Text(
                                text = clueText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = IvoryText
                            )
                        } else {
                            Text(
                                text = "Tap to reveal Celebrity Clue #${index + 1}...",
                                style = MaterialTheme.typography.bodySmall,
                                color = IvoryText.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isSuccess) {
            OutlinedTextField(
                value = typedText,
                onValueChange = {
                    typedText = it
                    isError = false
                },
                placeholder = { Text("Guess the track title...", color = Color.Gray, fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("guess_who_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StageGold,
                    unfocusedBorderColor = StageGold.copy(alpha = 0.4f),
                    focusedTextColor = IvoryText,
                    unfocusedTextColor = IvoryText,
                    focusedContainerColor = DarkBurgundy,
                    unfocusedContainerColor = VelvetCharcoal
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { verify() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("guess_who_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = StageGold, contentColor = Color.Black),
                shape = RoundedCornerShape(10.dp),
                enabled = typedText.isNotBlank()
            ) {
                Text("SUBMIT GUESS", fontWeight = FontWeight.Bold)
            }

            if (isError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Incorrect! Reveal more clues or check your spelling.",
                    color = VelvetCrimson,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        } else {
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
                        text = "HOLLYWOOD UNLOCKED!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}
