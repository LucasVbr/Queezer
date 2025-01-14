package fr.univpau.queezer.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.univpau.queezer.data.Answer
import fr.univpau.queezer.data.Track

@Composable
fun TrackCardItem(track: Track) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = track.title.value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = getColor(track.title.answer)
            )

            Text(text = track.artist.value, color = getColor(track.artist.answer))
        }
    }
}

@Composable
fun getColor(answer: Answer): Color {
    return when (answer) {
        Answer.CORRECT -> Color(0xFF4CAF50)
        Answer.INCORRECT -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }
}