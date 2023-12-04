package ie.setu.burnv3.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ie.setu.burnv3.login.LoginScreen
import ie.setu.burnv3.login.RegisterScreen
import ie.setu.burnv3.home.HomeScreen
import ie.setu.burnv3.home.AddRouteForm
import ie.setu.burnv3.home.EditRouteForm
import ie.setu.burnv3.models.getUserId
import ie.setu.burnv3.ui.theme.BurnV3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme = remember { mutableStateOf(true) }

            BurnV3Theme(useDarkTheme = isDarkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(isDarkTheme)
                }
            }
        }
    }

    @Composable
    fun AppNavigation(isDarkTheme: MutableState<Boolean>) {
        val navController = rememberNavController()
        val context = LocalContext.current
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(navController, onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }
            composable("register") {
                RegisterScreen(onRegistrationSuccess = {
                    navController.navigate("login")
                })
            }
            composable("home") {
                HomeScreen(isDarkTheme, navController, getUserId())
            }
            composable("addRoute") {
                AddRouteForm(navController)
            }
            composable("editRoute/{routeId}") { backStackEntry ->
                val routeId = backStackEntry.arguments?.getString("routeId")
                if (routeId != null) {
                    EditRouteForm(routeId, navController)
                } else {
                    Toast.makeText(context, "Error retrieving route id", Toast.LENGTH_LONG).show()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        }
    }
}
