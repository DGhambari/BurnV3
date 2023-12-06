package ie.setu.burnv3.models

data class Route(
    var id: String? = null,
    var county: String = "",
    var area: String = "",
    var description: String = "",
    var length: Double = 0.0,
    var startLat: Double = 0.0,
    var startLng: Double = 0.0,
    var stopLat: Double = 0.0,
    var stopLng: Double = 0.0,
    val imageUrl: String? = "",
    var isFavorite: Boolean = false
)
