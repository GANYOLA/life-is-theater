package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GridOn
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
fun NumberedGridInputView(
    targetTitle: String,
    onSolved: () -> Unit
) {
    var typedText by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    // Split title into clean uppercase words (e.g. ["THE", "OPENING", "NIGHT"])
    val wordList = remember(targetTitle) {
        targetTitle.trim()
            .split("\\s+".toRegex())
            .map { word -> word.filter { it.isLetter() }.uppercase() }
            .filter { it.isNotEmpty() }
    }

    val wordLengths = remember(wordList) { wordList.map { it.length } }
    val totalLength = remember(wordLengths) { wordLengths.sum() }
    val cleanTarget = remember(wordList) { wordList.joinToString("") }

    fun normalize(s: String) = s.replace(" ", "").replace("'", "").uppercase()

    fun verify() {
        if (isSuccess) return
        if (normalize(typedText) == cleanTarget) {
            isSuccess = true
            isError = false
            onSolved()
        } else {
            isError = true
        }
    }

    // Build letter count label (e.g. "3 words • 15 letters (3, 7, 5)" or "1 word • 8 letters")
    val countLabel = remember(wordList, wordLengths, totalLength) {
        if (wordList.size == 1) {
            "1 word • $totalLength letters"
        } else {
            val lengthsStr = wordLengths.joinToString(", ")
            "${wordList.size} words • $totalLength letters ($lengthsStr)"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("numbered_grid_input_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.GridOn,
                contentDescription = null,
                tint = StageGold,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "NUMBERED WORD GRIDS",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = StageGold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            color = DarkBurgundy,
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StageGold.copy(alpha = 0.5f))
        ) {
            Text(
                text = countLabel,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = StageGold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Type the letters in numbered order (1 to $totalLength) to fill the grid:",
            style = MaterialTheme.typography.bodySmall,
            color = IvoryText.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Render a separate grid row for each word
        val currentLetters = normalize(typedText).padEnd(totalLength, ' ')
        var globalIndex = 0

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            wordList.forEachIndexed { wordIdx, word ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (wordList.size > 1) {
                        Text(
                            text = "WORD ${wordIdx + 1} (${word.length} LETTERS)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = StageGold.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until word.length) {
                            val boxNum = globalIndex + 1
                            val letterChar = if (globalIndex < typedText.length) currentLetters[globalIndex] else ' '
                            val isFilled = globalIndex < typedText.length

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(34.dp)
                            ) {
                                Text(
                                    text = "$boxNum",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = StageGold.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when {
                                                isSuccess -> Color(0xFF2E7D32)
                                                isFilled -> DarkBurgundy
                                                else -> VelvetCharcoal
                                            }
                                        )
                                        .border(
                                            1.5.dp,
                                            when {
                                                isSuccess -> UnlockedGreen
                                                isFilled -> StageGold
                                                else -> StageGold.copy(alpha = 0.3f)
                                            },
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (letterChar != ' ') "$letterChar" else "",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.ExtraBold
                                        ),
                                        color = if (isSuccess) Color.White else IvoryText
                                    )
                                }
                            }

                            globalIndex++
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isSuccess) {
            OutlinedTextField(
                value = typedText,
                onValueChange = { input ->
                    if (input.replace(" ", "").length <= totalLength) {
                        typedText = input
                        isError = false
                        if (normalize(input) == cleanTarget) {
                            verify()
                        }
                    }
                },
                placeholder = { Text("Type answer in numbered order...", color = Color.Gray, fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("numbered_grid_text_input"),
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
                    .height(44.dp)
                    .testTag("numbered_grid_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = StageGold, contentColor = Color.Black),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("SUBMIT WORD GRID", fontWeight = FontWeight.Bold)
            }

            if (isError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Incorrect! Type all $totalLength letters in numbered order.",
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
                        text = "PUZZLE SOLVED!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}
