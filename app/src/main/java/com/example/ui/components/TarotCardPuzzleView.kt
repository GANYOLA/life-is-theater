package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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

data class TarotCardData(
    val cardNumber: String,
    val arcanaName: String,
    val symbolEmoji: String,
    val representedWord: String,
    val description: String
)

@Composable
fun TarotCardPuzzleView(
    targetTitle: String = "Men Don't Cry",
    onSolved: () -> Unit
) {
    var typedText by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var flippedCards by remember { mutableStateOf(setOf<Int>()) }

    val tarotCards = listOf(
        TarotCardData(
            cardNumber = "I",
            arcanaName = "THE GENTLEMEN",
            symbolEmoji = "🤵‍♂️",
            representedWord = "MEN",
            description = "Card of strength, nobility, and stoic brotherhood."
        ),
        TarotCardData(
            cardNumber = "II",
            arcanaName = "THE FORBIDDEN",
            symbolEmoji = "🚫",
            representedWord = "DON'T",
            description = "Card of boundaries, restraint, and unwritten laws."
        ),
        TarotCardData(
            cardNumber = "III",
            arcanaName = "THE TEARDROP",
            symbolEmoji = "💧",
            representedWord = "CRY",
            description = "Card of hidden sorrow, emotion, and silent release."
        )
    )

    fun normalize(s: String) = s.replace(" ", "").replace("'", "").replace("’", "").lowercase()

    fun verifyInput() {
        if (isSuccess) return
        val cleanInput = normalize(typedText)
        val cleanTarget = normalize(targetTitle)
        // Accept "mendontcry" or "mandontcry"
        if (cleanInput == cleanTarget || cleanInput == "mandontcry" || cleanInput == "mendontcry") {
            isSuccess = true
            isError = false
            flippedCards = setOf(0, 1, 2)
            onSolved()
        } else {
            isError = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tarot_card_puzzle_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = StageGold,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "TAROT ARCANA",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = StageGold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "3 words • 10 letters (3, 4, 3)\nTap each Tarot Card to reveal its symbol image:",
            style = MaterialTheme.typography.bodySmall,
            color = IvoryText.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3 Tarot Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tarotCards.forEachIndexed { index, card ->
                val isFlipped = flippedCards.contains(index) || isSuccess

                val rotation by animateFloatAsState(
                    targetValue = if (isFlipped) 180f else 0f,
                    animationSpec = tween(durationMillis = 400),
                    label = "tarot_flip"
                )

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(165.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.5.dp,
                            if (isFlipped) StageGold else StageGold.copy(alpha = 0.4f),
                            RoundedCornerShape(12.dp)
                        )
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clickable {
                            if (!flippedCards.contains(index)) {
                                flippedCards = flippedCards + index
                            }
                        },
                    color = if (isFlipped) DarkBurgundy else StageSurface
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (rotation <= 90f) {
                            // Back of Tarot Card
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "🂠",
                                    fontSize = 32.sp,
                                    color = StageGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "TAROT ${card.cardNumber}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = StageGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = IvoryText.copy(alpha = 0.5f),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        } else {
                            // Front of Tarot Card (Flipped)
                            Column(
                                modifier = Modifier.graphicsLayer { rotationY = 180f },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = card.symbolEmoji,
                                    fontSize = 32.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "ARCANA ${card.cardNumber}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = StageGold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = card.arcanaName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = IvoryText.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
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
                placeholder = { Text("Type full title...", color = Color.Gray, fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tarot_input"),
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
                onClick = { verifyInput() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("tarot_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = StageGold, contentColor = Color.Black),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("SUBMIT TAROT ANSWER", fontWeight = FontWeight.Bold)
            }

            if (isError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Incorrect! Combine the 3 Tarot words to spell the title.",
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
                        text = "TAROT REVEALED: MEN DON'T CRY!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}
