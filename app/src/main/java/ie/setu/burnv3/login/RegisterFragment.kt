package ie.setu.burnv3.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegistrationSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Todo: Configure the fonts
        Text("BURN", fontSize = 50.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Register Below", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
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
        TextField(
            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
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
        Spacer(modifier = Modifier.height(16.dp))
        FilledTonalButton(onClick = {
            //ToDo: combine these parameters into one model and pass the model by itself instead
            performRegistration(email, password, confirmPassword, firstName, lastName, context, onRegistrationSuccess)
        }) {
            Text("Register")
        }
    }
}

private fun performRegistration(
    email: String,
    password: String,
    confirmPassword: String,
    firstName: String,
    lastName: String,
    context: Context,
    onRegistrationSuccess: () -> Unit
) {
    if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        return
    }
    if (password != confirmPassword) {
        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
        return
    }

    Firebase.auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUser = task.result?.user
                firebaseUser?.let { user ->
                    val userDocument = hashMapOf(
                        "userId" to user.uid,
                        "email" to user.email,
                        "firstName" to firstName,
                        "lastName" to lastName,
                    )
                    val db = Firebase.firestore
                    db.collection("users").document(user.uid)
                        .set(userDocument)
                        .addOnSuccessListener {
                            onRegistrationSuccess()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Failed to create user profile: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
            } else {
                Toast.makeText(
                    context,
                    task.exception?.message ?: "Registration failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}
