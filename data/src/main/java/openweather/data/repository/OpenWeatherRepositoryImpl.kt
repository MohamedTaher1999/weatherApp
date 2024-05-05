package openweather.data.repository

import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import openweather.data.mapper.CityInfoMapper
import openweather.data.mapper.CurrentWeatherMapper
import openweather.data.mapper.ErrorMapper
import openweather.data.mapper.FiveDayWeatherMapper
import openweather.data.mapper.OneCallMapper
import openweather.data.remote.api.OpenWeatherApi
import openweather.data.remote.response.BaseResponse
import openweather.domain.datastore.CityPreferencesDataStore
import openweather.domain.models.CityInfo
import openweather.domain.models.CurrentWeather
import openweather.domain.models.FiveDayWeatherForecast
import openweather.domain.models.NetworkResult
import openweather.domain.models.OneCall
import openweather.domain.models.PreferenceUnits
import openweather.domain.repository.OpenWeatherRepository

class OpenWeatherRepositoryImpl (
    private val openWeatherApi: OpenWeatherApi,
    private val oneCallMapper: OneCallMapper,
    private val cityInfoMapper: CityInfoMapper,
    private val cityPreferencesDataStore: CityPreferencesDataStore,
    private val errorMapper: ErrorMapper
) : OpenWeatherRepository, BaseResponse(errorMapper) {

    override suspend fun fetchOneCall(lat: Double, lon: Double): Flow<NetworkResult<OneCall>> = flow {
        emit(NetworkResult.Loading)
        emit(safeApiCall {
            oneCallMapper.mapToModel(openWeatherApi.fetchOneCall(lat, lon))
        })
    }.flowOn(Dispatchers.IO)

    override suspend fun fetchCityLatLon(city: String): Flow<NetworkResult<CityInfo>> = flow {
        emit(NetworkResult.Loading)
        emit(safeApiCall {
            cityInfoMapper.mapToModel(openWeatherApi.getCityLatLon(city))
        })
    }

    override suspend fun getLocalCity(): CityInfo {
        return cityPreferencesDataStore.getPreferencesCity()
    }
    override suspend fun saveLocalCity(city: CityInfo) {
       cityPreferencesDataStore.savePreferencesCity(city)
    }
}