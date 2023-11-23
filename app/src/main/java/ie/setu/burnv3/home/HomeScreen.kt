package ie.setu.burnv3.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.*
import ie.setu.burnv3.models.sampleRoutes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(isDarkTheme: MutableState<Boolean>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Routes") },

                // TODO: Complete nav drawer at a later stage
//                navigationIcon = {
//                    IconButton(onClick = {  }) {
//                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
//                    }
//                },
                actions = {
                    Switch(
                        checked = isDarkTheme.value,
                        onCheckedChange = { isDarkTheme.value = it }
                    )
                }
            )
        },
        content = { paddingValues ->
            RoutesList(sampleRoutes, paddingValues)
        }
    )
}
