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
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import ie.setu.burnv3.R
import ie.setu.burnv3.models.Route

@Composable
fun RouteCard(route: Route, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(200.dp)
    ) {
        Box {
            val imagePainter = if (route.imageUrl.isNullOrEmpty()) {
                painterResource(id = R.drawable.sample3)
            } else {
                rememberAsyncImagePainter(route.imageUrl)
            }
            Image(
                painter = imagePainter,
                contentDescription = "Route Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(text = route.county, color = Color.White)
                Text(text = route.area, color = Color.White)
                Text(text = route.description, color = Color.White)
                //Text(text = route.length.toString(), color = Color.White)
            }
            IconButton(
                onClick = {
                    route.id?.let { routeId ->
                        navController.navigate("editRoute/${routeId}")
                    }
                        ?: run {
                            Log.e("Cards", "Route id is null")
                        }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp),
            ) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit")
            }
        }
    }
}