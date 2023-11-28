package ie.setu.burnv3.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ie.setu.burnv3.models.Route
import ie.setu.burnv3.models.getUserRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isDarkTheme: MutableState<Boolean>,
    navController: NavController,
    userId: String
) {
    var userRoutes by remember { mutableStateOf(emptyList<Route>()) }

    DisposableEffect(userId) {
        getUserRoutes(userId) { receivedRoutes ->
            userRoutes = receivedRoutes
        }
        onDispose { }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Routes") },
                actions = {
                    Switch(
                        checked = isDarkTheme.value,
                        onCheckedChange = { isDarkTheme.value = it }
                    )
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (userRoutes.isEmpty()) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Button(onClick = { navController.navigate("addRoute") }) {
                            Text("Add Route")
                        }
                    }
                } else {
                    RoutesList(userId)
                }
            }
        }
    )
}