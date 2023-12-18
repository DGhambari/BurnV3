package ie.setu.burnv3.navDrawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ie.setu.burnv3.models.getUserProfile


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawer(
    drawerState: DrawerState,
    navController: NavController,
    content: @Composable () -> Unit
) {
    //val scope = rememberCoroutineScope()
    val user = getUserProfile()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                if (user != null){
                    //TODO fill in user information
                    Text("${user.email}", modifier = Modifier.padding(16.dp))
                    Divider()

                    Spacer(modifier = Modifier.height(8.dp))
                    NavigationDrawerItem(
                        label = { Text(text = "Routes") },
                        selected = navController.currentDestination?.route == "home",
                        onClick = { navController.navigate("home") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NavigationDrawerItem(
                        label = { Text(text = "Map") },
                        selected = false,
                        onClick = { navController.navigate("map") }
                    )
                }
                else {
                    Text("Please Sign In", modifier = Modifier.padding(16.dp))
                }

                Box(modifier = Modifier.fillMaxSize().padding(bottom = 16.dp), contentAlignment = Alignment.BottomCenter) {
                    Text("Burn V3.0", style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true,
    )
    {
        content()
    }
}
