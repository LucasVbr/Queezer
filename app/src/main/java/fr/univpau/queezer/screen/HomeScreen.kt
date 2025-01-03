package fr.univpau.queezer.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.univpau.queezer.ui.theme.Purple40

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo et Titre
//        Image(
//            painter = painterResource(id = R.drawable.logo),
//            contentDescription = "Logo Queezer",
//            modifier = Modifier
//                .size(120.dp)
//                .padding(16.dp),
//            contentScale = ContentScale.Crop
//        )
        Text(
            text = "Queezer",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Purple40
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Boutons
        Button(
            onClick = { navController.navigate("game") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Partie rapide")
        }

        Button(
            onClick = { /* TODO: Partie personnalisée */ },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Partie personnalisée")
        }

        Button(
            onClick = { navController.navigate("settings") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Paramètres")
        }

        Button(
            onClick = { /* TODO: Scores */ },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Scores")
        }
    }
}