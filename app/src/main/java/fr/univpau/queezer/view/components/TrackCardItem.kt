package fr.univpau.queezer.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.univpau.queezer.data.Answer
import fr.univpau.queezer.data.Track

@Composable
fun TrackCardItem(track: Track) {
    val titleColor = when (track.title.answer) {
        Answer.CORRECT -> {
            MaterialTheme.colorScheme.primary
        }
        Answer.INCORRECT -> {
            MaterialTheme.colorScheme.error
        }
        else -> {
            MaterialTheme.colorScheme.onSurface
        }
    }

    val artistColor = when (track.artist.answer) {
        Answer.CORRECT -> {
            MaterialTheme.colorScheme.primary
        }
        Answer.INCORRECT -> {
            MaterialTheme.colorScheme.error
        }
        else -> {
            MaterialTheme.colorScheme.onSurface
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = track.title.value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor
            )

            Text(text = track.artist.value, color = artistColor)
        }
    }
}