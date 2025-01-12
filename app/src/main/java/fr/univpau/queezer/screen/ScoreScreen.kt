package fr.univpau.queezer.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.univpau.queezer.R
import fr.univpau.queezer.data.Game
import fr.univpau.queezer.viewmodel.GameViewModel

@Composable
fun ScoreScreen(navController: NavHostController, gameViewModel: GameViewModel) {
    val context = LocalContext.current
    val gameList : List<Game>? = gameViewModel.games.observeAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Titre des paramètres
        Text(
            text = context.resources.getString(R.string.score),
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        //  Nombre de parties jouées
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = context.resources.getString(R.string.games_played),
                fontSize = 24.sp,
            )
            if (gameList?.isNotEmpty() == true) {
                Text(
                    text = gameList.size.toString(),
                    fontSize = 24.sp,
                )
            } else {
                Text(
                    text = "0",
                    fontSize = 24.sp,
                )
            }

        }

        // pourcentage de réussite moyen
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = context.resources.getString(R.string.average_success_rate),
                fontSize = 24.sp,
            )
            if (gameList?.isNotEmpty() == true) {
                Text(
                    text = (gameList.sumOf { it.score }.div(gameList.size).toString()) + "%",
                    fontSize = 24.sp,
                )
            } else {
                Text(
                    text = "0%",
                    fontSize = 24.sp,
                )
            }
        }

        // filtres
        // - date
        // - mode de jeu –titre/artiste/les deux
        // - nombre de titres
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = context.resources.getString(R.string.filters),
                fontSize = 24.sp,
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Button(
                    onClick = { /* TODO: Filtre par date */ },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                ) {
                    Text(context.resources.getString(R.string.date))
                }

                Button(
                    onClick = { /* TODO: Filtre par date */ },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                ) {
                    Text("Mode de jeu")
                }

                Button(
                    onClick = { /* TODO: Filtre par date */ },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                ) {
                    Text("Nombre de titres")
                }
            }
        }

        // Historique des parties (score, nom)
        // - cliquer sur une partie pour voir les détails

        gameList?.forEach {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = it.playlist.title,
                    fontSize = 24.sp,
                )
                Text(
                    text = it.score.toString(),
                    fontSize = 24.sp,
                )
            }
        }

        Button(
            onClick = { navController.navigate("home") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(context.resources.getString(R.string.back))
        }

    }
}