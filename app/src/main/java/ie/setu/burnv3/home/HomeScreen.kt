package ie.setu.burnv3.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ie.setu.burnv3.appBars.MyBottomAppBar
import ie.setu.burnv3.appBars.MyTopAppBar
import ie.setu.burnv3.models.Route
import ie.setu.burnv3.models.getUserRoutes
import ie.setu.burnv3.models.signOut

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isDarkTheme: MutableState<Boolean>,
    navController: NavController,
    userId: String,
    drawerState: DrawerState
) {
    var userRoutes by remember { mutableStateOf<List<Route>>(emptyList()) }

    DisposableEffect(userId) {
        getUserRoutes(userId) { receivedRoutes ->
            userRoutes = receivedRoutes
        }
        onDispose { }
    }
    Scaffold(
        topBar = {
            MyTopAppBar(
                drawerState = drawerState,
                isDarkTheme = isDarkTheme,
                onSignOut = { signOut(navController) }
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
                    RoutesList(userId, navController)
                }
            }
        },
        bottomBar = {
            MyBottomAppBar(
                onAddRoute = { navController.navigate("addRoute") }
            )
        }
    )
}


