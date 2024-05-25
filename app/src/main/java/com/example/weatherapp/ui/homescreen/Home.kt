package com.example.weatherapp.ui.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import androidx.compose.ui.res.painterResource
import com.example.weatherapp.ui.theme.NavyBlue
import com.example.weatherapp.ui.theme.LightBlue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import com.example.weatherapp.ui.theme.DeepBlue
import com.example.weatherapp.ui.theme.WeatherAppTheme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.ui.theme.Blue60
import com.example.weatherapp.ui.theme.Blue80
import com.example.weatherapp.ui.theme.Sky80
import com.example.weatherapp.ui.theme.Sky60
import com.example.weatherapp.ui.theme.DeepSky
import com.example.weatherapp.ui.theme.LightSky
import com.example.weatherapp.ui.theme.NavySky
import com.example.weatherapp.viewmodal.WeatherViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class Forecast(
    val day: String,
    val highTemp: String,
    val lowTemp: String,
    val icon: ImageVector
)

data class HourlyInfo(
    val temperature: String,
    val icon: ImageVector,
    val hour: String
)

@Composable
fun Home(weatherViewModel: WeatherViewModel) {
    var isRainy by remember { mutableStateOf(true) }

    val weatherRespone by weatherViewModel.weather.collectAsState()

    var tempC = ""
    var desc = ""
    var maxtemp = ""
    var mintemp = ""
    var icon = ""
    var precip = ""
    var humi = ""
    var windSpeed = ""

    weatherRespone.let {
        tempC = it?.data?.current_condition?.get(0)?.temp_C ?: "Loading..."
        desc = it?.data?.current_condition?.get(0)?.weatherDesc?.get(0)?.value ?: "Loading..."
        maxtemp = it?.data?.weather?.get(0)?.maxtempC ?: "Loading..."
        mintemp = it?.data?.weather?.get(0)?.mintempC ?: "Loading..."
        icon = it?.data?.current_condition?.get(0)?.weatherIconUrl?.get(0)?.value ?: ""
        precip = it?.data?.current_condition?.get(0)?.precipMM ?: "Loading..."
        humi = it?.data?.current_condition?.get(0)?.humidity ?: "Loading..."
        windSpeed = it?.data?.current_condition?.get(0)?.windspeedKmph ?: "Loading..."
    }

    val gradientBrush = if (isRainy) {
        Brush.linearGradient(
            colors = listOf(NavyBlue, DeepBlue, LightBlue),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )
    } else {
        Brush.linearGradient(
            colors = listOf(NavySky, DeepSky, LightSky),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )
    }

    val mainColor = if (isRainy) {
        Blue80
    } else {
        Sky80
    }

    val secondaryColor = if (isRainy) {
        Blue60
    } else {
        Sky60
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        Header(onNotificationClick = { isRainy = !isRainy })
        Column(
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.Center)
                .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            MainInfo(
                tempC = tempC,
                desc = desc,
                maxtemp = maxtemp,
                mintemp = mintemp,
                iconWeather = icon,
            )
            Spacer(modifier = Modifier.height(30.dp))
            SpecificInfo(mainColor)
            Spacer(modifier = Modifier.height(30.dp))
            LazyColumn (
            ) {
                item {
                    val sampleHourlyInfo = listOf(
                        HourlyInfo("29°C", Icons.Default.WbSunny, "15:00"),
                        HourlyInfo("26°C", Icons.Default.WbSunny, "16:00"),
                        HourlyInfo("24°C", Icons.Default.Cloud, "17:00"),
                        HourlyInfo("23°C", Icons.Default.NightlightRound, "18:00"),
                        HourlyInfo("23°C", Icons.Default.NightlightRound, "19:00"),
                        HourlyInfo("29°C", Icons.Default.WbSunny, "20:00"),
                        HourlyInfo("26°C", Icons.Default.WbSunny, "21:00"),
                        HourlyInfo("24°C", Icons.Default.Cloud, "22:00"),
                        HourlyInfo("23°C", Icons.Default.NightlightRound, "23:00"),
                        HourlyInfo("23°C", Icons.Default.NightlightRound, "24:00"),

                    )

                    TodayInfo(hourlyInfoList = sampleHourlyInfo, mainColor, secondaryColor)
                    Spacer(modifier = Modifier.height(30.dp))
                    val sampleForecasts = listOf(
                        Forecast("Monday", "13°C", "10°C", Icons.Default.WbCloudy),
                        Forecast("Tuesday", "15°C", "12°C", Icons.Default.WbSunny),
                        Forecast("Wednesday", "17°C", "14°C", Icons.Default.WbCloudy),
                        Forecast("Thursday", "18°C", "15°C", Icons.Default.WbSunny),
                        Forecast("Friday", "20°C", "16°C", Icons.Default.WbCloudy),
                        Forecast("Saturday", "22°C", "18°C", Icons.Default.WbSunny),
                        Forecast("Sunday", "21°C", "17°C", Icons.Default.WbCloudy)
                    )
                    NextForecast(forecastList = sampleForecasts, mainColor)
                }
            }
        }
    }
}

