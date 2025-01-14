package fr.univpau.queezer.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.univpau.queezer.R
import fr.univpau.queezer.data.Filter
import fr.univpau.queezer.data.Game
import fr.univpau.queezer.data.GameMode
import fr.univpau.queezer.data.filterGames
import fr.univpau.queezer.view.components.GameCardItemList
import fr.univpau.queezer.viewmodel.GameViewModel
import java.util.Locale
import java.util.concurrent.Flow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ScoreScreen(navController: NavHostController, gameViewModel: GameViewModel) {
    val context = LocalContext.current
    var filter by remember { mutableStateOf(Filter()) }
    val games: List<Game> = gameViewModel.games.observeAsState().value ?: emptyList()

    val filteredGames by remember(filter, games) { derivedStateOf { filterGames(filter, games) } }
    val averageSuccessRate by remember(filteredGames) { derivedStateOf { calculateAverageSuccessRate(filteredGames) } }
    val nbGames by remember(filteredGames) { derivedStateOf { filteredGames.size } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = context.resources.getString(R.string.score),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = context.resources.getString(R.string.back)
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$nbGames", fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Text(text = "Parties jouées", fontSize = 14.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${String.format(Locale.getDefault(), "%.02f", averageSuccessRate)}%", fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Text(text = "De réussite", fontSize = 14.sp)
                }
            }

            HorizontalDivider()

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Mode de jeu", fontSize = 16.sp)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        val gameModesLabels = context.resources.getStringArray(R.array.game_modes)
                        filter.mode.entries.forEachIndexed { index, entry ->
                            FilterChip(
                                selected = entry.value,
                                onClick = {
                                    filter = filter.copy(mode = filter.mode.toMutableMap().apply {
                                        this[entry.key] = !entry.value
                                    })
                                },

                                leadingIcon = {
                                    if (entry.value) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Filtre activé",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                },

                                label = { Text(
                                    gameModesLabels[index],
                                    maxLines = 1,
                                ) },
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Filtrer par", fontSize = 16.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Date")

                        Switch(
                            checked = filter.orderByNbTitle,
                            onCheckedChange = {
                                filter = filter.copy(orderByNbTitle = it)
                            }
                        )

                        Text("Nombre de titres")
                    }
                }
            }

            if (filteredGames.isEmpty()) {
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Aucune partie trouvée... (｡•́︿•̀｡)", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            } else {
                GameCardItemList(filteredGames)
            }
        }
    }
}

fun calculateAverageSuccessRate(games: List<Game>): Double {
    if (games.isEmpty()) return 0.0

    val averageGameScore = games.sumOf {
        if (it.settings.gameMode == GameMode.ALL) {
            it.score.toDouble() / (it.settings.numberOfTitles!! * 2)
        } else {
            it.score.toDouble() / (it.settings.numberOfTitles ?: 1)
        }
    }

    return (averageGameScore / games.size) * 100
}