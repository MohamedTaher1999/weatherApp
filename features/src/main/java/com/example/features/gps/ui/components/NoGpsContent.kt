package com.example.features.gps.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@Composable
fun NoGpsContent() {

    Surface(color = Color.White) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, bottom = 50.dp, end = 20.dp)
        ) {

            val context = LocalContext.current

            val (titleLayout, descriptionLayout, cancelButton) = createRefs()

            // No gps text
            Text(
                text = "Location feature not found",
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

            // Rationale description text
            Text(
                text = "openWeather needs location services to know where you are.",
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .constrainAs(descriptionLayout) {
                        start.linkTo(parent.start)
                        bottom.linkTo(cancelButton.top)
                    }
                    .width(300.dp)
                    .height(IntrinsicSize.Max)
                    .padding(bottom = 10.dp)
            )

            // Exit button
            Button(
                onClick = {
                    (context as ComponentActivity).finish()
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .constrainAs(cancelButton) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 5.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Exit")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
fun NoGpsContentPreview() {
    NoGpsContent()
}