@Composable
fun Header(onNotificationClick: () -> Unit) {
    val locationPainter: Painter = painterResource(id = R.drawable.location)
    Row(
        modifier = Modifier
            .padding(top = 44.dp, start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = locationPainter,
                contentDescription = "Description of the image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(24.dp, 24.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = "TP. HCM",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Cloud Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Cloud Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
                    .clickable {
                        // Khi nhấn vào nút thông báo, gọi hàm onNotificationClick để cập nhật giá trị isRainy
                        onNotificationClick()
                    }
            )
        }
    }
}

@Composable
fun MainInfo (tempC: String, desc: String, maxtemp: String, mintemp: String,iconWeather: String) {
    val regularFont = FontFamily(
        Font(R.font.sfprodisplay_regular),
    )
    Image(
        painter = rememberAsyncImagePainter(iconWeather),
        contentDescription = "Description of the image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(250.dp, 180.dp)
    )

    Column(
        modifier = Modifier.size(433.dp, 123.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "${tempC}°",
            fontSize = 64.sp,
            color = Color.White,
            fontFamily = regularFont,
            modifier = Modifier.padding(start = 20.dp),
            fontWeight = FontWeight.Bold,
        )
        Column(
            modifier = Modifier.size(433.dp, 42.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = desc,
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = regularFont,
            )

            Row(
                modifier = Modifier.size(433.dp, 21.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Max.:${maxtemp}°",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = regularFont,
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Min.:${mintemp}°",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = regularFont,
                )
            }
        }
    }
}

@Composable
fun SpecificInfo (mainColor: Color) {
    val nounRainPainter: Painter = painterResource(id = R.drawable.nounrain)
    val nounHumidityPainter: Painter = painterResource(id = R.drawable.nounhumidity)
    val nounWindPainter: Painter = painterResource(id = R.drawable.nounwind)
    Row(
        modifier = Modifier
            .size(width = 343.dp, height = 47.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(mainColor),
            verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .padding(top = 13.dp, bottom = 13.dp, start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = nounRainPainter,
                contentDescription = "Description of the image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(24.dp, 24.dp)
            )

            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = "6%",
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .padding(top = 13.dp, bottom = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = nounHumidityPainter,
                contentDescription = "Description of the image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(24.dp, 24.dp)
            )

            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = "90%",
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .padding(top = 13.dp, bottom = 13.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = nounWindPainter,
                contentDescription = "Description of the image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(24.dp, 24.dp)
            )

            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = "19 km/h",
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun TodayInfo(hourlyInfoList: List<HourlyInfo>, mainColor: Color, secondaryColor: Color) {
    val currentHour = SimpleDateFormat("HH", Locale.getDefault()).format(Date()).toInt()

    val lazyListState = rememberLazyListState()
    val currentIndex = hourlyInfoList.indexOfFirst {
        it.hour.split(":")[0].toInt() == currentHour
    }

    LaunchedEffect(currentIndex) {
        if (currentIndex != -1) {
            launch {
                lazyListState.scrollToItem(currentIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .size(width = 343.dp, height = 217.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(mainColor), // Blue80
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(start = 17.dp, end = 17.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = SimpleDateFormat("MMM, d", Locale.getDefault()).format(Date()),
                fontSize = 18.sp,
                color = Color.White,
            )
        }

        LazyRow(
            state = lazyListState,
            modifier = Modifier.padding(start = 17.dp, end = 17.dp),
        ) {
            items(hourlyInfoList.size) { index ->
                val info = hourlyInfoList[index]
                val hourInt = info.hour.split(":")[0].toInt()
                val isCurrentHour = hourInt == currentHour

                Box(
                    modifier = Modifier
                        .size(width = 70.dp, height = 155.dp)
                        .padding(top = 20.dp)
                        .background(if (isCurrentHour) secondaryColor else mainColor)

                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = info.temperature,
                            fontSize = 18.sp,
                            color = Color.White,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            imageVector = info.icon,
                            contentDescription = "Weather icon",
                            tint = if (info.icon == Icons.Default.WbSunny) Color.Yellow else Color.White,
                            modifier = Modifier.size(43.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = info.hour,
                            fontSize = 18.sp,
                            color = Color.White,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun NextForecast (forecastList: List<Forecast>, mainColor: Color) {
    Column (
        modifier = Modifier
            .size(width = 343.dp, height = 330.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(mainColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(start = 17.dp, end = 17.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weekly Forecast",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Description of the icon",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        forecastList.forEach { forecast ->
            NextForecastItem(forecast)
        }
    }
}

@Composable
fun NextForecastItem (forecast: Forecast) {
    Row(
        modifier = Modifier.padding(start = 17.dp, end = 17.dp, top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(3f)) {
            Text(
                text = forecast.day,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Icon(
            imageVector = forecast.icon,
            contentDescription = "Description of the icon",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = forecast.highTemp,
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = forecast.lowTemp,
            fontSize = 18.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
        )
    }
}



