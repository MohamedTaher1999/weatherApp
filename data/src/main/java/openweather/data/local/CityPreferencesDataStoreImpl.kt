package openweather.data.local

import android.content.Context

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import openweather.domain.datastore.CityPreferencesDataStore
import openweather.domain.models.CityInfo

class CityPreferencesDataStoreImpl(val context: Context) : CityPreferencesDataStore {

    private val sharedPreferences = context.getSharedPreferences("city_info", Context.MODE_PRIVATE)
    override suspend fun getPreferencesCity(): CityInfo {
       with(sharedPreferences){
           return CityInfo(
               lat = getFloat("lat", 0.0f).toDouble(),
               lon = getFloat("lon", 0.0f).toDouble(),
               cityName = getString("cityName", "") ?: ""
           )
       }
    }



    override suspend fun savePreferencesCity(cityInfo: CityInfo) {
        with(sharedPreferences.edit()) {
            putFloat("lat", cityInfo.lat?.toFloat() ?: 0.0f)
            putFloat("lon", cityInfo.lon?.toFloat() ?: 0.0f)
            putString("cityName", cityInfo.cityName)
            apply()
        }
    }
}