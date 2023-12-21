package ie.setu.burnv3.login

import android.content.ContentValues
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
import androidx.compose.ui.unit.dp
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(navController: NavController) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("BURN", fontSize = 50.sp)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Enter Email Below", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(12.dp))
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
        FilledTonalButton(onClick = {
            sendPasswordReset(email) { success ->
                if (success) {
                    navController.navigate("login")
                } else {
                    Toast.makeText(context, "Failed to send password reset email", Toast.LENGTH_LONG).show()
                }
            }
        }) {
            Text("Reset Password")
        }
    }
}

fun sendPasswordReset(email: String, onResult: (Boolean) -> Unit) {
    Firebase.auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(ContentValues.TAG, "Password Reset sent.")
                onResult(true)
            } else {
                Log.e(ContentValues.TAG, "Password Reset failed.")
                onResult(false)
            }
        }
}