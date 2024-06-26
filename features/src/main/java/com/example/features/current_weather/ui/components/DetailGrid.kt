package com.example.features.current_weather.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.features.forecast.ui.components.HorizontalDivider
import me.tbandawa.android.openweather.extensions.*
import openweather.domain.models.Current
import openweather.domain.models.PreferenceUnits
import com.example.features.R

@Composable
fun DetailGrid(
    current: Current,
    preferenceUnits: PreferenceUnits
) {
    Column(
        modifier = Modifier
            .padding(0.dp, 15.dp, 0.dp, 0.dp)
            .width(IntrinsicSize.Max)
    ) {
        Row {
            DetailItem(painterResource(R.drawable.ic_feels), "Feels like", current.feelsLike!!.toTemperature(preferenceUnits.temperature))
            VerticalDivider()
            DetailItem(painterResource(R.drawable.ic_wind), "Wind Speed", current.windSpeed!!.toSpeed(preferenceUnits.speed))
            VerticalDivider()
            DetailItem(painterResource(R.drawable.ic_direction), "Wind direction", current.windDeg!!.toDirection())
        }
        HorizontalDivider()
        Row {
            DetailItem(painterResource(R.drawable.ic_uv), "UV index", current.uvi!!.toUV())
            VerticalDivider()
            DetailItem(painterResource(R.drawable.ic_cloud), "Cloud cover", current.clouds!!.toCloudCover())
            VerticalDivider()
            DetailItem(painterResource(R.drawable.ic_pressure), "Pressure", current.pressure!!.toPressure(preferenceUnits.pressure))
        }
        HorizontalDivider()
        Row {
            DetailItem(painterResource(R.drawable.ic_humidity), "Humidity", current.humidity!!.toHumidity())
            VerticalDivider()
            DetailItem(painterResource(R.drawable.ic_dew), "Dew point", current.dewPoint!!.toDewPoint(preferenceUnits.temperature))
            VerticalDivider()
            DetailItem(painterResource(R.drawable.ic_visibility), "Visibility", current.visibility!!.toVisibility())
        }
    }
}