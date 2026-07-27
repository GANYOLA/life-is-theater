package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.StageSurface
import com.example.ui.theme.UnlockedGreen
import com.example.ui.theme.VelvetCharcoal
import com.example.ui.theme.VelvetCrimson

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LetterUnscrambleView(
    targetAnswer: String,
    scrambledTiles: List<String>,
    onSolved: () -> Unit
) {
    fun normalize(str: String) = str.replace(" ", "").replace("'", "").replace(".", "").uppercase()

    val cleanTarget = normalize(targetAnswer)
    val availableTiles = remember(targetAnswer, scrambledTiles) {
        mutableStateListOf<Pair<Int, String>>().apply {
            scrambledTiles.forEachIndexed { index, letter ->
                add(Pair(index, letter.uppercase()))
            }
        }
    }

    val selectedTiles = remember { mutableStateListOf<Pair<Int, String>>() }
    var typedText by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    fun resetAll() {
        if (isSuccess) return
        typedText = ""
        isError = false
        availableTiles.clear()
        scrambledTiles.forEachIndexed { index, letter ->
            availableTiles.add(Pair(index, letter.uppercase()))
        }
        selectedTiles.clear()
    }

    LaunchedEffect(targetAnswer, scrambledTiles) {
        typedText = ""
        isError = false
        isSuccess = false
        availableTiles.clear()
        scrambledTiles.forEachIndexed { index, letter ->
            availableTiles.add(Pair(index, letter.uppercase()))
        }
        selectedTiles.clear()
    }

    fun syncTilesFromText(input: String) {
        if (isSuccess) return
        isError = false
        val cleanInput = normalize(input)

        // Reset tiles state and reconstruct matching tiles from input
        availableTiles.clear()
        val pool = scrambledTiles.mapIndexed { idx, char -> Pair(idx, char.uppercase()) }.toMutableList()
        selectedTiles.clear()

        for (char in cleanInput) {
            val matchIdx = pool.indexOfFirst { it.second == char.toString() }
            if (matchIdx != -1) {
                val matched = pool.removeAt(matchIdx)
                selectedTiles.add(matched)
            }
        }
        availableTiles.addAll(pool)

        if (cleanInput == cleanTarget) {
            isSuccess = true
            onSolved()
        } else if (cleanInput.length >= cleanTarget.length && cleanInput != cleanTarget) {
            isError = true
        }
    }

    fun selectTile(tile: Pair<Int, String>) {
        if (isSuccess) return
        isError = false
        selectedTiles.add(tile)
        availableTiles.remove(tile)
        typedText = selectedTiles.joinToString("") { it.second }

        val currentConstructed = selectedTiles.joinToString("") { it.second }
        if (currentConstructed == cleanTarget) {
            isSuccess = true
            onSolved()
        } else if (currentConstructed.length == cleanTarget.length) {
            isError = true
        }
    }

    fun deselectTileAt(index: Int) {
        if (isSuccess || index >= selectedTiles.size) return
        isError = false
        val tile = selectedTiles.removeAt(index)
        availableTiles.add(tile)
        typedText = selectedTiles.joinToString("") { it.second }
    }

    fun removeLastTile() {
        if (isSuccess || selectedTiles.isEmpty()) return
        deselectTileAt(selectedTiles.size - 1)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("letter_unscramble_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Target Slots / Blanks Display
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            maxItemsInEachRow = 8
        ) {
            val words = targetAnswer.split(" ")
            var globalLetterIdx = 0

            words.forEach { word ->
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    word.forEach { char ->
                        val charStr = char.toString().uppercase()
                        if (charStr == "'" || charStr == ".") {
                            Text(
                                text = charStr,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = StageGold,
                                modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp)
                            )
                        } else if (char.isLetter()) {
                            val slotIdx = globalLetterIdx
                            globalLetterIdx++
                            val filledTile = selectedTiles.getOrNull(slotIdx)?.second ?: ""

                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            isSuccess -> UnlockedGreen.copy(alpha = 0.3f)
                                            isError -> VelvetCrimson.copy(alpha = 0.3f)
                                            filledTile.isNotEmpty() -> DarkBurgundy
                                            else -> VelvetCharcoal
                                        }
                                    )
                                    .border(
                                        1.5.dp,
                                        when {
                                            isSuccess -> UnlockedGreen
                                            isError -> VelvetCrimson
                                            filledTile.isNotEmpty() -> StageGold
                                            else -> StageGold.copy(alpha = 0.3f)
                                        },
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        if (filledTile.isNotEmpty()) {
                                            deselectTileAt(slotIdx)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = filledTile,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    ),
                                    color = if (isSuccess) UnlockedGreen else IvoryText
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Direct Text Input Field
        if (!isSuccess) {
            OutlinedTextField(
                value = typedText,
                onValueChange = {
                    typedText = it
                    syncTilesFromText(it)
                },
                placeholder = { Text("Type track title or tap tiles below...", color = Color.Gray, fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .testTag("unscramble_text_input"),
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

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Status text
        AnimatedVisibility(visible = isError) {
            Text(
                text = "Incorrect sequence! Tap Reset or check your spelling.",
                color = VelvetCrimson,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        AnimatedVisibility(visible = isSuccess) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = UnlockedGreen)
                Text(
                    text = " TRACK TITLE UNLOCKED: ${targetAnswer.uppercase()}!",
                    color = UnlockedGreen,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        // Tappable Available Tiles
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, StageGold.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
            color = StageSurface.copy(alpha = 0.8f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "AVAILABLE LETTER TILES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = StageGold
                )

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 7
                ) {
                    availableTiles.forEach { tile ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkBurgundy)
                                .border(1.dp, StageGold, RoundedCornerShape(10.dp))
                                .clickable { selectTile(tile) }
                                .testTag("letter_tile_${tile.second}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tile.second,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = StageGold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Control buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { removeLastTile() },
                        enabled = (selectedTiles.isNotEmpty() || typedText.isNotEmpty()) && !isSuccess,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = IvoryText),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StageGold.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("backspace_button")
                    ) {
                        Icon(Icons.Default.Backspace, contentDescription = "Backspace", modifier = Modifier.size(18.dp))
                        Text(" Back", fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = { resetAll() },
                        enabled = (selectedTiles.isNotEmpty() || typedText.isNotEmpty() || availableTiles.size != scrambledTiles.size) && !isSuccess,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VelvetCrimson),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VelvetCrimson.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("reset_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(18.dp))
                        Text(" Reset", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

