package ie.setu.burnv3.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ie.setu.burnv3.images.ImagePicker
import ie.setu.burnv3.models.Route

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun EditRouteForm(routeId: String, navController: NavController) {
    var county by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var currentImageUrl by remember { mutableStateOf<String?>(null) }
    var isImageLoading by remember { mutableStateOf(false) }

    LaunchedEffect(routeId) {
        loadRouteData(routeId) { fetchedRoute ->
            county = fetchedRoute.county
            area = fetchedRoute.area
            description = fetchedRoute.description
            currentImageUrl = fetchedRoute.imageUrl
            Log.d("EditRouteForm", "Loaded Image URL: $currentImageUrl")
            isImageLoading = false
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
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
            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
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
            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Log.d("EditRouteForm", "Passing Image URL to Picker: $currentImageUrl")
        ImagePicker(
            initialImageUrl = currentImageUrl,
            onImageUploaded = { newImageUrl -> currentImageUrl = newImageUrl },
            onImageUploading = { uploading -> isImageLoading = uploading }
        )

        if (!isImageLoading) {
            FilledTonalButton(onClick = {
                val updatedRoute = Route(
                    id = routeId,
                    county = county,
                    area = area,
                    description = description,
                    imageUrl = currentImageUrl
                )
                updateRouteInFirestore(updatedRoute, context, navController)
            }) {
                Text("Update Route")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = { showDeleteConfirmationDialog = true }) {
            Text("Delete Route")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigateUp() }) {
            Text("Cancel")
        }
        if (showDeleteConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmationDialog = false },
                title = { Text("Delete Route") },
                text = { Text("Are you sure you want to delete this route?") },
                confirmButton = {
                    TextButton(onClick = { deleteRoute(routeId, context, navController) }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmationDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

fun deleteRoute(routeId: String?, context: Context, navController: NavController) {
    val db = Firebase.firestore
    if (routeId == null) {
        Toast.makeText(context, "Route ID is null", Toast.LENGTH_LONG).show()
        return
    }
    db.collection("routes").document(routeId)
        .delete()
        .addOnSuccessListener {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Failed to delete route: ${e.message}", Toast.LENGTH_LONG)
                .show()
        }
}

fun loadRouteData(routeId: String, onRouteLoaded: (Route) -> Unit) {
    val db = Firebase.firestore
    db.collection("routes").document(routeId).get()
        .addOnSuccessListener { document ->
            val route = document.toObject(Route::class.java)
            route?.let { onRouteLoaded(it) }
        }
}

fun updateRouteInFirestore(
    route: Route,
    context: Context,
    navController: NavController
) {
    val db = Firebase.firestore
    val routeId = route.id

    if (routeId != null) {
        val routeData = mapOf(
            "county" to route.county,
            "area" to route.area,
            "description" to route.description,
            "imageUrl" to route.imageUrl
        ).filterValues { it != null }

        db.collection("routes").document(routeId)
            .update(routeData)
            .addOnSuccessListener {
                Toast.makeText(context, "Route updated successfully!", Toast.LENGTH_SHORT).show()
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to update route: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
    } else {
        Toast.makeText(context, "Update Route: Route id is null", Toast.LENGTH_LONG)
            .show()
    }

}