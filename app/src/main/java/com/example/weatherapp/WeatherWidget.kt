package com.example.weatherapp

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.weatherapp.constant.Const.Companion.maxTempCG
import com.example.weatherapp.constant.Const.Companion.minTempCG
import com.example.weatherapp.constant.Const.Companion.tempCG
import com.example.weatherapp.constant.Const.Companion.weatherCodeG
import com.example.weatherapp.constant.Const.Companion.weatherDescG
//import androidx.glance.wear.tiles.border
//import androidx.work.CoroutineWorker
//import androidx.work.OneTimeWorkRequest
//import androidx.work.WorkManager
//import androidx.work.WorkerParameters
import com.example.weatherapp.ui.homescreen.LoadingSection
import com.example.weatherapp.ui.homescreen.WeatherDescription
import com.example.weatherapp.ui.homescreen.WeatherScreen
import com.example.weatherapp.viewmodal.LocationViewModel
import com.example.weatherapp.viewmodal.WeatherViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.security.Provider
import java.time.LocalDateTime
import java.util.Locale

class WeatherWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val sharedPrefs = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        val weatherData = sharedPrefs.getString("weather_data", "No Data")

        provideContent {
//            Image()
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable(
                        onClick = actionStartActivity<MainActivity>()
                    )
            ) {
//                Text(text = weatherData.toString())
//                Text(text = "Weather Widget")
//                Button(
//                    text = "Open App",
//                    onClick = actionStartActivity<MainActivity>()
//                )

                val isDaytime = com.example.weatherapp.ui.homescreen.isDaytime()
                val imageResource = com.example.weatherapp.ui.homescreen.getDayNightImageResource(
                    isDaytime,
                    weatherCodeG
                )
                val locale = Locale.getDefault()
                val desc = WeatherDescription.fromCode(weatherCodeG, locale)
                Row (
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.glance.Image(
                        provider = ImageProvider(imageResource),
                        contentDescription = "Description of the image",
                        contentScale = androidx.glance.layout.ContentScale.Crop,
                        modifier = GlanceModifier
                            .size(120.dp, 100.dp)
                            .padding(start = 24.dp)
                    )

                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = tempCG, style = TextStyle(fontSize = 25.sp, color = ColorProvider(Color.White)))
                        Text(text = desc, style = TextStyle(fontSize = 15.sp, color = ColorProvider(Color.White)))
                        Text(text = "${maxTempCG}° / ${minTempCG}°", style = TextStyle(fontSize = 15.sp, color = ColorProvider(Color.White)))
                    }
                }
            }


        }
    }
}

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel) {
    val weather by weatherViewModel.weather.collectAsState()
    Text(text = weather.toString())
}

class TestWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()

//    override fun onEnabled(context: Context) {
//        super.onEnabled(context)
//        val workRequest = OneTimeWorkRequest.Builder(UpdateWeatherWorker::class.java).build()
//        WorkManager.getInstance(context).enqueue(workRequest)
//    }
//
//    override fun onUpdate(
//        context: Context,
//        appWidgetManager: AppWidgetManager,
//        appWidgetIds: IntArray
//    ) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds)
//        val workRequest = OneTimeWorkRequest.Builder(UpdateWeatherWorker::class.java).build()
//        WorkManager.getInstance(context).enqueue(workRequest)
//    }
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