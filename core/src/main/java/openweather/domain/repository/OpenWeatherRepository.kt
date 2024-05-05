package openweather.domain.repository

import kotlinx.coroutines.flow.Flow
import openweather.domain.models.*

interface OpenWeatherRepository {
    suspend fun fetchOneCall(lat: Double, lon: Double): Flow<NetworkResult<OneCall>>

    suspend fun fetchCityLatLon(city: String): Flow<NetworkResult<CityInfo>>

    suspend fun getLocalCity(): CityInfo

    suspend fun saveLocalCity(city: CityInfo)
}