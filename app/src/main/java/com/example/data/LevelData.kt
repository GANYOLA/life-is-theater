package com.example.data

enum class PuzzleType {
    CROSSWORD_UNSCRAMBLE,
    MASK_PAIR_FLIP,
    ROYAL_EMOJI_GUESS,
    EMOJI_DECODER_MATCH,
    EGO_CHESS_PUZZLE,
    ANCIENT_RUNE_CIPHER,
    LANTERN_EMOJI_DECODE,
    SILENT_FREQUENCY_TYPE,
    DRAMA_SCRIPT_REORDER,
    NEON_CASINO_FLIP,
    DRAMATIC_STEP_CROSSWORD,
    WALK_OF_FAME_STAR,
    GRAND_FINALE_ALBUM_CLIMAX,
    WORD_SEARCH_GRID,
    GUESS_WHO_RIDDLE,
    TAROT_CARD_READING,
    RIDDLE_MANUAL_TYPE,
    NUMBERED_GRID_INPUT,
    ALPHABET_CIPHER_NUMBERS
}

data class LevelData(
    val levelNumber: Int,
    val trackTitle: String,
    val puzzleType: PuzzleType,
    val riddleClue: String,
    val hintText: String,
    val teaserQuote: String,
    val durationSeconds: String,
    val audioFrequencies: List<Int>,
    val audioTeaserUrl: String? = null,
    val crosswordWords: List<String> = emptyList(),
    val scrambledTiles: List<String> = emptyList()
)

