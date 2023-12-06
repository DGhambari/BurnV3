package ie.setu.burnv3.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Todo: Configure the fonts
        Text("BURN",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
            )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Login or Register Below",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrect = false
            )
        )

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = { navController.navigate("forgotPassword") }) {
            Text("Forgot Password")
        }

//        FilledTonalButton(onClick = { performLogin(email, password, context, onLoginSuccess) }) {
        //TODO revert when finished testing
        FilledTonalButton(onClick = { performLogin("homer@simpson.com", "Homer123", context, onLoginSuccess) }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))
        FilledTonalButton(onClick = { navController.navigate("register") }) {
            Text("Register")
        }
    }
}

private fun performLogin(
    email: String,
    password: String,
    context: Context,
    onLoginSuccess: () -> Unit,
) {
    if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(context, "Email and password must not be empty", Toast.LENGTH_SHORT).show()
        return
    }

    val auth = Firebase.auth
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    onLoginSuccess()
                }
            } else {
                Toast.makeText(
                    context,
                    task.exception?.message ?: "Login failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}



