package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
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

enum class LetterStatus {
    UNKNOWN,
    ABSENT,  // Dark gray
    PRESENT, // Yellow/Gold
    CORRECT  // Green
}

data class GuessRow(
    val letters: String = "",
    val isSubmitted: Boolean = false
)

@Composable
fun WordleGuessView(
    targetWord: String = "LILITH",
    maxAttempts: Int = 6,
    onSolved: () -> Unit
) {
    val wordLength = targetWord.uppercase().length
    val target = targetWord.uppercase()

    var attempts by remember { mutableStateOf(List(maxAttempts) { GuessRow() }) }
    var currentAttemptIndex by remember { mutableIntStateOf(0) }
    var currentInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isGameOver by remember { mutableStateOf(false) }

    // Map of letter to highest status
    val keyStatuses = remember(attempts) {
        val map = mutableMapOf<Char, LetterStatus>()
        attempts.filter { it.isSubmitted }.forEach { row ->
            row.letters.forEachIndexed { i, c ->
                val currentBest = map.getOrDefault(c, LetterStatus.UNKNOWN)
                val newStatus = when {
                    i < target.length && target[i] == c -> LetterStatus.CORRECT
                    target.contains(c) -> LetterStatus.PRESENT
                    else -> LetterStatus.ABSENT
                }
                if (newStatus == LetterStatus.CORRECT || (newStatus == LetterStatus.PRESENT && currentBest != LetterStatus.CORRECT) || currentBest == LetterStatus.UNKNOWN) {
                    map[c] = newStatus
                }
            }
        }
        map
    }

    fun submitGuess() {
        if (currentInput.length != wordLength) {
            errorMessage = "Enter a $wordLength-letter word!"
            return
        }

        errorMessage = null
        val newRow = GuessRow(letters = currentInput.uppercase(), isSubmitted = true)
        val updatedAttempts = attempts.toMutableList()
        updatedAttempts[currentAttemptIndex] = newRow
        attempts = updatedAttempts

        if (currentInput.uppercase() == target) {
            isGameOver = true
            onSolved()
        } else if (currentAttemptIndex >= maxAttempts - 1) {
            isGameOver = true
            errorMessage = "Out of tries! Target was: $target"
        } else {
            currentAttemptIndex++
            currentInput = ""
        }
    }

    fun onKeyPress(char: Char) {
        if (isGameOver) return
        if (currentInput.length < wordLength) {
            currentInput += char.uppercaseChar()
            errorMessage = null
        }
    }

    fun onDeletePress() {
        if (isGameOver) return
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            errorMessage = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("wordle_guess_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "WORDLE CIPHER",
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            color = Color(0xFFFFD700)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Wordle Grid (6 rows)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (rowIndex in 0 until maxAttempts) {
                val row = attempts[rowIndex]
                val isCurrentRow = rowIndex == currentAttemptIndex && !isGameOver

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (colIndex in 0 until wordLength) {
                        val letterChar = when {
                            row.isSubmitted -> row.letters.getOrNull(colIndex)
                            isCurrentRow -> currentInput.getOrNull(colIndex)
                            else -> null
                        }

                        val status = when {
                            !row.isSubmitted -> LetterStatus.UNKNOWN
                            colIndex < target.length && letterChar == target[colIndex] -> LetterStatus.CORRECT
                            letterChar != null && target.contains(letterChar) -> LetterStatus.PRESENT
                            else -> LetterStatus.ABSENT
                        }

                        val bgColor = when (status) {
                            LetterStatus.CORRECT -> Color(0xFF2E7D32) // Green
                            LetterStatus.PRESENT -> Color(0xFFC6A700) // Gold
                            LetterStatus.ABSENT -> Color(0xFF37474F)  // Dark Gray
                            LetterStatus.UNKNOWN -> if (isCurrentRow && letterChar != null) Color(0xFF4A148C) else Color(0xFF1E1024)
                        }

                        val borderColor = when {
                            status != LetterStatus.UNKNOWN -> Color.Transparent
                            isCurrentRow && letterChar != null -> Color(0xFFFFD700)
                            else -> Color(0xFF5D4037)
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(bgColor)
                                .border(1.5.dp, borderColor, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letterChar?.toString() ?: "",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        errorMessage?.let { msg ->
            Text(
                text = msg,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = if (isGameOver && !attempts.any { it.letters == target }) Color(0xFFFF5252) else Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Onscreen Keyboard
        val keyboardRows = listOf(
            listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'),
            listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'),
            listOf('Z', 'X', 'C', 'V', 'B', 'N', 'M')
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            keyboardRows.forEachIndexed { rowIndex, keys ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (rowIndex == 2) {
                        // Enter Key
                        Box(
                            modifier = Modifier
                                .height(42.dp)
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF8E24AA))
                                .clickable { submitGuess() }
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "ENTER",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }

                    keys.forEach { char ->
                        val status = keyStatuses[char] ?: LetterStatus.UNKNOWN
                        val keyBg = when (status) {
                            LetterStatus.CORRECT -> Color(0xFF2E7D32)
                            LetterStatus.PRESENT -> Color(0xFFC6A700)
                            LetterStatus.ABSENT -> Color(0xFF263238)
                            LetterStatus.UNKNOWN -> Color(0xFF424242)
                        }

                        Box(
                            modifier = Modifier
                                .size(width = 28.dp, height = 42.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(keyBg)
                                .clickable { onKeyPress(char) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char.toString(),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }

                    if (rowIndex == 2) {
                        // Backspace Key
                        Box(
                            modifier = Modifier
                                .height(42.dp)
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF5D4037))
                                .clickable { onDeletePress() }
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