object AlbumTracklist {
    val levels = listOf(
        LevelData(
            levelNumber = 1,
            trackTitle = "The Opening Night",
            puzzleType = PuzzleType.NUMBERED_GRID_INPUT,
            riddleClue = "When velvet curtains gently rise,\nAnd spotlights capture eager eyes,\nThe stage is set, the stars ignite,\nTo celebrate... The Opening Night.",
            hintText = "Type the 3-word title: The Opening Night",
            teaserQuote = "It's about waiting for someone to rescue you from a life of invisibility and help you finally begin your real story.",
            durationSeconds = "3:42",
            audioFrequencies = listOf(440, 554, 659, 880),
            audioTeaserUrl = "https://raw.githubusercontent.com/GANYOLA/life/refs/heads/main/Teasers/the%20opening%20night.mp3"
        ),
        LevelData(
            levelNumber = 2,
            trackTitle = "Disguise",
            puzzleType = PuzzleType.MASK_PAIR_FLIP,
            riddleClue = "Flip the cards to reveal the letter positions (#1..#8), then type the word into the grid below.",
            hintText = "8 numbered grid boxes for D - I - S - G - U - I - S - E",
            teaserQuote = "It's about two people who are forced to love each other in secret because the world around them refuses to accept their relationship, choosing to meet 'in disguise' rather than let society keep them apart.",
            durationSeconds = "4:05",
            audioFrequencies = listOf(349, 440, 523, 698),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/Disguise%20teaser.mp3",
            scrambledTiles = listOf("D", "I", "S", "G", "U", "I", "S", "E")
        ),
        LevelData(
            levelNumber = 3,
            trackTitle = "Your Majesty",
            puzzleType = PuzzleType.ROYAL_EMOJI_GUESS,
            riddleClue = "Decipher the royal crown emojis to unlock Track 3.",
            hintText = "Royal title: Crown + Sovereign",
            teaserQuote = "It's about a princess who realizes that true love is worth more than a crown, abandons her royal life, and chooses a person who loves her for who she is rather than what she represents.",
            durationSeconds = "3:18",
            audioFrequencies = listOf(293, 370, 440, 587),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/your%20majesty%20teaser.mp3"
        ),
        LevelData(
            levelNumber = 4,
            trackTitle = "Men Don't Cry",
            puzzleType = PuzzleType.TAROT_CARD_READING,
            riddleClue = "Flip the three arcana tarot cards to reveal the image representing each word.",
            hintText = "Card I: Men, Card II: Don't, Card III: Cry",
            teaserQuote = "It's about a man who sacrifices his own dreams, happiness, and well-being to provide for his family, carrying unbearable pressure in silence because he believes his purpose is to keep the people he loves safe.",
            durationSeconds = "4:12",
            audioFrequencies = listOf(220, 261, 329, 440),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/Men%20Dont%20Cry%20teaser.mp3"
        ),
        LevelData(
            levelNumber = 5,
            trackTitle = "Mr. Superior",
            puzzleType = PuzzleType.EGO_CHESS_PUZZLE,
            riddleClue = "He believes he holds the highest rank on the board, far above all mortals.",
            hintText = "Unscramble the 10 letter tiles (M R S U P E R I O R)",
            teaserQuote = "It's about realizing that even though an ex tried to move on and act like they were better than you, you shaped who they became so deeply that they can never truly escape your influence or forget you.",
            durationSeconds = "3:55",
            audioFrequencies = listOf(392, 493, 587, 783),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/mr.superiour%20teaser.wav",
            scrambledTiles = listOf("E", "R", "M", "U", "P", "R", "O", "S", "I", "R")
        ),
        LevelData(
            levelNumber = 6,
            trackTitle = "Lilith",
            puzzleType = PuzzleType.ANCIENT_RUNE_CIPHER,
            riddleClue = "Unscramble the ancient runes to reveal the dark queen's name.",
            hintText = "First letter: L | 6 letters",
            teaserQuote = "It's about embracing who you are in a society that calls you wicked for refusing to conform, choosing to burn as your true self rather than kneel and become what everyone else wants you to be.",
            durationSeconds = "4:30",
            audioFrequencies = listOf(220, 261, 311, 392, 440, 523, 622, 659, 880),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/lilith%20teaser.mp3",
            scrambledTiles = listOf("L", "I", "L", "I", "T", "H")
        ),
        LevelData(
            levelNumber = 7,
            trackTitle = "Lantern Girl",
            puzzleType = PuzzleType.LANTERN_EMOJI_DECODE,
            riddleClue = "Decipher the floating paper light emojis: 🏮 👧",
            hintText = "Paper lantern + maiden",
            teaserQuote = "It's about a girl who was turned into a beautiful 'lantern' for others to admire and control, dreaming of freedom but ultimately realizing that she has become so deeply defined by her captivity that she has nowhere else to go and can only continue burning where others want her to.",
            durationSeconds = "3:34",
            audioFrequencies = listOf(329, 415, 493, 659),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/lantern%20girl%20teaser.mp3"
        ),
        LevelData(
            levelNumber = 8,
            trackTitle = "Forever Silent",
            puzzleType = PuzzleType.WORD_SEARCH_GRID,
            riddleClue = "Highlight hidden letters on the grid and type the title below.",
            hintText = "Highlight letters on the grid and type the title below.",
            teaserQuote = "It's about a kidnapped person who survives slavery and eventually gains freedom, but is permanently changed by it, knowing that the system that stole their life has also turned them into someone who unknowingly helps continue the same cycle of oppression.",
            durationSeconds = "4:50",
            audioFrequencies = listOf(196, 246, 293, 392),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/forever%20silent%20teaser.mp3",
            scrambledTiles = listOf("FOREVER", "SILENT")
        ),
        LevelData(
            levelNumber = 9,
            trackTitle = "Roleplay",
            puzzleType = PuzzleType.DRAMA_SCRIPT_REORDER,
            riddleClue = "Mix two word tiles together to form the track title.",
            hintText = "Look for the word combining ROLE and PLAY",
            teaserQuote = "It's about realizing that the person you dreamed of marrying only ever treated your relationship like a fantasy, and even after finding someone who truly loves and chooses you, a part of your heart still belongs to the person who never did.",
            durationSeconds = "3:22",
            audioFrequencies = listOf(349, 415, 523, 698),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/roleplay%20teaser.mp3",
            scrambledTiles = listOf("ROLE", "PLAY")
        ),
        LevelData(
            levelNumber = 10,
            trackTitle = "Playboy Bunny",
            puzzleType = PuzzleType.EMOJI_DECODER_MATCH,
            riddleClue = "Decipher the lounge emojis: 👯‍♀️ 🐰 🍸",
            hintText = "First letter: P | Dancing girls 👯‍♀️ and bunny ears 🐰 in the midnight lounge.",
            teaserQuote = "It's about a woman who plays the role of the irresistible Playboy bunny, letting wealthy men believe they have power over her while she uses their desire to secure the luxurious life and financial freedom she wants.",
            durationSeconds = "3:48",
            audioFrequencies = listOf(277, 349, 415, 554),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/playboy%20bunny%20teaser.mp3",
            scrambledTiles = listOf("P", "L", "A", "Y", "B", "O", "Y", "B", "U", "N", "N", "Y")
        ),
        LevelData(
            levelNumber = 11,
            trackTitle = "High Heels Make Men Fall Silent",
            puzzleType = PuzzleType.DRAMATIC_STEP_CROSSWORD,
            riddleClue = "Echoing footsteps down the marble hall. What happens when supreme confidence walks in?",
            hintText = "First letter: H | 6 crossword clues: HIGH, HEELS, MAKE, MEN, FALL, SILENT.",
            teaserQuote = "It's about a woman who refuses to be won over by empty romance, choosing diamonds and financial security instead, while confidently using her beauty and stage presence to keep men captivated and under her control.",
            durationSeconds = "5:15",
            audioFrequencies = listOf(220, 277, 329, 440),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/high%20heels%20make%20men%20fall%20silent%20teaser.mp3",
            crosswordWords = listOf("HIGH", "HEELS", "MAKE", "MEN", "FALL", "SILENT")
        ),
        LevelData(
            levelNumber = 12,
            trackTitle = "Hollywood",
            puzzleType = PuzzleType.GUESS_WHO_RIDDLE,
            riddleClue = "Gilded boulevard of silver screens, flash cameras, and brass stars.",
            hintText = "First letter: H | Celebrities, Oscar statuettes, and Walk of Fame boulevard.",
            teaserQuote = "It's about a person who feels that Hollywood is calling them toward the life they were meant to live, willing to leave everything behind and risk failure rather than give up on their dream of becoming someone and making history.",
            durationSeconds = "4:01",
            audioFrequencies = listOf(392, 440, 493, 587),
            audioTeaserUrl = "https://raw.githubusercontent.com/udsgazeg/life/refs/heads/main/Teasers/hollywood%20teaser.wav",
            scrambledTiles = listOf("H", "O", "L", "L", "Y", "W", "O", "O", "D")
        ),
        LevelData(
            levelNumber = 13,
            trackTitle = "Life is Theater",
            puzzleType = PuzzleType.ALPHABET_CIPHER_NUMBERS,
            riddleClue = "Decode the numbers using the alphabet code (A=1, B=2, C=3... Z=26) to reveal the finale track title!",
            hintText = "A=1, B=2, C=3... L=12, I=9, F=6, E=5 (LIFE) ...",
            teaserQuote = "It's about living your life on an open stage where every decision is a performance, embracing the drama and beauty of human existence as the grand finale production.",
            durationSeconds = "5:40",
            audioFrequencies = listOf(440, 554, 659, 880, 1108),
            crosswordWords = listOf("LIFE", "IS", "THEATER")
        )
    )
}
