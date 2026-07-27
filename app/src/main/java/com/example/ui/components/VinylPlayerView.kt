package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.DarkBurgundy
import com.example.ui.theme.IvoryText
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageSurface
import com.example.ui.theme.VelvetCrimson
import kotlinx.coroutines.launch

@Composable
fun VinylPlayerView(
    trackTitle: String,
    isPlayingAudio: Boolean,
    onPlayToggle: () -> Unit,
    onReverseSpin: () -> Unit
) {
    val rotationAnim = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var manualAngle by remember { mutableStateOf(0f) }

    LaunchedEffect(isPlayingAudio) {
        if (isPlayingAudio) {
            rotationAnim.animateTo(
                targetValue = rotationAnim.value + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.5.dp, StageGold.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .testTag("vinyl_player_view"),
        color = StageSurface
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "VINYL AUDIO PLAYER",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = StageGold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Vinyl Record Disc
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF22222A), Color(0xFF08080C), Color.Black)
                        )
                    )
                    .border(3.dp, Color(0xFF333340), CircleShape)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, _, _ ->
                            if (pan.x < -10 || pan.y > 10) {
                                manualAngle -= 25f
                                onReverseSpin()
                            }
                        }
                    }
                    .rotate(rotationAnim.value + manualAngle)
                    .testTag("vinyl_record_disc"),
                contentAlignment = Alignment.Center
            ) {
                // Vinyl Groove lines
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                )

                // Album Center Label
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(DarkBurgundy)
                        .border(2.dp, StageGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_album_cover),
                        contentDescription = "Center Label",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = trackTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = IvoryText
            )

            Text(
                text = "Tip: Drag vinyl backward to trigger secret frequency!",
                style = MaterialTheme.typography.bodySmall,
                color = StageGold.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(
                    onClick = onPlayToggle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VelvetCrimson,
                        contentColor = IvoryText
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("play_teaser_button")
                ) {
                    Icon(
                        imageVector = if (isPlayingAudio) Icons.Default.GraphicEq else Icons.Default.PlayArrow,
                        contentDescription = "Play Teaser"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPlayingAudio) "PLAYING TEASER..." else "LISTEN TEASER",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = onReverseSpin,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBurgundy,
                        contentColor = StageGold
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("reverse_spin_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.RotateLeft,
                        contentDescription = "Reverse Spin"
                    )
                }
            }
        }
    }
}
