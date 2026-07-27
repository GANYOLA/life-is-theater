package com.example.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBurgundy
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.IvoryText
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageSurface
import com.example.ui.theme.UnlockedGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LanternLinkView(
    targetTitle: String,
    nodeTiles: List<String>,
    onSolved: () -> Unit
) {
    val cleanTarget = targetTitle.replace(" ", "").uppercase()
    val linkedIndices = remember { mutableStateListOf<Int>() }
    var isSuccess by remember { mutableStateOf(false) }

    fun toggleNode(index: Int) {
        if (isSuccess) return
        if (linkedIndices.contains(index)) {
            linkedIndices.remove(index)
        } else {
            linkedIndices.add(index)
        }

        val constructed = linkedIndices.joinToString("") { nodeTiles[it].uppercase() }
        if (constructed == cleanTarget || linkedIndices.size == nodeTiles.size) {
            isSuccess = true
            onSolved()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lantern_link_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TAP LANTERNS TO CONNECT LIGHTS",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            ),
            color = StageGold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Target Preview Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, StageGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
            color = StageSurface
        ) {
            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (linkedIndices.isEmpty()) "• LIGHT NODES BELOW •" else linkedIndices.joinToString(" ") { nodeTiles[it].uppercase() },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ),
                    color = if (isSuccess) UnlockedGreen else StageGold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            maxItemsInEachRow = 4
        ) {
            nodeTiles.forEachIndexed { index, letter ->
                val isLit = linkedIndices.contains(index)
                val nodeColor by animateColorAsState(
                    targetValue = if (isLit) GoldenAmber else DarkBurgundy,
                    label = "lanternColor"
                )

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(58.dp)
                        .shadow(if (isLit) 12.dp else 2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(nodeColor)
                        .border(
                            2.dp,
                            if (isLit) StageGold else StageGold.copy(alpha = 0.3f),
                            CircleShape
                        )
                        .clickable { toggleNode(index) }
                        .testTag("lantern_node_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = if (isLit) Color.Black else StageGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = letter.uppercase(),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isLit) Color.Black else IvoryText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isSuccess) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = UnlockedGreen)
                Text(
                    text = " ALL LANTERNS LIT!",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = UnlockedGreen
                )
            }
        }
    }
}
