package ie.setu.burnv3.navDrawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import coil.compose.rememberAsyncImagePainter
import ie.setu.burnv3.models.getUserProfile
import ie.setu.burnv3.models.logOut


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawer(
    drawerState: DrawerState,
    navController: NavController,
    content: @Composable () -> Unit
) {
    val user = getUserProfile()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                if (user != null) {
                    //TODO Removed for now until the user creation form accommodates adding a profile picture
//                    Image(
//                        painter = rememberAsyncImagePainter(user.photoUrl),
//                        contentDescription = "Profile Picture",
//                        modifier = Modifier
//                            .size(100.dp)
//                            .align(Alignment.CenterHorizontally)
//                    )
                    Text(
                        "${user.email}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Divider()

                    Spacer(modifier = Modifier.height(8.dp))
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.Route, contentDescription = "Routes") },
                        label = { Text(text = "Routes") },
                        selected = false,
                        onClick = { navController.navigate("home") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.Map, contentDescription = "Map") },
                        label = { Text(text = "Map") },
                        selected = false,
                        onClick = { navController.navigate("mapListRoutes") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()

                    Spacer(modifier = Modifier.height(8.dp))
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.Logout, contentDescription = "Log Out") },
                        label = { Text(text = "Log Out") },
                        selected = false,
                        onClick = { logOut(navController) }
                    )
                } else {
                    Text("Please Log In", modifier = Modifier.padding(16.dp))
                    Divider()

                    Spacer(modifier = Modifier.height(8.dp))
                    NavigationDrawerItem(
                        label = { Text(text = "Log In") },
                        selected = false,
                        onClick = { navController.navigate("login") }
                    )
                }
                Divider()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text("Burn V3.0", style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
    )
    {
        content()
    }
}
