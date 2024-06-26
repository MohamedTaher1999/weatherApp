package com.example.features.current_weather.ui.presenter

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.features.R
import com.example.features.common.util.Utlis.Companion.WEATHER_ICON_SIZE_4X
import com.example.features.common.util.Utlis.Companion.WEATHER_ICON_URL
import com.example.features.current_weather.ui.components.BottomRecycler
import com.example.features.current_weather.ui.components.DetailGrid
import com.example.features.current_weather.ui.components.SearchAppBar
import com.example.features.current_weather.ui.components.WeatherToolBar
import me.tbandawa.android.openweather.extensions.toTemperature
import me.tbandawa.android.openweather.ui.theme.dimensions
import me.tbandawa.android.openweather.ui.theme.orientation
import openweather.data.viewmodels.MainViewModel
import openweather.domain.models.Error
import openweather.domain.models.NetworkResult
import openweather.domain.models.OneCall
import openweather.domain.models.PreferenceUnits
import java.util.Locale

@ExperimentalAnimationApi
@Composable
fun WeatherContent(
    preferenceUnits: PreferenceUnits,
    viewModel: MainViewModel,
    latitude: Double,
    longitude: Double,
    location: String,
    navigateToSettings: () -> Unit,
    navigateToForecast: () -> Unit
) {

    Surface {

        // Retry callback
        val retry: () -> Unit = { viewModel.fetchOneCall(latitude, longitude) }

        LaunchedEffect(Unit) {
            viewModel.fetchOneCall(latitude, longitude)
        }

        // Update UI according to network result state
        when(val result = viewModel.oneCallWeather.collectAsState().value) {
            is NetworkResult.Loading -> {
                LoadingScreen()
            }
            is NetworkResult.Success -> {
                WeatherScreen(
                    preferenceUnits = preferenceUnits,
                    oneCall = result.data,
                    viewModel,
                    location,
                    navigateToSettings,
                    navigateToForecast
                )
            }
            is NetworkResult.Error -> {
                ErrorScreen(result.data!!, retry)
            }
            is NetworkResult.Failure -> {
                FailureScreen(result.message, retry)
            }
            is NetworkResult.Empty -> {
                LoadingScreen()
            }
            else -> {}
        }

    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun WeatherScreen(
    preferenceUnits: PreferenceUnits,
    oneCall: OneCall,
    viewModel: MainViewModel,
    location: String,
    navigateToSettings: () -> Unit,
    navigateToForecast: () -> Unit
) {

    val weatherIcon = "${WEATHER_ICON_URL}${oneCall.current?.weather?.get(0)?.icon}${WEATHER_ICON_SIZE_4X}"

    Scaffold(
        topBar = { SearchAppBar(
            text = viewModel.searchTextState.value,
            onTextChange = { viewModel.updateSearchTextState(it) },
            onSearchClicked = { viewModel.getWeather(it) },
            navigateToSettings
        ) },
        containerColor = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight(1f)
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                when (orientation) {
                    1 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = dimensions.weatherIconPadding),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // Main weather image
                            AsyncImage(
                                model = weatherIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(dimensions.weatherIconSize, dimensions.weatherIconSize)
                            )

                            // Temperature text
                            Text(
                                text = oneCall.current?.temp!!.toTemperature(preferenceUnits.temperature),
                                style = TextStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = dimensions.weatherTemperatureTextSize
                                )
                            )

                            // Weather description text
                            Text(
                                text = oneCall.current!!.weather?.get(0)!!.description!!.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                },
                                style = TextStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = dimensions.weatherDescriptionTextSize,
                                    textAlign = TextAlign.Center
                                )
                            )

                            DetailGrid(oneCall.current!!, preferenceUnits)

                        }
                    }
                    2 -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(IntrinsicSize.Min)
                                    .padding(start = 25.dp, end = 35.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                // Main weather image
                                AsyncImage(
                                    model = weatherIcon,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(dimensions.weatherIconSize, dimensions.weatherIconSize)
                                )

                                // Temperature text
                                Text(
                                    text = oneCall.current?.temp!!.toTemperature(preferenceUnits.temperature),
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 25.sp,
                                        textAlign = TextAlign.Center
                                    )
                                )

                                // Weather description text
                                Text(
                                    text = oneCall.current!!.weather?.get(0)!!.description!!.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.getDefault()
                                        ) else it.toString()
                                    },
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                )

                            }
                            DetailGrid(oneCall.current!!, preferenceUnits)
                        }
                    }
                }

            }
            Spacer(Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BottomRecycler(
                    oneCall.hourly!!,
                    preferenceUnits,
                    navigateToForecast
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun LoadingScreen() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        val (centerLayout, textTitle) = createRefs()

        // Open weather image
        Image(
            painter = painterResource(R.drawable.weather),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(centerLayout) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .size(225.dp, 225.dp)
                .padding(0.dp, 0.dp, 8.dp, 0.dp)
        )

        // Open weather text
        Text(
            text = "open Weather",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .constrainAs(textTitle) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
        )

    }

}

@Composable
fun ErrorScreen(
    error: Error,
    retry: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, bottom = 50.dp, end = 20.dp)
    ) {

        val (titleLayout, descriptionLayout, retryButton) = createRefs()

        // Ooops text
        Text(
            text = "Ooops!",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .constrainAs(titleLayout) {
                    bottom.linkTo(descriptionLayout.top)
                    start.linkTo(parent.start)
                }
                .height(IntrinsicSize.Min)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
        )

        // Error message text
        Text(
            text = error.message,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .constrainAs(descriptionLayout) {
                    start.linkTo(parent.start)
                    bottom.linkTo(retryButton.top)
                }
                .width(300.dp)
                .height(IntrinsicSize.Max)
                .padding(bottom = 10.dp)
        )

        // Retry button
        Button(
            onClick = {
                retry.invoke()
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .constrainAs(retryButton) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 5.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Retry")
        }

    }
}

@Composable
fun FailureScreen(
    message: String,
    retry: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, bottom = 50.dp, end = 20.dp)
    ) {

        val (titleLayout, descriptionLayout, retryButton) = createRefs()

        // Ooops text
        Text(
            text = "Ooops!",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .constrainAs(titleLayout) {
                    bottom.linkTo(descriptionLayout.top)
                    start.linkTo(parent.start)
                }
                .height(IntrinsicSize.Min)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
        )

        // Error message text
        Text(
            text = message,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .constrainAs(descriptionLayout) {
                    start.linkTo(parent.start)
                    bottom.linkTo(retryButton.top)
                }
                .width(300.dp)
                .height(IntrinsicSize.Max)
                .padding(bottom = 10.dp)
        )

        // Retry button
        Button(
            onClick = {
                retry.invoke()
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .constrainAs(retryButton) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 5.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Retry")
        }

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true)
@Composable
fun WeatherContentPreview() {
    LoadingScreen()
}