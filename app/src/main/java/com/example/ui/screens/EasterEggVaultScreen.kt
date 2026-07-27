package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
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
import com.example.data.AlbumTracklist
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
fun EasterEggVaultScreen(
    totalStars: Int = 0,
    purchasedHints: Int = 0,
    unlockedTopics: Set<Int> = emptySet(),
    onRevealTopic: (levelNumber: Int) -> Boolean = { false },
    onBackClick: () -> Unit
) {
    var shopFeedbackText by remember { mutableStateOf("") }

    val spentOnTopics = unlockedTopics.size * 2
    val availableStars = (totalStars - purchasedHints - spentOnTopics).coerceAtLeast(0)

    val defaultTopicMap = remember {
        mapOf(
            1 to "The song is about a person who has spent years feeling trapped, forgotten, and invisible, dreaming of someone who will find them, break them free, and help them finally step into the life they were meant to live.\n\nMain topics:\n• Feeling trapped and unseen\n• Loneliness and longing\n• Hope and destiny\n• Finding love and salvation\n• Breaking free from a metaphorical curse\n• Finally becoming the main character of your own story\n\nIn one sentence: It's about waiting for someone to rescue you from a life of invisibility and help you finally begin your real story.",
            2 to "The song is about two people who are deeply in love but are forced to hide their relationship because their love is not accepted by the people and society around them, choosing to meet in secret rather than give up on each other.\n\nMain topics:\n• Forbidden love\n• Secret relationships\n• Loving someone you cannot openly be with\n• Hiding your feelings from family and society\n• The pain of living a love that cannot be publicly acknowledged\n• Choosing love despite fear and disapproval\n• The emotional weight of lies and secrecy\n• Creating a private world where the two lovers can be together\n• The conflict between love and social expectations\n• Holding onto hope that their love can be more than just a secret\n\nIn one sentence: It's about two people who are forced to love each other in secret because the world around them refuses to accept their relationship, choosing to meet \"in disguise\" rather than let society keep them apart.",
            3 to "The song is about a royal woman who is surrounded by wealth, status, and suitors who only want her for her crown, until she meets someone who genuinely loves her as a person and chooses to escape royalty with him for true love.\n\nMain topics:\n• Feeling trapped by royalty and expectations\n• Being desired for status rather than genuine love\n• Rejecting arranged or strategic marriage\n• Finding someone who sees the real person behind the crown\n• Choosing love over wealth, power, and legacy\n• Escaping a life of luxury that feels lonely and empty\n• Sacrificing royalty for freedom and true love\n• \"Your Majesty\" transforming from a title into a term of genuine love and devotion\n\nIn one sentence: It's about a princess who realizes that true love is worth more than a crown, abandons her royal life, and chooses a person who loves her for who she is rather than what she represents.",
            4 to "The song is about a man who carries the weight of providing for his family, sacrificing his own happiness and emotional well-being to protect the people he loves, while being told to stay strong even when the pressure becomes unbearable.\n\nMain topics:\n• Sacrifice for family\n• The pressure of being a provider\n• Hiding pain and struggling in silence\n• Feeling worthless despite working hard\n• The expectation that men must always be strong\n• Putting your family's needs before your own\n• Enduring poverty, hardship, and pressure\n• Finding purpose through protecting the people you love\n• The fear of becoming a burden instead of a source of strength\n• The idea that suffering can shape and strengthen you\n• The emotional cost of giving everything of yourself to others\n\nIn one sentence: It's about a man who sacrifices his own dreams, happiness, and well-being to provide for his family, carrying unbearable pressure in silence because he believes his purpose is to keep the people he loves safe.",
            5 to "The song is about someone confronting an ex-lover who pretends to be superior and has moved on, while secretly knowing that the narrator still lives in their thoughts, memories, habits, and identity.\n\nMain topics:\n• A toxic or complicated past relationship\n• An ex who acts superior and looks down on the narrator\n• Knowing that someone secretly misses you despite pretending not to\n• The narrator's confidence that they left a permanent mark on their ex\n• Being mocked or belittled while still being the person the ex needed\n• The irony of an ex trying to replace you but realizing you were irreplaceable\n• The idea that you can leave someone's life without truly disappearing from it\n• Taking back your power by realizing \"you know everything about me because I taught you who you are\"\n\nIn one sentence: It's about realizing that even though an ex tried to move on and act like they were better than you, you shaped who they became so deeply that they can never truly escape your influence or forget you.",
            6 to "The song is about someone who chooses to embrace their identity and individuality in a society that condemns anyone who refuses to conform, accepting the label of \"sinner\" rather than betraying themselves just to be accepted.\n\nMain topics:\n• Choosing to be different in a broken or judgmental society\n• Refusing to conform to society's expectations\n• Being labeled as a sinner or an outcast\n• Being judged and feared for challenging accepted beliefs\n• The hypocrisy of people who condemn those who are different\n• Choosing authenticity over acceptance\n• Freedom that comes with a price\n• Being willing to suffer consequences rather than surrender your identity\n• Defiance against traditional and popular ideas imposed by society\n• Turning the label of \"Lilith\" into a symbol of rebellion, independence, and self-acceptance\n\nIn one sentence: It's about embracing who you are in a society that calls you wicked for refusing to conform, choosing to burn as your true self rather than kneel and become what everyone else wants you to be.",
            7 to "The song is about a young girl who is raised to believe she has a beautiful destiny, only to be exploited and turned into a performer for others, eventually realizing that the role forced upon her has consumed her identity so completely that she can no longer escape it.\n\nMain topics:\n• Exploitation and loss of innocence\n• Being forced into a life you never chose\n• The illusion of beauty hiding deep suffering\n• Losing your youth and identity to please others\n• Being treated as a possession rather than a person\n• Dreaming of freedom but being unable to reach it\n• Becoming trapped by the identity that others created for you\n• Having nowhere else to go after years of being controlled\n• The tragedy of realizing that the cage has become your entire world\n• Wanting to escape but ultimately remaining trapped in the only life you know\n\nIn one sentence: It's about a girl who was turned into a beautiful \"lantern\" for others to admire and control, dreaming of freedom but ultimately realizing that she has become so deeply defined by her captivity that she has nowhere else to go and can only continue burning where others want her to.",
            8 to "The song is about a person who is kidnapped from their homeland, sold into slavery, and raised to serve the wealthy, eventually becoming part of the same system that once enslaved them by teaching another kidnapped child the same lessons of obedience.\n\nMain topics:\n• Kidnapping and forced displacement\n• Slavery and exploitation\n• Losing one's homeland and family\n• Being trained to obey and remain silent\n• The cycle of oppression being passed from one victim to another\n• The painful contradiction of gaining personal freedom while knowing someone else is taking your place\n• Survivor's guilt and loneliness\n• Anger toward the people and land responsible for destroying your original life\n• Losing the ability to return to who you once were\n• The tragedy of freedom coming too late to truly restore your old life\n\nIn one sentence: It's about a kidnapped person who survives slavery and eventually gains freedom, but is permanently changed by it, knowing that the system that stole their life has also turned them into someone who unknowingly helps continue the same cycle of oppression.",
            9 to "The song is about someone who falls in love with a person who never truly commits to them, leaving them questioning whether their relationship was genuine or just an elaborate roleplay, until they eventually find a real partner—but still carry unresolved feelings for the person who never chose them.\n\nMain topics:\n• Feeling inadequate because you don't fit someone's ideal type\n• Being desired and displayed without being genuinely loved\n• The uncertainty of an undefined relationship\n• Dreaming of marriage and a future that may never happen\n• Waiting for someone to finally commit\n• The pain of realizing you were never officially together, so you can't even call it betrayal\n• Wanting to hate someone because loving them hurts too much\n• Finding genuine love with someone else\n• The complicated reality of still loving someone from your past despite being happily married\n• The contrast between fantasy love (\"roleplay\") and real, committed love\n\nIn one sentence: It's about realizing that the person you dreamed of marrying only ever treated your relationship like a fantasy, and even after finding someone who truly loves and chooses you, a part of your heart still belongs to the person who never did.",
            10 to "The song is about a woman who embraces the glamorous, seductive, and transactional world of being a \"playboy bunny,\" using her beauty and charm to gain access to luxury, money, and power while knowing that the men around her are ultimately the ones being played.\n\nMain topics:\n• Glamour, luxury, and wealth\n• Seduction as a form of power and control\n• Men desiring her for her beauty and sexuality\n• The transactional nature of relationships\n• Performing a seductive persona to get what she wants\n• Using charm and attraction to pay the bills\n• The contrast between what men think they're getting and what she actually wants\n• Being surrounded by Playboy-style fame, parties, cars, fashion, and excess\n• Turning the idea of the \"Playboy bunny\" into an identity of confidence, survival, and ambition\n• \"It's all about the money\" as the central motivation behind the glamorous performance\n\nIn one sentence: It's about a woman who plays the role of the irresistible Playboy bunny, letting wealthy men believe they have power over her while she uses their desire to secure the luxurious life and financial freedom she wants.",
            11 to "The song is about a glamorous performer who has learned that romantic affection is temporary, while wealth and luxury feel permanent, so she uses her beauty, stage presence, and femininity to make men desire her without allowing their attention to define her worth.\n\nMain topics:\n• Choosing wealth and material security over romantic affection\n• The temporary nature of flowers, compliments, and sweet talk\n• Using beauty and performance as a source of power\n• The glamorous life of a woman who knows exactly what she wants\n• Men being captivated by her appearance and presence\n• Rejecting empty promises in favor of something tangible and permanent\n• The contrast between love and luxury\n• A confident, almost theatrical female persona who controls the attention of the room\n• The high heels as a symbol of femininity, seduction, confidence, and power\n• Treating romance as a performance where she is the star and the men are the audience\n\nIn one sentence: It's about a woman who refuses to be won over by empty romance, choosing diamonds and financial security instead, while confidently using her beauty and stage presence to keep men captivated and under her control.",
            12 to "The song is about someone who dreams of leaving their ordinary life behind and making it in Hollywood, believing that the city represents their destiny, identity, and chance to become someone extraordinary—even while facing doubt, fear, and warnings from their family.\n\nMain topics:\n• Dreaming of fame and success\n• The desire to escape an ordinary or restrictive life\n• Hollywood as a symbol of destiny and a better future\n• Ambition and the pursuit of a lifelong dream\n• Believing that you are meant for something bigger\n• Struggling against doubt, rejection, and fear of failure\n• Choosing to risk everything rather than live with regret\n• The glamour and fantasy of celebrity culture\n• Seeing a dream as a place where you truly belong\n• Being willing to suffer and fail if it means getting closer to your dream\n\nIn one sentence: It's about a person who feels that Hollywood is calling them toward the life they were meant to live, willing to leave everything behind and risk failure rather than give up on their dream of becoming someone and making history.",
            13 to "The song is about the grand finale of an album where life itself is unveiled as a grand theater performance. The artist reflects on every mask worn, every role played, and every emotion expressed under the bright stage lights.\n\nMain topics:\n• Life as a theater stage\n• Unmasking the soul\n• Grand finale reflections\n• Art, performance, and reality\n• Final curtain call\n\nIn one sentence: It's about living your life on an open stage where every decision is a performance, embracing the drama and beauty of human existence as the grand finale production."
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SHOP",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TheaterBackground)
            )
        },
        containerColor = TheaterBackground,
        modifier = Modifier.testTag("reveal_track_title_shop_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                // Shop Header Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkBurgundy),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, StageGold)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = StageGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SHOP (2 STARS = 1 TOPIC REVEAL)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                ),
                                color = StageGold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Available Stars: ⭐ $availableStars",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = IvoryText
                                )
                                Text(
                                    text = "Topics Revealed: 💡 ${unlockedTopics.size} / 13",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = StageGold.copy(alpha = 0.8f)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(VelvetCrimson)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "COST: 2 ⭐",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = IvoryText
                                )
                            }
                        }

                        if (shopFeedbackText.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = shopFeedbackText,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = if (shopFeedbackText.contains("Revealed")) UnlockedGreen else VelvetCrimson
                            )
                        }
                    }
                }
            }

            items(AlbumTracklist.levels, key = { it.levelNumber }) { level ->
                val isUnlocked = unlockedTopics.contains(level.levelNumber)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            if (isUnlocked) StageGold else StageGold.copy(alpha = 0.3f),
                            RoundedCornerShape(16.dp)
                        )
                        .testTag("track_topic_shop_item_${level.levelNumber}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) StageSurface else DarkBurgundy
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (isUnlocked) UnlockedGreen.copy(alpha = 0.2f) else DarkBurgundy),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isUnlocked) Icons.Default.Visibility else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (isUnlocked) UnlockedGreen else StageGold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "TRACK #${level.levelNumber}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = StageGold
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = if (isUnlocked) "TOPIC REVEALED" else "TRACK TOPIC (LOCKED)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = IvoryText
                                )
                            }

                            if (!isUnlocked) {
                                Button(
                                    onClick = {
                                        val success = onRevealTopic(level.levelNumber)
                                        if (success) {
                                            shopFeedbackText = "Revealed Topic for Track #${level.levelNumber}! ⭐⭐"
                                        } else {
                                            shopFeedbackText = "Need 2 Stars! Solve mini games quickly to earn stars."
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = StageGold,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    enabled = availableStars >= 2,
                                    modifier = Modifier.testTag("reveal_topic_btn_${level.levelNumber}")
                                ) {
                                    Text("REVEAL (2 ⭐)", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(UnlockedGreen)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("UNLOCKED", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        if (isUnlocked) {
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkBurgundy)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "TRACK SONG TOPIC:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = VelvetCrimson
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = defaultTopicMap[level.levelNumber] ?: "Track topic description.",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Serif),
                                        color = StageGold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
