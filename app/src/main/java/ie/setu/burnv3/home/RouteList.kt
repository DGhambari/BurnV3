package ie.setu.burnv3.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import ie.setu.burnv3.models.Route
import ie.setu.burnv3.models.getUserRoutes
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

//@Composable
// fun RoutesList(routes: List<Route>, paddingValues: PaddingValues) {
//    LazyColumn(modifier = Modifier.padding(paddingValues)) {
//        items(routes.size) { index ->
//            RouteCard(routes[index])
//        }
//    }
//}

@Composable
fun RoutesList(userId: String) {
    var routes by remember { mutableStateOf<List<Route>>(emptyList()) }

    DisposableEffect(Unit) {
        getUserRoutes(userId) { receivedRoutes ->
            routes = receivedRoutes
        }
        onDispose { }
    }

    LazyColumn(modifier = Modifier.padding(PaddingValues(8.dp))) {
        items(routes) { route ->
            RouteCard(route)
        }
    }
}