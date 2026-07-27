package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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

private data class CrosswordWordDef(
    val id: Int,
    val word: String,
    val direction: String, // "ACROSS" or "DOWN"
    val number: Int,
    val startRow: Int,
    val startCol: Int,
    val clue: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CrosswordView(
    words: List<String>,
    targetTrackTitle: String,
    onSolved: () -> Unit
) {
    // Standard layout for Track 11 "HIGH HEELS MAKE MEN FALL SILENT"
    val wordDefs = remember(words) {
        listOf(
            CrosswordWordDef(0, "HIGH", "ACROSS", 1, 0, 0, "1 Across: Top rank or peak level (4)"),
            CrosswordWordDef(1, "HEELS", "DOWN", 1, 0, 0, "1 Down: Stiletto footwear that makes a statement (5)"),
            CrosswordWordDef(2, "MAKE", "DOWN", 2, 1, 3, "2 Down: To create or cause an effect (4)"),
            CrosswordWordDef(3, "MEN", "ACROSS", 5, 1, 3, "5 Across: Gentlemen on stage (3)"),
            CrosswordWordDef(4, "FALL", "DOWN", 4, 2, 2, "4 Down: To drop or tumble down (4)"),
            CrosswordWordDef(5, "SILENT", "ACROSS", 3, 4, 0, "3 Across: Completely quiet without a word (6)")
        )
    }

    val solvedWords = remember { mutableStateMapOf<Int, Boolean>() }
    var activeWordIdx by remember { mutableStateOf(0) }
    var currentInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isAllSolved by remember { mutableStateOf(false) }

    val activeDef = wordDefs.getOrNull(activeWordIdx) ?: wordDefs[0]

    // Helper to calculate cell positions for each word
    fun getCellsForWord(def: CrosswordWordDef): List<Pair<Int, Int>> {
        return (0 until def.word.length).map { i ->
            if (def.direction == "ACROSS") {
                Pair(def.startRow, def.startCol + i)
            } else {
                Pair(def.startRow + i, def.startCol)
            }
        }
    }

    // Grid letter state map: Pair(row, col) -> Char
    val gridLetters = remember(solvedWords, activeWordIdx, currentInput) {
        val map = mutableMapOf<Pair<Int, Int>, Char>()
        // Fill solved words
        wordDefs.forEachIndexed { idx, def ->
            if (solvedWords[idx] == true) {
                getCellsForWord(def).forEachIndexed { charIdx, pos ->
                    map[pos] = def.word[charIdx]
                }
            }
        }
        // Fill current typing input for active word
        val activeCells = getCellsForWord(activeDef)
        val cleanInput = currentInput.uppercase()
        activeCells.forEachIndexed { charIdx, pos ->
            if (charIdx < cleanInput.length && map[pos] == null) {
                map[pos] = cleanInput[charIdx]
            }
        }
        map
    }

    // Check if cell is part of active word
    val activeWordCells = remember(activeWordIdx) { getCellsForWord(activeDef).toSet() }

    // Number label at top-left of starting cells
    val cellNumbers = remember {
        val map = mutableMapOf<Pair<Int, Int>, Int>()
        wordDefs.forEach { def ->
            map[Pair(def.startRow, def.startCol)] = def.number
        }
        map
    }

    fun verifyActiveWord() {
        if (isAllSolved) return
        val target = activeDef.word.uppercase()
        if (currentInput.trim().uppercase() == target) {
            solvedWords[activeWordIdx] = true
            errorMessage = ""
            currentInput = ""

            // Advance to next unsolved word
            val nextUnsolved = wordDefs.indices.firstOrNull { solvedWords[it] != true }
            if (nextUnsolved != null) {
                activeWordIdx = nextUnsolved
            } else {
                isAllSolved = true
                onSolved()
            }
        } else {
            errorMessage = "Incorrect answer for ${activeDef.direction.lowercase()} clue #${activeDef.number}!"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("crossword_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CROSSWORD PUZZLE GRID (${solvedWords.size}/${wordDefs.size} SOLVED)",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            ),
            color = StageGold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 6x6 Crossword Visual Board
        Surface(
            modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, StageGold.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
            color = DarkBurgundy
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (r in 0..5) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (c in 0..5) {
                            val pos = Pair(r, c)
                            val letter = gridLetters[pos]
                            val num = cellNumbers[pos]
                            val isCellActive = activeWordCells.contains(pos)
                            val isCellInGrid = wordDefs.any { def -> getCellsForWord(def).contains(pos) }

                            if (isCellInGrid) {
                                val isSolvedCell = wordDefs.any { def ->
                                    solvedWords[def.id] == true && getCellsForWord(def).contains(pos)
                                }

                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when {
                                                isSolvedCell -> UnlockedGreen.copy(alpha = 0.25f)
                                                isCellActive -> StageGold.copy(alpha = 0.35f)
                                                else -> VelvetCharcoal
                                            }
                                        )
                                        .border(
                                            width = if (isCellActive) 2.dp else 1.dp,
                                            color = when {
                                                isSolvedCell -> UnlockedGreen
                                                isCellActive -> StageGold
                                                else -> StageGold.copy(alpha = 0.4f)
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            // Select word that contains this cell
                                            val matchingDef = wordDefs.firstOrNull { def ->
                                                getCellsForWord(def).contains(pos) && solvedWords[def.id] != true
                                            } ?: wordDefs.firstOrNull { def -> getCellsForWord(def).contains(pos) }
                                            if (matchingDef != null) {
                                                activeWordIdx = matchingDef.id
                                                currentInput = ""
                                                errorMessage = ""
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Small clue number in top left corner
                                    if (num != null) {
                                        Text(
                                            text = "$num",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = StageGold,
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(start = 3.dp, top = 1.dp)
                                        )
                                    }

                                    // Cell Letter
                                    if (letter != null) {
                                        Text(
                                            text = "$letter",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = if (isSolvedCell) UnlockedGreen else IvoryText
                                        )
                                    }
                                }
                            } else {
                                // Non-grid blank cell
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(Color.Transparent)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Clue list selector buttons
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            wordDefs.forEachIndexed { index, def ->
                val isSolved = solvedWords[index] == true
                val isSelected = activeWordIdx == index

                Surface(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = when {
                                isSolved -> UnlockedGreen
                                isSelected -> StageGold
                                else -> StageGold.copy(alpha = 0.3f)
                            },
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            activeWordIdx = index
                            currentInput = ""
                            errorMessage = ""
                        },
                    color = when {
                        isSolved -> UnlockedGreen.copy(alpha = 0.2f)
                        isSelected -> DarkBurgundy
                        else -> StageSurface
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${def.number} ${def.direction.take(1)}: ${if (isSolved) def.word else "${def.word.length}L"}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isSolved) UnlockedGreen else IvoryText
                        )
                        if (isSolved) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Check, contentDescription = null, tint = UnlockedGreen, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (!isAllSolved) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(containerColor = DarkBurgundy),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StageGold)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = activeDef.clue,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = StageGold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = currentInput,
                        onValueChange = {
                            if (it.length <= activeDef.word.length) {
                                currentInput = it.uppercase()
                                errorMessage = ""
                            }
                        },
                        placeholder = { Text("Type word (${activeDef.word.length} letters)...", color = IvoryText.copy(alpha = 0.5f)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = IvoryText,
                            unfocusedTextColor = IvoryText,
                            focusedBorderColor = StageGold,
                            unfocusedBorderColor = StageGold.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("crossword_input_field")
                    )

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = errorMessage, color = VelvetCrimson, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { verifyActiveWord() },
                        colors = ButtonDefaults.buttonColors(containerColor = StageGold, contentColor = Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("submit_crossword_word")
                    ) {
                        Text("SUBMIT WORD", fontWeight = FontWeight.Bold)
                    }
                }
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
                        text = "CROSSWORD COMPLETELY SOLVED!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}

