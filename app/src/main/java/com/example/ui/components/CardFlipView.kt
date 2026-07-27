package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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

data class NumberedCard(
    val letter: String,
    val positionNumber: Int
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardFlipView(
    targetTitle: String,
    tiles: List<String>,
    onSolved: () -> Unit
) {
    val cleanTarget = targetTitle.replace(" ", "").uppercase()

    // Create cards with their 1-based order numbers in cleanTarget
    val numberedCards = remember(targetTitle) {
        val targetLetters = cleanTarget.map { it.toString() }
        val list = targetLetters.mapIndexed { index, letter ->
            NumberedCard(letter = letter, positionNumber = index + 1)
        }
        // Shuffle card positions on screen
        list.shuffled()
    }

    val flippedIndices = remember { mutableStateListOf<Int>() }
    var typedAnswer by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    fun normalize(s: String) = s.replace(" ", "").replace("'", "").replace(".", "").uppercase()

    fun toggleCard(index: Int) {
        if (isSuccess) return
        if (flippedIndices.contains(index)) {
            flippedIndices.remove(index)
        } else {
            flippedIndices.add(index)
        }
    }

    fun submitAnswer() {
        if (isSuccess) return
        if (normalize(typedAnswer) == cleanTarget) {
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
            .testTag("card_flip_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FLIP TILES TO REVEAL NUMBERED LETTERS",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            ),
            color = StageGold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Flip cards to find letter positions (#1, #2...), then type the word!",
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cards Grid
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            maxItemsInEachRow = 4
        ) {
            numberedCards.forEachIndexed { index, card ->
                val isFlipped = flippedIndices.contains(index)
                val rotation by animateFloatAsState(
                    targetValue = if (isFlipped) 180f else 0f,
                    animationSpec = tween(durationMillis = 350),
                    label = "cardFlip"
                )

                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(64.dp)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12 * density
                        }
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isFlipped) DarkBurgundy else VelvetCharcoal)
                        .border(
                            2.dp,
                            if (isFlipped) StageGold else StageGold.copy(alpha = 0.4f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { toggleCard(index) }
                        .testTag("flip_card_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        Text(
                            text = "?",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = StageGold.copy(alpha = 0.6f)
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.graphicsLayer { rotationY = 180f }
                        ) {
                            Text(
                                text = card.letter,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = StageGold
                            )
                            Text(
                                text = "#${card.positionNumber}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Numbered Grid Input below flip cards
        NumberedGridInputView(
            targetTitle = targetTitle,
            onSolved = onSolved
        )
    }
}
