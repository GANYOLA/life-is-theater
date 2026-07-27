package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pin
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
fun AlphabetCipherPuzzleView(
    targetTitle: String = "Life is Theater",
    onSolved: () -> Unit
) {
    var typedText by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    // Split target into clean words
    val wordList = remember(targetTitle) {
        targetTitle.trim()
            .split("\\s+".toRegex())
            .map { word -> word.filter { it.isLetter() }.uppercase() }
            .filter { it.isNotEmpty() }
    }

    val totalLength = remember(wordList) { wordList.sumOf { it.length } }
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

    // Alphabet reference key strings (A=1, B=2, C=3 ... Z=26)
    val alphabetKey = remember {
        listOf(
            "A=1", "B=2", "C=3", "D=4", "E=5", "F=6", "G=7", "H=8", "I=9", "J=10",
            "K=11", "L=12", "M=13", "N=14", "O=15", "P=16", "Q=17", "R=18", "S=19",
            "T=20", "U=21", "V=22", "W=23", "X=24", "Y=25", "Z=26"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("alphabet_cipher_puzzle_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Pin,
                contentDescription = null,
                tint = StageGold,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ALPHABET CIPHER (A=1, B=2... Z=26)",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = StageGold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Each number represents a letter in the alphabet (A=1, B=2, C=3...):\nDecode the numbers to reveal the finale title!",
            style = MaterialTheme.typography.bodySmall,
            color = IvoryText.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Reference Key Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkBurgundy),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StageGold.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CIPHER KEY REFERENCE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = StageGold
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Render in 3 rows
                alphabetKey.chunked(9).forEach { chunk ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                    ) {
                        chunk.forEach { item ->
                            Text(
                                text = item,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = IvoryText.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid Display
        val currentTyped = normalize(typedText).padEnd(totalLength, ' ')
        var globalIndex = 0

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            wordList.forEachIndexed { wordIdx, word ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until word.length) {
                            val charVal = word[i]
                            val numberCode = charVal.code - 'A'.code + 1
                            val letterChar = if (globalIndex < typedText.length) currentTyped[globalIndex] else ' '
                            val isFilled = globalIndex < typedText.length

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(36.dp)
                            ) {
                                // Number code above box
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(StageGold)
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "$numberCode",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        ),
                                        color = Color.Black
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
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

        Spacer(modifier = Modifier.height(20.dp))

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
                placeholder = { Text("Type decoded letters here...", color = Color.Gray, fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("alphabet_cipher_text_input"),
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
                    .testTag("alphabet_cipher_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = StageGold, contentColor = Color.Black),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("SUBMIT DECODED TITLE", fontWeight = FontWeight.Bold)
            }

            if (isError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Incorrect! Match the numbers (A=1, B=2, C=3...) to decode $cleanTarget.",
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
                        text = "CIPHER SOLVED: LIFE IS THEATER!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}
