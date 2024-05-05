package com.example.features.settings.ui.presenter

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.features.R
import com.example.features.common.util.Utlis.Companion.WEATHER_ICONS_SOURCE_URL
import com.example.features.forecast.ui.components.HorizontalDivider
import com.example.features.settings.ui.components.SettingsToolBar
import com.example.features.settings.ui.components.UnitChip
import kotlinx.coroutines.launch
import me.tbandawa.android.openweather.ui.theme.dimensions
import me.tbandawa.android.openweather.ui.theme.orientation
import openweather.data.local.UnitsPreferencesDataStoreImpl
import openweather.domain.datastore.UnitsPreferencesDataStore
import openweather.domain.models.PreferenceUnits

@Composable
fun SettingsContent(
    unitsPreferencesDataStore: UnitsPreferencesDataStore,
    navigateUp: () -> Unit
) {

    val preferenceUnits = unitsPreferencesDataStore.preferencesUnits.collectAsState(
        PreferenceUnits("°C", "m/s", "hPa", "12-hour")
    ).value

    val coroutineScope = rememberCoroutineScope()

    val updateUnitsPreference: (PreferenceUnits) -> Unit = { preferenceUnits ->
        coroutineScope.launch {
            unitsPreferencesDataStore.savePreferencesUnits(preferenceUnits)
        }
    }

    Surface {
        Scaffold(
            topBar = { SettingsToolBar(navigateUp) },
            containerColor = Color.White
        ) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            ) {

                val (contentLayout, bottomLayout) = createRefs()

                Column(
                    modifier = Modifier
                        .constrainAs(contentLayout) {
                            top.linkTo(parent.top)
                        }
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {

                    // Units text
                    Text(
                        text = "Units",
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = dimensions.settingsHeaderTextSize
                        ),
                        modifier = Modifier
                            .padding(0.dp, 16.dp, 0.dp, 8.dp)
                    )

                    // Horizontal divider
                    HorizontalDivider()

                    // Temperature units settings
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = when (orientation) {
                                    1 -> {
                                        20.dp
                                    }
                                    2 -> {
                                        5.dp
                                    }
                                    else -> {
                                        20.dp
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Temperature",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = dimensions.settingsTitleTextSize
                            )
                        )
                        Surface(
                            color = Color.LightGray,
                            contentColor = Color.LightGray,
                            shape = CircleShape,
                            modifier = Modifier
                                .height(dimensions.settingsUnitContainerSize)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 3.dp)
                            ) {
                                UnitChip(preferenceUnits, updateUnitsPreference, "°C")
                                UnitChip(preferenceUnits, updateUnitsPreference, "°F")
                            }
                        }
                    }

                    // Wind speed units settings
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = when (orientation) {
                                    1 -> {
                                        20.dp
                                    }
                                    2 -> {
                                        5.dp
                                    }
                                    else -> {
                                        20.dp
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Wind speed",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = dimensions.settingsTitleTextSize
                            )
                        )
                        Surface(
                            color = Color.LightGray,
                            contentColor = Color.LightGray,
                            shape = CircleShape,
                            modifier = Modifier
                                .height(dimensions.settingsUnitContainerSize)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 3.dp)
                            ) {
                                UnitChip(preferenceUnits, updateUnitsPreference, text = "m/s")
                                UnitChip(preferenceUnits, updateUnitsPreference, text = "km/h")
                                UnitChip(preferenceUnits, updateUnitsPreference, text = "mph")
                            }
                        }
                    }

                    // Pressure units settings
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = when (orientation) {
                                    1 -> {
                                        20.dp
                                    }
                                    2 -> {
                                        5.dp
                                    }
                                    else -> {
                                        20.dp
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pressure",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = dimensions.settingsTitleTextSize
                            )
                        )
                        Surface(
                            color = Color.LightGray,
                            contentColor = Color.LightGray,
                            shape = CircleShape,
                            modifier = Modifier
                                .height(dimensions.settingsUnitContainerSize)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 3.dp)
                            ) {
                                UnitChip(preferenceUnits, updateUnitsPreference, text = "hPa")
                                UnitChip(preferenceUnits, updateUnitsPreference, text = "inHg")
                            }
                        }
                    }

                    // Time format units settings
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = when (orientation) {
                                    1 -> {
                                        20.dp
                                    }
                                    2 -> {
                                        5.dp
                                    }
                                    else -> {
                                        20.dp
                                    }
                                },
                                bottom = when (orientation) {
                                    1 -> {
                                        45.dp
                                    }
                                    2 -> {
                                        5.dp
                                    }
                                    else -> {
                                        45.dp
                                    }
                                }

                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Time format",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = dimensions.settingsTitleTextSize
                            )
                        )
                        Surface(
                            color = Color.LightGray,
                            contentColor = Color.LightGray,
                            shape = CircleShape,
                            modifier = Modifier
                                .height(dimensions.settingsUnitContainerSize)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 3.dp)
                            ) {
                                UnitChip(preferenceUnits, updateUnitsPreference, text = "12-hour")
                                UnitChip(preferenceUnits, updateUnitsPreference, text = "24-hour")
                            }
                        }
                    }

                    // Horizontal divider
                    HorizontalDivider()

                    }

                }

            }
        }
    }


@Preview(showBackground = true)
@Composable
fun SettingsContentPreview() {
    SettingsContent(
        unitsPreferencesDataStore = UnitsPreferencesDataStoreImpl(LocalContext.current),
        navigateUp = { }
    )
}