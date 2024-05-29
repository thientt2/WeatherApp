package com.example.weatherapp.ui.homescreen

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.DecodeUtils.calculateInSampleSize

import com.example.weatherapp.modal.weather.Hourly
import com.example.weatherapp.modal.weather.Weather
import com.example.weatherapp.modal.weather.WeatherData
import com.example.weatherapp.ui.theme.Blue60
import com.example.weatherapp.ui.theme.Blue80
import com.example.weatherapp.ui.theme.Sky80
import com.example.weatherapp.ui.theme.Sky60
import com.example.weatherapp.ui.theme.DeepSky
import com.example.weatherapp.ui.theme.Grey40
import com.example.weatherapp.ui.theme.Grey60
import com.example.weatherapp.ui.theme.Grey80
import com.example.weatherapp.ui.theme.LightSky
import com.example.weatherapp.ui.theme.NavySky
import com.example.weatherapp.viewmodal.LocationViewModel
import com.example.weatherapp.viewmodal.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale



@Composable
fun Home(weatherViewModel: WeatherViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), locationViewModel: LocationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    LoadModel(weatherViewModel,locationViewModel)

}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoadModel(weatherViewModel: WeatherViewModel, locationViewModel: LocationViewModel){
    val context = LocalContext.current
    val location by locationViewModel.location.collectAsState()
    val weather by weatherViewModel.weather.collectAsState()

    var permissionGranted by remember { mutableStateOf(false) }
    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            permissionGranted = true
            locationViewModel.fetchLocation(context)
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    if (permissionGranted) {
        Surface(color = MaterialTheme.colorScheme.background) {
            if (location != null) {
                LaunchedEffect(location) {
                    weatherViewModel.fetchWeather(location!!.latitude, location!!.longitude)

                }
                if (weather != null) {
                    WeatherScreen(weatherViewModel)
                } else {
                    LoadingSection()
                }
            } else {
                androidx.compose.material3.Text(text = "Fetching location data...")
            }
        }
    } else {
        Surface(color = MaterialTheme.colorScheme.background) {
            androidx.compose.material3.Text(text = "Permission required to access location.")
        }
    }
}

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel){
    var isRainy by remember { mutableStateOf(true) }
    val weatherRespone by weatherViewModel.weather.collectAsState()
    val weatherData = weatherRespone?.data?.weather

    var tempC = ""
    var desc = ""
    var maxtemp = ""
    var mintemp = ""
    var icon = ""
    var precip = ""
    var humi = ""
    var windSpeed = ""
    val hourlyInfoList = mutableListOf<Triple<String, String, String>>()
    val filteredHourlyData = mutableListOf<Hourly>()
    var weatherDate = ""
    var weatherCode = ""
    var uvIndex = ""
    var visibility = ""
    var cloudCover = ""
    var sunSet = ""
    var sunRise = ""

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
        uvIndex = data?.data?.current_condition?.get(0)?.uvIndex ?: "Loading..."
        visibility = data?.data?.current_condition?.get(0)?.visibilityMiles ?: "Loading..."
        cloudCover = data?.data?.current_condition?.get(0)?.cloudcover ?: "Loading..."

        sunRise = data?.data?.weather?.get(0)?.astronomy?.get(0)?.sunrise ?: "Loading..."
        sunSet = data?.data?.weather?.get(0)?.astronomy?.get(0)?.sunset ?: "Loading..."

        val weatherDataList = data?.data?.weather
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formatterHour = DateTimeFormatter.ofPattern("HH:mm")
        val timeNow = now.format(formatterHour)
        hourlyInfoList.add(Triple(timeNow,weatherCode,tempC))

        if (weatherDataList != null) {
            for (weatherData in weatherDataList) {
                val date = weatherData.date
                // Lọc thông tin thời tiết theo giờ cho 24 giờ tiếp theo
                for (hourly in weatherData.hourly) {
                    val time = hourly.time.padStart(4, '0') // Đảm bảo thời gian có định dạng 4 chữ số
                    val timeFormatted = "${time.substring(0, 2)}:${time.substring(2, 4)}"
                    val dateTimeStr = "$date ${time.substring(0, 2)}:${time.substring(2, 4)}"
                    val dateTime = LocalDateTime.parse(dateTimeStr, formatter)

                    if (dateTime.isAfter(now) && dateTime.isBefore(now.plusHours(23))) {
                        val weatherCode = hourly.weatherCode ?: "0"
                        val tempC = hourly.tempC ?: "Loading..."
                        filteredHourlyData.add(hourly)
                        hourlyInfoList.add(Triple(timeFormatted, weatherCode, tempC))
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
                .padding(top = 90.dp),
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
                SpecificInfo(
                    mainColor,
                    precipitation = precip,
                    humidity = humi,
                    windSpeed = windSpeed,
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                TodayInfo(hourlyInfoList,weatherDate, mainColor, secondaryColor)
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                if (weatherData != null) {
                    NextForecast( weatherData = weatherData, mainColor)
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                MoreInfo(uvIndex, visibility, cloudCover, mainColor)
                Spacer(modifier = Modifier.height(30.dp))
            }

//            val currentTime = LocalTime.now()
//            val sunriseTime = LocalTime.parse(sunRise, DateTimeFormatter.ofPattern("hh:mm a"))
//            val sunsetTime = LocalTime.parse(sunSet, DateTimeFormatter.ofPattern("hh:mm a"))
//
//            val isDaytime = currentTime.isAfter(sunriseTime) && currentTime.isBefore(sunsetTime)
//
//            if (isDaytime) {
//                item {
//                    SunriseSunsetSlider(mainColor, sunrise = sunRise, sunset = sunSet)
//                    Spacer(modifier = Modifier.height(40.dp))
//                }
//            }
        }
    }
}

@Composable
fun LoadingSection() {
    return Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.White)

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
                    text = "Max: ${maxtemp}°",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = regularFont,
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Min: ${mintemp}°",
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
fun SpecificInfo (mainColor: Color, precipitation: String, humidity: String, windSpeed: String) {
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
                .clip(RoundedCornerShape(15.dp))
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
                    text = "${precipitation}%",
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
                    text = "${humidity}%",
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
                    text = "$windSpeed km/h",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun TodayInfo(hourlyInfoList: MutableList<Triple<String, String, String>> ,weatherDate: String,mainColor: Color, secondaryColor: Color) {
    val currentHour = SimpleDateFormat("HH", Locale.getDefault()).format(Date()).toInt()

    val lazyListState = rememberLazyListState()

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
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(hourlyInfoList.size) { index ->
                    val info = hourlyInfoList[index]
                    val hourInt = getHourFromTimeString(info.first)
                    val isCurrentHour = hourInt == currentHour


                    val isDaytime = isDaytime(hourInt)
                    val weatherCodeIcon = info.second ?: ""
                    val iconResource = getDayNightIconResource(isDaytime, weatherCodeIcon)


                    Box(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = if (isCurrentHour) 1.dp else 0.dp, // Width of the border if current hour
                                color = if (isCurrentHour) Color.White else Color.Transparent, // Border color if current hour
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(if (isCurrentHour) secondaryColor else mainColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = info.third + "°C",
                                fontSize = 18.sp,
                                color = Color.White,
                            )

                            Spacer(modifier = Modifier.weight(1f))


                            Icon(
                                painter = painterResource(id = iconResource),
                                contentDescription = "Weather icon",
                                modifier = Modifier.size(43.dp),
                                tint = Color.White,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = if (index == 0) "Now" else convertToTimeFormat(info.first),
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
fun isDaytime(hour: Int): Boolean {
    return hour in 6..17
}
fun getHourFromTimeString(time: String): Int {
    // Giả sử đầu vào là "HH:mm", chúng ta lấy phần giờ "HH" và chuyển đổi nó thành số nguyên
    return time.substring(0, 2).toInt()
}

fun convertToTimeFormat(time: String): String {
    // Giả sử đầu vào là "HHmm", chúng ta chuyển đổi nó thành "HH:mm"
    return if (time.length == 4) {
        "${time.substring(0, 2)}:${time.substring(2, 4)}"
    } else {
        time // Trả về nguyên bản nếu không đúng định dạng
    }
}


fun getDayNightIconResource(isDaytime: Boolean, weatherCode: String): Int {
    return if (isDaytime) {
        when (weatherCode) {
            "227" -> R.drawable.blowingsnow
            "179" ,"323" ,"326" ,"329" ,"332" ,"335" ,"338" ,"350" ,"392" -> R.drawable.snow
            "182" ,"311" ,"314" ,"368" ,"371" ,"374" ,"377" -> R.drawable.sleet
            "230" ,"395" -> R.drawable.blizzard
            "386" ,"389" -> R.drawable.rainandthunderstorm
            "200" -> R.drawable.severthunderstorm
            "185" ,"263" ,"266" ,"281" ,"284" -> R.drawable.drizzle
            "176" ,"296" ,"299" ,"302" ,"317" ,"320" ,"356" ,"359" -> R.drawable.rain
            "293", "353","362" ,"365" -> R.drawable.scatteradshowers
            "305" ,"308" -> R.drawable.heavyrain
            "143", "248", "260" -> R.drawable.fog
            "116" -> R.drawable.partlycloudy
            "119" ,"122" -> R.drawable.cloudy
            "113" -> R.drawable.sunny


            else -> 0
        }
    } else {
        when (weatherCode) {
            "227" -> R.drawable.blowingsnow
            "179" ,"323" ,"326" ,"329" ,"332" ,"335" ,"338" ,"350" ,"392" -> R.drawable.snow
            "182" ,"311" ,"314" ,"368" ,"371" ,"374" ,"377" -> R.drawable.sleet
            "230" ,"395" -> R.drawable.blizzard
            "386" ,"389" -> R.drawable.rainandthunderstorm
            "200" -> R.drawable.severthunderstorm
            "185" ,"263" ,"266" ,"281" ,"284" -> R.drawable.drizzle
            "176" ,"296" ,"299" ,"302" ,"317" ,"320" ,"356" ,"359" -> R.drawable.rainnight
            "293", "353","362" ,"365" -> R.drawable.scatteradshowersnight
            "305" ,"308" -> R.drawable.heavyrain
            "143", "248", "260" -> R.drawable.fog
            "116" -> R.drawable.partlycloudynight
            "119" ,"122" -> R.drawable.partlyclearnight
            "113" -> R.drawable.clearnight
            else -> 0
        }
    }
}
@Composable
fun NextForecast (weatherData: List<Weather>, mainColor: Color) {

    Box(
        modifier = Modifier.padding(start = 32.dp, end = 32.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .clip(RoundedCornerShape(15.dp))
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
            weatherData.forEach { weather ->
                NextForecastItem(weather)
            }
        }
    }
}

@Composable
fun NextForecastItem (weather: Weather) {
    val weatherCode = weather?.hourly?.get(11)?.weatherCode ?: "0"
    val iconResource = getIconResource(weatherCode)
    val maxTempC = weather?.maxtempC ?: "0"
    val minTempC = weather?.mintempC ?: "0"

    Row(
        modifier = Modifier.padding(start = 17.dp, end = 17.dp, top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(3f)) {
            Text(
                text = weather.date,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = "Description of the icon",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "${maxTempC}°",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${minTempC}°",
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.5f),
            fontWeight = FontWeight.Bold,
        )
    }
}

fun getIconResource(weatherCode: String): Int {
    return when (weatherCode) {
        "227" -> R.drawable.blowingsnow
        "179", "323", "326", "329", "332", "335", "338", "350", "392" -> R.drawable.snow
        "182", "311", "314", "368", "371", "374", "377" -> R.drawable.sleet
        "230", "395" -> R.drawable.blizzard
        "386", "389" -> R.drawable.rainandthunderstorm
        "200" -> R.drawable.severthunderstorm
        "185", "263", "266", "281", "284" -> R.drawable.drizzle
        "176", "296", "299", "302", "317", "320", "356", "359" -> R.drawable.rain
        "293", "353", "362", "365" -> R.drawable.scatteradshowers
        "305", "308" -> R.drawable.heavyrain
        "143", "248", "260" -> R.drawable.fog
        "116" -> R.drawable.partlycloudy
        "119", "122" -> R.drawable.cloudy
        "113" -> R.drawable.sunny
        else -> 0
    }
}



@Composable
fun InfoBox(icon: ImageVector, description: String, value: String, unit: String, mainColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(color = mainColor)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 15.dp, bottom = 15.dp, start = 15.dp, end = 20.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "$description Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = unit,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun MoreInfo(uvIndex: String, visibility: String, cloudCover: String, mainColor: Color) {
    val uvIndexInt = uvIndex.toIntOrNull() ?: 0
    val uvDescription = when (uvIndexInt) {
        in 0..2 -> "Low"
        in 3..5 -> "Moderate"
        in 6..7 -> "High"
        in 8..10 -> "Extreme"
        else -> "Extreme"
    }

    Box(
        modifier = Modifier
            .padding(start = 32.dp, end = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            InfoBox(
                icon = Icons.Default.WbSunny,
                description = "UV",
                value = uvIndex,
                unit = uvDescription,
                mainColor = mainColor,
                modifier = Modifier.weight(1f)
            )

            InfoBox(
                icon = Icons.Default.RemoveRedEye,
                description = "Visibility",
                value = visibility,
                unit = "Miles",
                mainColor = mainColor,
                modifier = Modifier.weight(1f)
            )

            InfoBox(
                icon = Icons.Default.WbCloudy,
                description = "Cloud",
                value = cloudCover,
                unit = "%",
                mainColor = mainColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SunriseSunsetSlider(mainColor: Color, sunrise: String, sunset: String) {
    val currentTime = remember { mutableStateOf(LocalTime.now()) }

    // Update the current time every second
    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = LocalTime.now().truncatedTo(ChronoUnit.SECONDS)
            delay(1000)
        }
    }

    val sunriseTime: LocalTime = LocalTime.parse(sunrise, DateTimeFormatter.ofPattern("hh:mm a"))
    val sunsetTime: LocalTime = LocalTime.parse(sunset, DateTimeFormatter.ofPattern("hh:mm a"))

    // Convert time to float for the slider
    val minTime = sunriseTime.toSecondOfDay().toFloat()
    val maxTime = sunsetTime.toSecondOfDay().toFloat()

    val currentTimeFloat = currentTime.value.toSecondOfDay().toFloat()

    val sliderPosition = remember { mutableStateOf(currentTimeFloat) }

    // Update the slider position to match the current time
    LaunchedEffect(currentTime.value) {
        sliderPosition.value = currentTimeFloat
    }

    Box(
        modifier = Modifier
            .padding(start = 32.dp, end = 32.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = mainColor),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(top = 24.dp, start = 20.dp, end = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                ) {
                    val imagePainter: Painter = painterResource(id = R.drawable.sunrise)
                    Icon(
                        painter = imagePainter,
                        contentDescription = "My Icon",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp) // Điều chỉnh kích thước tùy ý
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Sunrise",
                        fontSize = 20.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                ) {
                    val imagePainter: Painter = painterResource(id = R.drawable.sunset)
                    Icon(
                        painter = imagePainter,
                        contentDescription = "My Icon",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp) // Điều chỉnh kích thước tùy ý
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Sunset",
                        fontSize = 20.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
            Slider(
                value = sliderPosition.value,
                onValueChange = { sliderPosition.value = it },
                valueRange = minTime..maxTime,
                colors = SliderDefaults.colors(
                    activeTrackColor = Color.Blue, // Màu của đường slider
                    thumbColor = Color.Yellow, // Màu của con trỏ trượt
                    inactiveTrackColor = Color.White // Màu của phần còn lại của Slider
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp),
            )

            Row(
                modifier = Modifier.padding(bottom = 24.dp, start = 20.dp, end = 20.dp)
            ) {
                Text(
                    text = sunrise,
                    fontSize = 20.sp,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.weight(1f))


                Text(
                    text = sunset,
                    fontSize = 20.sp,
                    color = Color.White,
                )
            }
        }
    }
}