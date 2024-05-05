package openweather.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.collections.List

@Serializable
data class CityLatLongResponse (

    val items: List<CityItem>

    )
@Serializable
data class CityItem(
    val name: String,
    val local_names: Map<String, String> = emptyMap(),
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String
)