package ie.setu.burnv3.activities

import android.os.Bundle
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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ie.setu.burnv3.login.LoginScreen
import ie.setu.burnv3.login.RegisterScreen
import ie.setu.burnv3.home.HomeScreen
import ie.setu.burnv3.ui.theme.BurnV3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme = remember { mutableStateOf(false) }

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
                HomeScreen(isDarkTheme)
            }
        }
    }
}
