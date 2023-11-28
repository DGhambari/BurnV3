package ie.setu.burnv3.home

import android.widget.Toast
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ie.setu.burnv3.models.Route
import ie.setu.burnv3.models.getUserId

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AddRouteForm(navController: NavController) {
    var county by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            modifier = Modifier.clip(shape = RoundedCornerShape(20.dp)),
            value = county,
            onValueChange = { county = it },
            label = { Text("County") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.clip(shape = RoundedCornerShape(20.dp)),
            value = area,
            onValueChange = { area = it },
            label = { Text("Area") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.clip(shape = RoundedCornerShape(20.dp)),
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        FilledTonalButton(onClick = {
            val newRoute = Route(county = county, area = area, description = description)
            addRouteToFirestore(newRoute, getUserId(), context, navController)
        }) {
            Text("Add Route")
        }
    }
}

fun addRouteToFirestore(
    route: Route,
    userId: String,
    context: Context,
    navController: NavController
) {
    val db = Firebase.firestore

    val routeData = hashMapOf(
        "userId" to userId,
        "county" to route.county,
        "area" to route.area,
        "description" to route.description,
        "length" to route.length,
        "startLat" to route.startLat,
        "startLng" to route.startLng,
        "stopLat" to route.stopLat,
        "stopLng" to route.stopLng
    )

    db.collection("routes").add(routeData)
        .addOnSuccessListener { documentReference ->
            val routeId = documentReference.id
            db.collection("routes").document(routeId)
                .update("routeId", routeId)
                .addOnSuccessListener {
                    Toast.makeText(context, "Route added successfully!", Toast.LENGTH_SHORT).show()
                    //route.id = routeId
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Failed to update route ID: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Failed to add route: ${e.message}", Toast.LENGTH_LONG).show()
        }
}
