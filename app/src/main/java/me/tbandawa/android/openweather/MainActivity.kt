package me.tbandawa.android.openweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.example.features.gps.service.LocationInfo
import com.example.features.gps.service.LocationService
import com.example.features.gps.ui.components.EnableGpsContent
import com.example.features.gps.ui.presenter.LoadingContent
import com.example.features.settings.ui.presenter.SettingsContent
import com.example.features.current_weather.ui.presenter.WeatherContent
import com.example.features.forecast.ui.presenter.ForecastContent
import me.tbandawa.android.openweather.ui.theme.OpenWeatherTheme
import openweather.data.viewmodels.MainViewModel
import openweather.domain.datastore.UnitsPreferencesDataStore
import openweather.domain.models.PreferenceUnits
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val unitsPreferencesDataStore: UnitsPreferencesDataStore by inject()
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create location service, observe gps coordinates and
        // navigate call api if location info is available
        LocationService(this@MainActivity).locationInfo.value.also {
            if (it != null) {
                viewModel.fetchOneCall(
                    lat = it.latitude,
                    lon = it.longitude
                )
            } else {
                viewModel.dismissSplash()
            }
        }

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.showSplash.value
        }

        setContent {

            val preferenceUnits = unitsPreferencesDataStore.preferencesUnits.collectAsState(
                PreferenceUnits("°C", "m/s", "hPa", "12-hour")
            ).value

            val navController = rememberNavController()

            // Back navigation callback
            val navigateUp: () -> Unit = { navController.navigateUp() }

            // Weather navigation callback
            val navigateToWeather: (LocationInfo?) -> Unit = { locationInfo ->
                if(locationInfo != null) {
                    navController.navigate("weather/${locationInfo.latitude}/${locationInfo.longitude}/${locationInfo.location}") {
                        launchSingleTop = true
                        popUpTo("loading") { inclusive = true }
                    }
                }
                else {

                    navController.navigate("weather/${-1}/${-1}/${-1}") {
                        launchSingleTop = true
                        popUpTo("loading") { inclusive = true }
                    }
                }
            }

            // Forecast navigation callback
            val navigateToForecast: () -> Unit = {
                navController.navigate("forecast/${viewModel.currentCity?.cityName}")
            }

            // Settings navigation callback
            val navigateToSettings: () -> Unit = {
                navController.navigate("settings")
            }

            OpenWeatherTheme {
                NavHost(navController, startDestination = "loading") {

                    composable(route = "loading") {
                        LoadingContent(
                            navigateToWeather
                        )
                    }

                    composable(route = "weather/{latitude}/{longitude}/{location}") { backStackEntry ->
                        val latitude = backStackEntry.arguments?.getString("latitude")?.toDouble()
                        val longitude = backStackEntry.arguments?.getString("longitude")?.toDouble()
                        val location = backStackEntry.arguments?.getString("location")
                        WeatherContent(
                            preferenceUnits = preferenceUnits,
                            viewModel = viewModel,
                            latitude = latitude!!,
                            longitude = longitude!!,
                            location = location!!,
                            navigateToSettings =  { navigateToSettings() },
                            navigateToForecast = { navigateToForecast() }
                        )
                    }

                    composable(route = "forecast/{location}") { backStackEntry ->
                        val location = backStackEntry.arguments?.getString("location")
                        ForecastContent(
                            preferenceUnits = preferenceUnits,
                            viewModel = viewModel,
                            location = location!!,
                            navigateUp = navigateUp
                        )
                    }

                    composable(route = "settings") {
                        SettingsContent(
                            unitsPreferencesDataStore,
                            navigateUp
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    OpenWeatherTheme {
        EnableGpsContent()
    }
}