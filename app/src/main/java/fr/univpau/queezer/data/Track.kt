package fr.univpau.queezer.data

data class Track(
    val preview: String, // Music preview URL
    val album: String,   // Album picture URL
    val title: Input,    // Title of the track
    val artist: Input,   // Artist of the track
)

data class Input(
    val value: String,
    val answer: Answer = Answer.UNKNOWN
)

enum class Answer {
    CORRECT,
    INCORRECT,
    UNKNOWN
}