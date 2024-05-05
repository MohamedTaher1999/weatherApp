package openweather.domain.datastore

import kotlinx.coroutines.flow.Flow
import openweather.domain.models.CityInfo
import openweather.domain.models.PreferenceUnits

interface CityPreferencesDataStore {

    suspend fun getPreferencesCity(): CityInfo

    suspend fun savePreferencesCity(cityInfo: CityInfo)

}