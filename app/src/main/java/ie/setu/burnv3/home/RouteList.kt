package ie.setu.burnv3.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ie.setu.burnv3.models.Route

@Composable
fun RoutesList(routes: List<Route>, paddingValues: PaddingValues) {
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(routes.size) { index ->
            RouteCard(routes[index])
        }
    }
}
