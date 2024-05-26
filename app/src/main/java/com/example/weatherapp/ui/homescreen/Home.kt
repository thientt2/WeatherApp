package com.example.weatherapp.ui.homescreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.weatherapp.ui.theme.DeepBlue
import com.example.weatherapp.ui.theme.WeatherAppTheme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.DecodeUtils.calculateInSampleSize
import com.example.weatherapp.modal.weather.Hourly
import com.example.weatherapp.ui.theme.Blue60
import com.example.weatherapp.ui.theme.Blue80
import com.example.weatherapp.ui.theme.Sky80
import com.example.weatherapp.ui.theme.Sky60
import com.example.weatherapp.ui.theme.DeepSky
import com.example.weatherapp.ui.theme.Grey60
import com.example.weatherapp.ui.theme.LightSky
import com.example.weatherapp.ui.theme.NavySky
import com.example.weatherapp.viewmodal.WeatherViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    var hourlyInfoList = mutableMapOf<String, String>()
    val filteredHourlyData = mutableListOf<Hourly>()
    var weatherDate = ""
    var weatherCode = ""

    weatherRespone.let {data ->
        tempC = data?.data?.current_condition?.get(0)?.temp_C ?: "Loading..."
        desc = data?.data?.current_condition?.get(0)?.weatherDesc?.get(0)?.value ?: "Loading..."
        maxtemp = data?.data?.weather?.get(0)?.maxtempC ?: "Loading..."
        mintemp = data?.data?.weather?.get(0)?.mintempC ?: "Loading..."
        icon = data?.data?.current_condition?.get(0)?.weatherIconUrl?.get(0)?.value ?: ""
        precip = data?.data?.current_condition?.get(0)?.precipMM ?: "Loading..."
        humi = data?.data?.current_condition?.get(0)?.humidity ?: "Loading..."
        windSpeed = data?.data?.current_condition?.get(0)?.windspeedKmph ?: "Loading..."
        weatherDate = data?.data?.weather?.get(0)?.date.toString()
        weatherCode = data?.data?.current_condition?.get(0)?.weatherCode ?: "0"

        val weatherDataList = data?.data?.weather
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        if (weatherDataList != null) {
            for(weatherData in weatherDataList){
                val date = weatherData.date

                for(hourly in weatherData.hourly){
                    val time = hourly.time.padStart(4, '0') // Đảm bảo thời gian có định dạng 4 chữ số
                    val dateTimeStr = "$date ${time.substring(0, 2)}:${time.substring(2, 4)}"
                    val dateTime = LocalDateTime.parse(dateTimeStr, formatter)

                    if (dateTime.isAfter(now.minusHours(1)) && dateTime.isBefore(now.plusHours(24))) {
                        filteredHourlyData.add(hourly)
                    }
                }

            }
        }

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
        LazyColumn(
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.Center)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                MainInfo(
                    tempC = tempC,
                    desc = desc,
                    maxtemp = maxtemp,
                    mintemp = mintemp,
                    weatherCode = weatherCode,
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                SpecificInfo(mainColor)
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                TodayInfo(filteredHourlyData,weatherDate, mainColor, secondaryColor)
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
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
                Spacer(modifier = Modifier.height(30.dp))
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
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // Khi nhấn vào nút thông báo, gọi hàm onNotificationClick để cập nhật giá trị isRainy
                        onNotificationClick()
                    }
            )
        }
    }
}

