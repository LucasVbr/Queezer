package fr.univpau.queezer.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import fr.univpau.queezer.data.filterGames
import fr.univpau.queezer.view.components.GameCardItemList
import fr.univpau.queezer.viewmodel.GameViewModel
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(navController: NavHostController, gameViewModel: GameViewModel) {
    val context = LocalContext.current
    val filter = remember { mutableStateOf(Filter()) }
    val games: List<Game> = gameViewModel.games.observeAsState().value ?: emptyList()

    val nbGames = games.size;
    val averageSuccessRate = games.sumOf { it.score }.div(max(games.size, 1))

    val filteredGames = filterGames(filter.value, games)
    Log.i("ScoreScreen", "filteredGames: $filteredGames")

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
            modifier = Modifier
                .padding(innerPadding),
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
                    Text(text = "$averageSuccessRate%", fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Text(text = "De réussite", fontSize = 14.sp)
                }
            }

            HorizontalDivider()

            // Todo add filters

            GameCardItemList(filteredGames)
        }
    }

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        // Titre des paramètres
//        Text(
//            text = context.resources.getString(R.string.score),
//            fontSize = 32.sp,
//            modifier = Modifier.padding(bottom = 32.dp)
//        )
//
//        //  Nombre de parties jouées
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = context.resources.getString(R.string.games_played),
//                fontSize = 24.sp,
//            )
//            if (gameList?.isNotEmpty() == true) {
//                Text(
//                    text = gameList.size.toString(),
//                    fontSize = 24.sp,
//                )
//            } else {
//                Text(
//                    text = "0",
//                    fontSize = 24.sp,
//                )
//            }
//
//        }
//
//        // pourcentage de réussite moyen
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = context.resources.getString(R.string.average_success_rate),
//                fontSize = 24.sp,
//            )
//            if (gameList?.isNotEmpty() == true) {
//                Text(
//                    text = (gameList.sumOf { it.score }.div(gameList.size).toString()) + "%",
//                    fontSize = 24.sp,
//                )
//            } else {
//                Text(
//                    text = "0%",
//                    fontSize = 24.sp,
//                )
//            }
//        }

    // filtres
    // - date
    // - mode de jeu –titre/artiste/les deux
    // - nombre de titres
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = context.resources.getString(R.string.filters),
//                fontSize = 24.sp,
//            )
//
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween
//            ){
//                Button(
//                    onClick = { /* TODO: Filtre par date */ },
//                    shape = RoundedCornerShape(8.dp),
//                    modifier = Modifier
//                        .padding(vertical = 8.dp)
//                ) {
//                    Text(context.resources.getString(R.string.date))
//                }
//
//                Button(
//                    onClick = { /* TODO: Filtre par date */ },
//                    shape = RoundedCornerShape(8.dp),
//                    modifier = Modifier
//                        .padding(vertical = 8.dp)
//                ) {
//                    Text("Mode de jeu")
//                }
//
//                Button(
//                    onClick = { /* TODO: Filtre par date */ },
//                    shape = RoundedCornerShape(8.dp),
//                    modifier = Modifier
//                        .padding(vertical = 8.dp)
//                ) {
//                    Text("Nombre de titres")
//                }
//            }
//        }

    // Historique des parties (score, nom)
    // - cliquer sur une partie pour voir les détails

//        gameList?.forEach {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = it.playlist.title,
//                    fontSize = 24.sp,
//                )
//                Text(
//                    text = it.score.toString(),
//                    fontSize = 24.sp,
//                )
//            }
//        }

//        if (gameList !== null) {
//            GameList(gameList)
//        }
//
//        Button(
//            onClick = { navController.navigate("home") },
//            shape = RoundedCornerShape(8.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            Text(context.resources.getString(R.string.back))
//        }

    //}
}