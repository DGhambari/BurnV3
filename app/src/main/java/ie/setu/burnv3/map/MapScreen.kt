package ie.setu.burnv3.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point

@Composable
fun MapScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapBoxMap(
            point = Point.fromLngLat(-0.6333, 35.6971),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}