package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.DisposableEffect
import kotlinx.coroutines.delay
import androidx.compose.runtime.key
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.foundation.layout.PaddingValues
import com.example.data.LevelData
import com.example.data.PuzzleType
import com.example.ui.components.AlphabetCipherPuzzleView
import com.example.ui.components.CardFlipView
import com.example.ui.components.CrosswordView
import com.example.ui.components.EmojiPuzzleView
import com.example.ui.components.GuessWhoView
import com.example.ui.components.LanternLinkView
import com.example.ui.components.LetterUnscrambleView
import com.example.ui.components.NumberedGridInputView
import com.example.ui.components.RoleplayConnectionsView
import com.example.ui.components.TarotCardPuzzleView
import com.example.ui.components.WordSearchView
import com.example.ui.components.WordleGuessView
import com.example.ui.components.LanternLinkView
import com.example.ui.components.LetterUnscrambleView
import com.example.ui.components.WordleGuessView
import com.example.ui.theme.DarkBurgundy
import com.example.ui.theme.IvoryText
import com.example.ui.theme.LockGray
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageSurface
import com.example.ui.theme.TheaterBackground
import com.example.ui.theme.UnlockedGreen
import com.example.ui.theme.VelvetCrimson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    levelData: LevelData,
    totalStars: Int = 0,
    purchasedHints: Int = 0,
    isAudioPlaying: Boolean = false,
    onBuyHintWithStar: () -> Boolean = { false },
    onBackClick: () -> Unit,
    onCompleteLevel: (stars: Int, hintsUsed: Int, timeTakenMillis: Long) -> Unit,
    onPlayAudioTeaser: () -> Unit,
    onStopAudio: () -> Unit = {},
    onViewDetailClick: () -> Unit,
    onNextLevelClick: () -> Unit,
    onLilithRuneTapped: () -> Unit,
    onResetLevel: (Int) -> Unit = {}
) {
    DisposableEffect(Unit) {
        onDispose {
            onStopAudio()
        }
    }

    var hintsUsedCount by remember { mutableIntStateOf(0) }
    var showHintDialog by remember { mutableStateOf(false) }
    var hintBuyMessage by remember { mutableStateOf("") }
    var isSolved by remember { mutableStateOf(false) }
    var puzzleResetKey by remember { mutableIntStateOf(0) }
    var elapsedTimeSeconds by remember { mutableIntStateOf(0) }
    var solvedTimeTaken by remember { mutableLongStateOf(0L) }

    val availableStars = (totalStars - purchasedHints).coerceAtLeast(0)

    LaunchedEffect(isSolved, puzzleResetKey) {
        if (!isSolved) {
            elapsedTimeSeconds = 0
            while (!isSolved) {
                delay(1000L)
                elapsedTimeSeconds++
            }
        }
    }

    val currentStars = when {
        elapsedTimeSeconds <= 60 -> 3
        elapsedTimeSeconds <= 180 -> 2
        else -> 1
    }

    fun handleSolved() {
        if (!isSolved) {
            isSolved = true
            solvedTimeTaken = elapsedTimeSeconds * 1000L
            val stars = currentStars
            onCompleteLevel(stars, hintsUsedCount, solvedTimeTaken)
        }
    }

    fun resetCurrentLevel() {
        isSolved = false
        puzzleResetKey++
        onResetLevel(levelData.levelNumber)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "LEVEL ${levelData.levelNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        ),
                        color = IvoryText
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = StageGold
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { resetCurrentLevel() },
                        modifier = Modifier.testTag("reset_level_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Level",
                            tint = StageGold
                        )
                    }

                    IconButton(
                        onClick = {
                            showHintDialog = !showHintDialog
                            if (showHintDialog && hintsUsedCount == 0) {
                                hintsUsedCount = 1
                            }
                        },
                        modifier = Modifier.testTag("hint_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Hint",
                            tint = if (showHintDialog) StageGold else IvoryText.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TheaterBackground)
            )
        },
        containerColor = TheaterBackground,
        modifier = Modifier.testTag("game_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Live Countdown Timer & Star System
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, StageGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkBurgundy)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Timer",
                            tint = StageGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val mins = elapsedTimeSeconds / 60
                        val secs = elapsedTimeSeconds % 60
                        Text(
                            text = String.format("%02d:%02d", mins, secs),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            ),
                            color = IvoryText
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(3) { starIdx ->
                            val isActive = starIdx < currentStars
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (isActive) StageGold else LockGray,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (currentStars) {
                                3 -> "< 1m (3★)"
                                2 -> "< 3m (2★)"
                                else -> "> 3m (1★)"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = StageGold
                        )
                    }
                }
            }

            // Hint Card Banner
            AnimatedVisibility(visible = showHintDialog) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkBurgundy),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StageGold)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = StageGold)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "HINT: ${levelData.hintText}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = StageGold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Available Stars: ⭐ $availableStars | Hint Tokens: 💡 $purchasedHints",
                                style = MaterialTheme.typography.bodySmall,
                                color = IvoryText.copy(alpha = 0.8f)
                            )

                            if (availableStars >= 1) {
                                Button(
                                    onClick = {
                                        val success = onBuyHintWithStar()
                                        if (success) {
                                            hintBuyMessage = "Extra hint token bought for 1 Star!"
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = StageGold,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(32.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                ) {
                                    Text("BUY 1 HINT (1 ⭐)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (hintBuyMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hintBuyMessage,
                                style = MaterialTheme.typography.labelSmall,
                                color = UnlockedGreen
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Riddle Poem Card
            if (levelData.riddleClue.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, DarkBurgundy, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = StageSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(DarkBurgundy)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "TRACK RIDDLE POEM",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                ),
                                color = StageGold
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = levelData.riddleClue,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 22.sp
                            ),
                            color = IvoryText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Render specific Mini-Game Puzzle
            key(puzzleResetKey) {
                when (levelData.puzzleType) {
                    PuzzleType.ROYAL_EMOJI_GUESS -> {
                        EmojiPuzzleView(
                            emojis = "👑 🤴 🏰",
                            targetTitle = levelData.trackTitle,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.EMOJI_DECODER_MATCH -> {
                        EmojiPuzzleView(
                            emojis = if (levelData.levelNumber == 10) "👯‍♀️ 🐰 🍸" else "🧔 🚫 😭",
                            targetTitle = levelData.trackTitle,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.LANTERN_EMOJI_DECODE -> {
                        EmojiPuzzleView(
                            emojis = "🏮 👧",
                            targetTitle = levelData.trackTitle,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.WORD_SEARCH_GRID -> {
                        WordSearchView(
                            targetTitle = levelData.trackTitle,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.RIDDLE_MANUAL_TYPE,
                    PuzzleType.GUESS_WHO_RIDDLE -> {
                        GuessWhoView(
                            targetAnswer = levelData.trackTitle,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.NUMBERED_GRID_INPUT -> {
                        NumberedGridInputView(
                            targetTitle = levelData.trackTitle,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.TAROT_CARD_READING -> {
                        TarotCardPuzzleView(
                            targetTitle = levelData.trackTitle,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.CROSSWORD_UNSCRAMBLE,
                    PuzzleType.DRAMATIC_STEP_CROSSWORD,
                    PuzzleType.GRAND_FINALE_ALBUM_CLIMAX -> {
                        if (levelData.crosswordWords.isNotEmpty()) {
                            CrosswordView(
                                words = levelData.crosswordWords,
                                targetTrackTitle = levelData.trackTitle,
                                onSolved = { handleSolved() }
                            )
                        } else {
                            LetterUnscrambleView(
                                targetAnswer = levelData.trackTitle,
                                scrambledTiles = levelData.scrambledTiles,
                                onSolved = { handleSolved() }
                            )
                        }
                    }

                    PuzzleType.MASK_PAIR_FLIP,
                    PuzzleType.NEON_CASINO_FLIP,
                    PuzzleType.WALK_OF_FAME_STAR -> {
                        CardFlipView(
                            targetTitle = levelData.trackTitle,
                            tiles = levelData.scrambledTiles,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.ANCIENT_RUNE_CIPHER -> {
                        WordleGuessView(
                            targetWord = levelData.trackTitle,
                            maxAttempts = 6,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.SILENT_FREQUENCY_TYPE -> {
                        LanternLinkView(
                            targetTitle = levelData.trackTitle,
                            nodeTiles = levelData.scrambledTiles,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.DRAMA_SCRIPT_REORDER -> {
                        RoleplayConnectionsView(
                            targetAnswer = levelData.trackTitle,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.ALPHABET_CIPHER_NUMBERS -> {
                        AlphabetCipherPuzzleView(
                            targetTitle = levelData.trackTitle,
                            onSolved = { handleSolved() }
                        )
                    }

                    PuzzleType.EGO_CHESS_PUZZLE -> {
                        LetterUnscrambleView(
                            targetAnswer = levelData.trackTitle,
                            scrambledTiles = levelData.scrambledTiles,
                            onSolved = { handleSolved() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Completion Banner & Actions
            AnimatedVisibility(visible = isSolved, enter = fadeIn()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(2.dp, UnlockedGreen, RoundedCornerShape(20.dp)),
                    color = StageSurface
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(DarkBurgundy, StageSurface)
                                )
                            )
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = UnlockedGreen,
                            modifier = Modifier.size(52.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "TRACK REVEALED!",
                            style = MaterialTheme.typography.labelLarge.copy(
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = UnlockedGreen
                        )

                        Text(
                            text = levelData.trackTitle,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = IvoryText,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val finalTimeSecs = solvedTimeTaken / 1000
                        val starsEarned = when {
                            finalTimeSecs <= 60 -> 3
                            finalTimeSecs <= 180 -> 2
                            else -> 1
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(3) { index ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < starsEarned) StageGold else LockGray,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = when (starsEarned) {
                                3 -> "Speed Demon! Solved in under 1 minute! (3 Stars)"
                                2 -> "Great Job! Solved in under 3 minutes! (2 Stars)"
                                else -> "Track Completed! (1 Star)"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = StageGold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = onPlayAudioTeaser,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = VelvetCrimson,
                                    contentColor = IvoryText
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("listen_synth_teaser")
                            ) {
                                Icon(
                                    imageVector = if (isAudioPlaying) Icons.Default.Replay else Icons.Default.MusicNote,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isAudioPlaying) "REPLAY TEASER" else "PLAY TEASER",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            if (isAudioPlaying) {
                                Button(
                                    onClick = onStopAudio,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF8B0000),
                                        contentColor = IvoryText
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("stop_music_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Stop,
                                        contentDescription = "Stop Music",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("STOP", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = onNextLevelClick,
                            colors = ButtonDefaults.buttonColors(containerColor = StageGold, contentColor = Color.Black),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("next_track_button")
                        ) {
                            Text("NEXT TRACK", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}
