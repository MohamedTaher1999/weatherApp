package openweather.data.mapper

import openweather.data.remote.response.CityItem
import openweather.data.remote.response.CityLatLongResponse
import openweather.data.remote.response.OneCallResponse
import openweather.domain.mapper.ResponseMapper
import openweather.domain.models.*

class CityInfoMapper : ResponseMapper<List<CityItem>, CityInfo> {

    override fun mapToModel(entity: List<CityItem>): CityInfo {
        return CityInfo(
            entity[0].lat,
            entity[0].lon,
            entity[0].name,
        )
    }

}