package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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

@Composable
fun EmojiPuzzleView(
    emojis: String,
    targetTitle: String,
    onSolved: () -> Unit
) {
    var typedText by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    fun normalize(str: String) = str.replace(Regex("[^A-Za-z0-9]"), "").lowercase()

    fun verify() {
        if (isSuccess) return
        if (normalize(typedText) == normalize(targetTitle)) {
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
            .testTag("emoji_puzzle_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large Emoji Display Card (No writing text)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color(0xFFFFD700).copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
            color = Color(0xFF1E1024)
        ) {
            Box(
                modifier = Modifier.padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emojis,
                    fontSize = 56.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Numbered Grid Input below emoji card
        NumberedGridInputView(
            targetTitle = targetTitle,
            onSolved = onSolved
        )
    }
}
