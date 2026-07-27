package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
fun WordSearchView(
    targetTitle: String, // "Forever Silent"
    onSolved: () -> Unit
) {
    // 8x8 Grid with hidden FOREVER and SILENT
    val grid = remember {
        listOf(
            listOf('F', 'O', 'R', 'E', 'V', 'E', 'R', 'A'),
            listOf('X', 'Y', 'Z', 'M', 'N', 'O', 'P', 'B'),
            listOf('S', 'I', 'L', 'E', 'N', 'T', 'Q', 'C'),
            listOf('H', 'J', 'K', 'L', 'U', 'V', 'R', 'D'),
            listOf('F', 'O', 'R', 'E', 'V', 'E', 'R', 'E'),
            listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'F'),
            listOf('S', 'I', 'L', 'E', 'N', 'T', 'H', 'G'),
            listOf('W', 'O', 'R', 'D', 'G', 'R', 'I', 'D')
        )
    }

    var selectedCells by remember { mutableStateOf(setOf<Pair<Int, Int>>()) }
    var foundForever by remember { mutableStateOf(false) }
    var foundSilent by remember { mutableStateOf(false) }
    var typedText by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    fun normalize(s: String) = s.replace(" ", "").uppercase()

    fun checkSolvedState() {
        if (foundForever && foundSilent) {
            isSuccess = true
            onSolved()
        }
    }

    fun toggleCell(r: Int, c: Int) {
        if (isSuccess) return
        val pos = Pair(r, c)
        val newSet = if (selectedCells.contains(pos)) {
            selectedCells - pos
        } else {
            selectedCells + pos
        }
        selectedCells = newSet

        // Build string from selected
        val constructed = selectedCells.map { (row, col) -> grid[row][col] }.joinToString("")
        if (constructed.contains("FOREVER")) {
            foundForever = true
        }
        if (constructed.contains("SILENT")) {
            foundSilent = true
        }

        // Quick check if all cells for FOREVER (Row 0, 0..6) and SILENT (Row 2, 0..5) are selected
        val foreverCells = (0..6).map { Pair(0, it) }.toSet()
        val silentCells = (0..5).map { Pair(2, it) }.toSet()

        if (selectedCells.containsAll(foreverCells)) {
            foundForever = true
        }
        if (selectedCells.containsAll(silentCells)) {
            foundSilent = true
        }

        checkSolvedState()
    }

    fun verifyInput() {
        if (isSuccess) return
        val cleanInput = normalize(typedText)
        if (cleanInput == normalize(targetTitle) || (cleanInput.contains("FOREVER") && cleanInput.contains("SILENT"))) {
            foundForever = true
            foundSilent = true
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
            .testTag("word_search_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "WORD SEARCH PUZZLE",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            ),
            color = StageGold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tap letters in the grid to highlight them, then type the title below:",
            style = MaterialTheme.typography.bodySmall,
            color = IvoryText.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Grid View
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, StageGold.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
            color = DarkBurgundy
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                grid.forEachIndexed { r, row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        row.forEachIndexed { c, letter ->
                            val pos = Pair(r, c)
                            val isSelected = selectedCells.contains(pos)
                            val isForeverRow = foundForever && r == 0 && c <= 6
                            val isSilentRow = foundSilent && r == 2 && c <= 5
                            val isHighlighted = isForeverRow || isSilentRow || isSelected

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        when {
                                            isForeverRow || isSilentRow -> UnlockedGreen.copy(alpha = 0.4f)
                                            isSelected -> StageGold.copy(alpha = 0.4f)
                                            else -> VelvetCharcoal
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        when {
                                            isForeverRow || isSilentRow -> UnlockedGreen
                                            isSelected -> StageGold
                                            else -> StageGold.copy(alpha = 0.3f)
                                        },
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable { toggleCell(r, c) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$letter",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = if (isHighlighted) IvoryText else IvoryText.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Numbered Grid Input below word search
        NumberedGridInputView(
            targetTitle = targetTitle,
            onSolved = onSolved
        )
    }
}
