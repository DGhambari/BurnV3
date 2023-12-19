package ie.setu.burnv3.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import ie.setu.burnv3.models.getUserId

@Composable
fun MapScreen(isMapActive: MutableState<Boolean>) {
    GoogleMapComposable(modifier = Modifier.fillMaxSize(), getUserId(), isMapActive)
}