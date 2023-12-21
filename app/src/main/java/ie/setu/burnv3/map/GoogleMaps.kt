package ie.setu.burnv3.map

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.burnv3.models.Route
import ie.setu.burnv3.models.getUserId
import ie.setu.burnv3.models.getUserRoutes
import kotlinx.coroutines.launch

@Composable
fun GoogleMapRoutesList(modifier: Modifier = Modifier) {
    val mapView = rememberMapView()
    var userId = getUserId()
    var userRoutes by remember { mutableStateOf<List<Route>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        scope.launch {
            getUserRoutes(userId) { receivedRoutes ->
                userRoutes = receivedRoutes
                Log.d("GoogleMapComposable", "Routes fetched: ${userRoutes.size}")
                mapView.getMapAsync { googleMap ->
                    setupRoutesListMap(googleMap, userRoutes)
                }
            }
        }
    }

    AndroidView(
        { mapView },
        modifier = modifier
    ) { mapView ->
    }
}

@Composable
fun GoogleMapAddMarker(modifier: Modifier = Modifier, onLocationSelected: (LatLng) -> Unit) {
    val mapView = rememberMapView()
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    AndroidView(
        { mapView },
        modifier = modifier
    ) { mapView ->
        mapView.getMapAsync { googleMap ->
            setupAddMarkerMap(googleMap, selectedLocation) { latLng ->
                selectedLocation = latLng
                onLocationSelected(latLng)
            }
        }
    }
}

fun setupAddMarkerMap(googleMap: GoogleMap, selectedLocation: LatLng?, onMapClick: (LatLng) -> Unit) {
    val defaultLocation = selectedLocation ?: LatLng(46.5724, 8.4149)
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 11f))
    googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    googleMap.uiSettings.isZoomControlsEnabled = true

    googleMap.setOnMapClickListener { latLng ->
        googleMap.clear()
        googleMap.addMarker(
            MarkerOptions().position(latLng).draggable(true)
        )
        onMapClick(latLng)
    }

    googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
        override fun onMarkerDragStart(marker: Marker) {
        }

        override fun onMarkerDrag(marker: Marker) {
        }

        override fun onMarkerDragEnd(marker: Marker) {
            val newLocation = marker.position
            onMapClick(newLocation)
        }
    })

    selectedLocation?.let {
        googleMap.addMarker(
            MarkerOptions().position(it).draggable(true)
        )
    }
}


@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current
    val mapView = MapView(context, GoogleMapOptions())

    mapView.onCreate(Bundle())
    mapView.onResume()

    DisposableEffect(mapView) {
        onDispose {
            mapView.onPause()
            mapView.onDestroy()
        }
    }
    return mapView
}

fun setupRoutesListMap(googleMap: GoogleMap, routes: List<Route>) {
    val defaultLocation = LatLng(46.5724, 8.4149)
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 11f))

    googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    googleMap.uiSettings.isZoomControlsEnabled = true

    Log.d(
        "setupGoogleMap",
        "setupGoogleMap has been called. Routes: ${routes.size}"
    )

    routes.forEach { route ->
        Log.d("setupGoogleMap", "Processing route: ${route.id}")
        if (route.startLat != 0.0 && route.startLng != 0.0 && route.stopLat != 0.0 && route.stopLng != 0.0) {
            val startLatLng = LatLng(route.startLat, route.startLng)
            val stopLatLng = LatLng(route.stopLat, route.stopLng)

            googleMap.addMarker(
                MarkerOptions().position(startLatLng)
                    .title("${route.county} - ${route.area} (Route Start)")
            )
            googleMap.addMarker(
                MarkerOptions().position(stopLatLng)
                    .title("${route.county} - ${route.area} (Route End)")
            )
        } else {
            Log.d("Routes", "Invalid coordinates for route: ${route.id}")
        }
    }
}
