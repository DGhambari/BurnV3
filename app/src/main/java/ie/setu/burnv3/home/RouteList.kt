package ie.setu.burnv3.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import ie.setu.burnv3.models.Route
import ie.setu.burnv3.models.getUserRoutes
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

//@Composable
// fun RoutesList(routes: List<Route>, paddingValues: PaddingValues) {
//    LazyColumn(modifier = Modifier.padding(paddingValues)) {
//        items(routes.size) { index ->
//            RouteCard(routes[index])
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesList(userId: String, navController: NavController) {
    var routes by remember { mutableStateOf<List<Route>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        getUserRoutes(userId) { receivedRoutes ->
            routes = receivedRoutes
        }
        onDispose { }
    }

    val filteredRoutes = routes.filter {
        it.county.contains(searchQuery, ignoreCase = true) ||
                it.area.contains(searchQuery, ignoreCase = true)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text("Search routes") },
            singleLine = true
        )
        LazyColumn(modifier = Modifier.padding(PaddingValues(8.dp))) {
            items(filteredRoutes) { route ->
                if (route.id != null) {
                    RouteCard(route, navController)
                } else {
                    Log.e("Routes", "Route id is null")
                }
            }
        }
    }
}