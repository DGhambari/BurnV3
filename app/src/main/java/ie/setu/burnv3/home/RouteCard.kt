package ie.setu.burnv3.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ie.setu.burnv3.R
import ie.setu.burnv3.models.Route

@Composable
fun RouteCard(route: Route, navController: NavController, ) {
    var isFavorite by remember { mutableStateOf(route.isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .height(300.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val imagePainter = if (route.imageUrl.isNullOrEmpty()) {
                painterResource(id = R.drawable.sample3)
            } else {
                rememberAsyncImagePainter(route.imageUrl)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = "Route Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Edit button
                IconButton(
                    onClick = {
                        route.id?.let { routeId ->
                            navController.navigate("editRoute/${routeId}")
                        } ?: run {
                            Log.e("Cards", "Route id is null")
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
//                        .padding(top = 12.dp, end = 12.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                }
                // Favorite button
                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        updateFavoriteStatus(route.id, isFavorite)
                    },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = route.county,
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall)
                Text(text = route.area,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium)
                Text(text = route.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

fun updateFavoriteStatus(routeId: String?, isFavorite: Boolean){
    val db = Firebase.firestore
    if (routeId != null) {
        db.collection("routes").document(routeId)
            .update("isFavorite", isFavorite)
            .addOnSuccessListener {
                Log.d("Firestore", "Route favorite status updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating route favorite status", e)
            }
    } else {
        Log.e("Firestore", "Route ID is null")
    }
}