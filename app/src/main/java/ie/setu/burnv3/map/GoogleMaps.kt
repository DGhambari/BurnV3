package ie.setu.burnv3.map

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.burnv3.models.Route
import ie.setu.burnv3.models.getUserRoutes

@Composable
fun GoogleMapComposable(modifier: Modifier = Modifier, userId: String, isMapActive: MutableState<Boolean>) {
    val mapView = rememberMapView()
    var routes by remember { mutableStateOf<List<Route>>(emptyList()) }

    LaunchedEffect(userId) {
        getUserRoutes(userId) { receivedRoutes ->
            routes = receivedRoutes
        }
    }

    AndroidView(
        { mapView },
        modifier = modifier
    ) { mapView ->
        mapView.getMapAsync { googleMap ->
            setupGoogleMap(googleMap, routes, isMapActive)
        }
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

fun setupGoogleMap(googleMap: GoogleMap, routes: List<Route>, isMapActive: MutableState<Boolean>) {
    val defaultLocation = LatLng(46.5724, 8.4149)
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 11f))

    googleMap.setOnCameraMoveStartedListener {
        isMapActive.value = true
    }

    googleMap.setOnCameraIdleListener {
        isMapActive.value = false
    }

    googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    googleMap.uiSettings.isZoomControlsEnabled = true

    routes.forEach { route ->
        val startLatLng = LatLng(route.startLat, route.startLng)
        val stopLatLng = LatLng(route.stopLat, route.stopLng)

        Log.d("startLatLng:", "${route.startLat}, startLng: ${route.startLng}")
        Log.d("stopLatLng:", "${route.stopLat}, stopLat: ${route.stopLng}")

        val startMarkerOptions = MarkerOptions()
            .position(startLatLng)
            .title("${route.county} - ${route.area} (Start)")
            .snippet("Length: ${route.length}")
        googleMap.addMarker(startMarkerOptions)


        val stopMarkerOptions = MarkerOptions()
            .position(stopLatLng)
            .title("${route.county} - ${route.area} (Stop)")
        googleMap.addMarker(stopMarkerOptions)
    }
}
