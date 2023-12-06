package ie.setu.burnv3.appbars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    isDarkTheme: MutableState<Boolean>,
    onSignOut: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text("Your Routes", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 20.sp) },
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
