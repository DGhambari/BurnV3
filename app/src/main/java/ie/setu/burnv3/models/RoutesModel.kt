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
    //val imageURL: String
)

//val sampleRoutes = listOf(
//    Route("1", "Heading", "Sub Heading", "Description", 3.5),
//    Route("2", "Heading", "Sub Heading", "Description", 4.0),
//    Route("3", "Heading", "Sub Heading", "Description", 4.5),
//    Route("4", "Heading", "Sub Heading", "Description", 5.0)
//)

//val sampleRoutes = listOf<Route>()