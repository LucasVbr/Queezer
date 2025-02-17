package fr.univpau.queezer.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.univpau.queezer.R
import fr.univpau.queezer.ui.theme.Purple40

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current;

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo foreground et background
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = "Logo Queezer",
            modifier = Modifier.size(140.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = context.resources.getString(R.string.app_name),
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
            Text(
                context.resources.getString(R.string.quick_play),
                fontSize = 18.sp
            )
        }

        Button(
            onClick = {
                navController.navigate("custom_settings")
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(context.resources.getString(R.string.custom_play), fontSize = 18.sp)
        }

        Button(
            onClick = { navController.navigate("settings") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text( context.resources.getString(R.string.settings), fontSize = 18.sp)
        }

        Button(
            onClick = { navController.navigate("score") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(context.resources.getString(R.string.score), fontSize = 18.sp)
        }
    }
}