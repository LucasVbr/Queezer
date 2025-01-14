package fr.univpau.queezer.view.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.univpau.queezer.R
import fr.univpau.queezer.data.GameMode
import fr.univpau.queezer.manager.loadSettings
import fr.univpau.queezer.manager.saveSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, saveLocation: String = "settings") {
    val context = LocalContext.current
    val settings = remember { mutableStateOf(loadSettings(context, saveLocation)) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = context.resources.getString(R.string.settings),
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
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        try {
                            settings.value.validate(context)
                            saveSettings(context, settings.value, saveLocation)
                            navController.popBackStack()
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(context.resources.getString(R.string.submit)) }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    label = { Text(context.resources.getString(R.string.playlist_url_label)) },
                    placeholder = { Text(context.resources.getString(R.string.playlist_url_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    value = settings.value.playlistUrl,
                    onValueChange = { settings.value = settings.value.copy(playlistUrl = it) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri),
                )

                TextField(
                    label = { Text(context.resources.getString(R.string.tracks_count_label)) },
                    modifier = Modifier.fillMaxWidth(1f),
                    value = settings.value.numberOfTitles?.toString() ?: "",
                    onValueChange = {
                        settings.value = settings.value.copy(numberOfTitles = it.toIntOrNull())
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Mode de jeu", fontSize = 18.sp)
                    val gameModes = context.resources.getStringArray(R.array.game_modes)
                    gameModes.forEachIndexed { index, label ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.selectable(
                                selected = (settings.value.gameMode.ordinal == index),
                                onClick = {
                                    settings.value =
                                        settings.value.copy(gameMode = GameMode.entries[index])
                                },
                                role = Role.RadioButton
                            )
                        ) {
                            RadioButton(
                                selected = settings.value.gameMode.ordinal == index,
                                onClick = {
                                    settings.value =
                                        settings.value.copy(gameMode = GameMode.entries[index])
                                }
                            )
                            Text(text = label)
                        }

                    }
                }
            }
        }
    }
}
