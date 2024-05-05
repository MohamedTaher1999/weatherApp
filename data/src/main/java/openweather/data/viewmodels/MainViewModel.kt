package openweather.data.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import openweather.data.remote.response.CityItem
import openweather.domain.models.CityInfo
import openweather.domain.models.NetworkResult
import openweather.domain.models.OneCall
import openweather.domain.repository.OpenWeatherRepository

class MainViewModel(
    private val repository: OpenWeatherRepository
) : ViewModel() {

    private val _oneCallWeather = MutableStateFlow<NetworkResult<OneCall>?>(NetworkResult.Empty)
    private val _oneCallCityInfo = MutableStateFlow<NetworkResult<CityInfo>?>(NetworkResult.Empty)

    val oneCallWeather: StateFlow<NetworkResult<OneCall>?> = _oneCallWeather
    val searchTextState: MutableState<String> = mutableStateOf(value = "")
    var currentCity: CityInfo? = null

    private val _showSplash= MutableStateFlow(true)
    val showSplash = _showSplash.asStateFlow()

    fun fetchOneCall(lat: Double, lon: Double){

        if (_oneCallWeather.value is NetworkResult.Success)
            return
        viewModelScope.launch {
            var lat = lat
            var lon = lon
            if(lat == -1.0){
                var cityInfo = repository.getLocalCity()
                lat = cityInfo.lat
                lon = cityInfo.lon
                currentCity = cityInfo
            }
            repository.fetchOneCall(lat, lon).collect { result ->
                _oneCallWeather.value = result
                when (result) {
                    is NetworkResult.Success, is NetworkResult.Error, is NetworkResult.Failure -> {
                        _showSplash.value = false
                    } else -> {}
                }
            }
        }
    }
    fun updateSearchTextState(newValue: String) {
        searchTextState.value = newValue
    }

    fun getWeather(city: String = "") {

        viewModelScope.launch {
            repository.fetchCityLatLon(city).collect { result ->
                _oneCallCityInfo.value = result
                when (result) {
                    is NetworkResult.Success ->{
                        currentCity = result.data
                        repository.saveLocalCity(result.data)
                        _oneCallWeather.value = NetworkResult.Loading
                        result.data.lat?.let { result.data.lon?.let { it1 -> fetchOneCall(it, it1) } }
                    } else -> {}
                }
            }
        }

    }
    fun dismissSplash() { _showSplash.value = false }
}