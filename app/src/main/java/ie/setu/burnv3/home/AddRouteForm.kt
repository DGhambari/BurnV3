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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ie.setu.burnv3.images.ImagePicker
import ie.setu.burnv3.models.Route
import ie.setu.burnv3.models.getUserId

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AddRouteForm(navController: NavController) {
    var county by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startLatStr by remember { mutableStateOf("0.0") }
    var startLngStr by remember { mutableStateOf("0.0") }
    var stopLatStr by remember { mutableStateOf("0.0") }
    var stopLngStr by remember { mutableStateOf("0.0") }
    val context = LocalContext.current
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var isImageUploading by remember { mutableStateOf(false) }
    val maxDescriptionChar = 200
    val maxHeadingChar = 20

    //    if (routeType == "start") {
//        startLat = lat
//        startLng = lng
//    } else if (routeType == "end") {
//        stopLat = lat
//        stopLng = lng
//    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            modifier = Modifier
                .padding(horizontal = 39.dp)
                .clip(shape = RoundedCornerShape(10.dp)),
            value = county,
            onValueChange = {
                if (it.length <= maxHeadingChar)
                    county = it
            },
            singleLine = true,
            label = { Text("County") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier
                .padding(horizontal = 39.dp)
                .clip(shape = RoundedCornerShape(10.dp)),
            value = area,
            maxLines = 1,
            onValueChange = {
                if (it.length <= maxHeadingChar)
                    area = it
            },
            singleLine = true,
            label = { Text("Area") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier
                .padding(horizontal = 39.dp)
                .clip(shape = RoundedCornerShape(10.dp)),
            value = description,
            maxLines = 1,
            onValueChange = {
                if (it.length <= maxDescriptionChar)
                    description = it
            },
            singleLine = true,
            label = { Text("Description") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Start Location",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 39.dp)
        ) {
            TextField(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .weight(1f),
                value = startLatStr,
                maxLines = 1,
                onValueChange = { startLatStr = it },
                singleLine = true,
                label = { Text("Lat") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .weight(1f),
                value = startLngStr,
                maxLines = 1,
                onValueChange = { startLngStr = it },
                singleLine = true,
                label = { Text("Lng") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "End Location",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 39.dp)
        ) {
            TextField(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .weight(1f),
                value = stopLatStr,
                maxLines = 1,
                onValueChange = { stopLatStr = it },
                singleLine = true,
                label = { Text("Lat") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .weight(1f),
                value = stopLngStr,
                maxLines = 1,
                onValueChange = { stopLngStr = it },
                singleLine = true,
                label = { Text("Lng") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }


//        Button(onClick = { navController.navigate("mapAddMarker/start") }) {
//            Text("Select Route Start Location on Map")
//        }
//
//        Button(onClick = { navController.navigate("mapAddMarker/end") }) {
//            Text("Select Route End Location on Map")
//        }

        ImagePicker(
            initialImageUrl = null,
            onImageUploaded = { url -> imageUrl = url },
            onImageUploading = { uploading -> isImageUploading = uploading }
        )
        if (isImageUploading) {
            CircularProgressIndicator()
        } else {
            FilledTonalButton(
                onClick = {
                    val newRoute = Route(
                        county = county,
                        area = area,
                        description = description,
                        imageUrl = imageUrl,
                        startLat = startLatStr.toDouble(),
                        startLng = startLngStr.toDouble(),
                        stopLat = stopLatStr.toDouble(),
                        stopLng = stopLngStr.toDouble()
                    )
                    addRouteToFirestore(newRoute, getUserId(), context, navController)
                },
                enabled = imageUrl != null
            ) {
                Text("Add Route")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigateUp() }) {
            Text("Cancel")
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
        "stopLng" to route.stopLng,
        "imageUrl" to route.imageUrl
    )

    db.collection("routes").add(routeData)
        .addOnSuccessListener { documentReference ->
            val routeId = documentReference.id
            db.collection("routes").document(routeId)
                .update("routeId", routeId)
                .addOnSuccessListener {
                    Toast.makeText(context, "Route added successfully!", Toast.LENGTH_SHORT)
                        .show()
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
            Toast.makeText(context, "Failed to add route: ${e.message}", Toast.LENGTH_LONG)
                .show()
        }
}
