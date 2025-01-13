package fr.univpau.queezer.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.univpau.queezer.data.Game
import fr.univpau.queezer.data.GameMode
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameCardItem(game: Game) {
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val showBottomSheet = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val maxScore: Int = if (game.settings.gameMode == GameMode.ALL) {
        (game.settings.numberOfTitles ?: 1) * 2
    } else {
        (game.settings.numberOfTitles ?: 1)
    }

    Card(
        onClick = { showBottomSheet.value = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = game.playlist.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = formatter.format(game.date))

            Text(text = "${game.score}/$maxScore")
        }
    }

    // Modal BottomSheet
    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false // Fermer la BottomSheet au clic en dehors
            }
        ) {
            // Contenu de la BottomSheet
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = game.playlist.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${game.score}/$maxScore",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    text = formatter.format(game.date),
                )

                TrackCardItemList(game.playlist.tracks)
            }
        }
    }
}