@Composable
fun MainInfo (tempC: String, desc: String, maxtemp: String, mintemp: String,weatherCode: String) {
    val regularFont = FontFamily(
        Font(R.font.sfprodisplay_regular),
    )

    val context = LocalContext.current
    val isDaytime = isDaytime()
    val imageResource = getDayNightImageResource(isDaytime, weatherCode)
    val bitmap = getBitmapFromResource(context, imageResource)
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.size(250.dp,150.dp),
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

fun getBitmapFromResource(context: Context, resId: Int): Bitmap {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeResource(context.resources, resId, options)

    options.inSampleSize = calculateInSampleSize(options, 250, 180)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(context.resources, resId, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}


fun isDaytime(): Boolean {
    val currentHour = LocalDateTime.now().hour
    return currentHour in 6..18
}

fun getDayNightImageResource(isDaytime: Boolean, weatherCode: String): Int {
    return if (isDaytime) {
        when (weatherCode) {
            "113" -> R.drawable.daysun
            "116", "119" -> R.drawable.dayclouds
            "176", "263" ,"266" ,"281" ,"284" ,"293" ,"296" ,"299" ,"302" ,"305" ,"308" ,"317" ,"311" ,"320" ,"350" ,"353" ,"356" ,"359" ,"362" ,"365" ,"374" ,"377" ,"386" ,"389" -> R.drawable.dayrain
            "179" ,"182" ,"185" ,"227" ,"230" ,"314" ,"323" ,"326" ,"329" ,"332" ,"338" ,"368" ,"371" ,"392" ,"395" -> R.drawable.daysnow
            "200" -> R.drawable.daystorm

            else -> 0
        }
    } else {
        when (weatherCode) {
            "113" -> R.drawable.nightmoon
            "116", "119" -> R.drawable.nightclouds
            "176", "263" ,"266" ,"281" ,"284" ,"293" ,"296" ,"299" ,"302" ,"305" ,"308" ,"317" ,"311" ,"320" ,"350" ,"353" ,"356" ,"359" ,"362" ,"365" ,"374" ,"377" ,"386" ,"389" -> R.drawable.nightrain
            "179" ,"182" ,"185" ,"227" ,"230" ,"314" ,"323" ,"326" ,"329" ,"332" ,"338" ,"368" ,"371" ,"392" ,"395" -> R.drawable.nightsnow
            "200" -> R.drawable.nightsnow
            else -> 0
        }
    }
}




@Composable
fun SpecificInfo (mainColor: Color) {
    val nounRainPainter: Painter = painterResource(id = R.drawable.rain)
    val nounHumidityPainter: Painter = painterResource(id = R.drawable.nounhumidity)
    val nounWindPainter: Painter = painterResource(id = R.drawable.nounwind)
    Box(
        modifier = Modifier.padding(start = 32.dp, end = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(47.dp)
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
}

@Composable
fun TodayInfo(filteredHourlyData: MutableList<Hourly>,weatherDate: String,mainColor: Color, secondaryColor: Color) {
    val currentHour = SimpleDateFormat("HH", Locale.getDefault()).format(Date()).toInt()

    val lazyListState = rememberLazyListState()
//    val currentIndex = filteredHourlyData.indexOfFirst {
//        it.hour.split(":")[0].toInt() == currentHour
//    }

//    LaunchedEffect(currentIndex) {
//        if (currentIndex != -1) {
//            launch {
//                lazyListState.scrollToItem(currentIndex)
//            }
//        }
//    }

    Box(
        modifier = Modifier.padding(start = 32.dp, end = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .height(217.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(mainColor), // Blue80
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp),
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
                    text = weatherDate,
                    fontSize = 18.sp,
                    color = Color.White,
                )
            }

            LazyRow(
                state = lazyListState,
                modifier = Modifier.padding(start = 14.dp, end = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(filteredHourlyData.size) { index ->
                    val info = filteredHourlyData[index]
                    val hourInt = info.time.split(":")[0].toInt()
                    val isCurrentHour = hourInt == currentHour

                    Box(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .background(if (isCurrentHour) secondaryColor else mainColor)

                    ) {
                        Column(
                            modifier = Modifier
                                .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = info.tempC + "°C",
                                fontSize = 18.sp,
                                color = Color.White,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                painter = rememberAsyncImagePainter(info.weatherIconUrl.get(0).value),
                                contentDescription = "Weather icon",
                                modifier = Modifier.size(43.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = if (isCurrentHour) "Now" else convertToTimeFormat(info.time),
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

}

fun convertToTimeFormat(time: String): String {
    val number = time.toInt()
    val hour = number / 100
    val minute = number % 100
    return "$hour:${String.format("%02d", minute)}"
}

@Composable
fun NextForecast (forecastList: List<Forecast>, mainColor: Color) {
    Box(
        modifier = Modifier.padding(start = 32.dp, end = 32.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
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



