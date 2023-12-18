package ie.setu.burnv3.appBars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.BottomAppBar
import androidx.compose.ui.Alignment

@Composable
fun MyBottomAppBar(onAddRoute: () -> Unit) {
    BottomAppBar(
        modifier = Modifier.height(50.dp),
        actions = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(onClick = { onAddRoute() }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Route")
                }
            }
        }
    )
}