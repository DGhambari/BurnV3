package ie.setu.burnv3.appBars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    drawerState: DrawerState,
    isDarkTheme: MutableState<Boolean>,
    onSignOut: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    drawerState.open()
                }
            }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        title = {
            Text(
                "Your Routes",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(onClick = { isDarkTheme.value = !isDarkTheme.value }) {
                Icon(
                    imageVector = if (isDarkTheme.value) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                    contentDescription = "Switch Theme"
                )
            }
            IconButton(onClick = { onSignOut() }) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Sign Out")
            }
        }
    )
}
