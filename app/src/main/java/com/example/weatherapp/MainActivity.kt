package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.constant.Const
import com.example.weatherapp.constant.Const.Companion.permissions
import com.example.weatherapp.modal.weather.MyLatLng
import com.example.weatherapp.modal.weather.WeatherResponse
import com.example.weatherapp.ui.advancedscreen.Advanced
import com.example.weatherapp.ui.homescreen.Home
import com.example.weatherapp.ui.theme.Black20
import com.example.weatherapp.ui.theme.Blue20
import com.example.weatherapp.ui.theme.GreenCL
import com.example.weatherapp.ui.theme.Grey60
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodal.WeatherViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.coroutineScope

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var weatherViewModel: WeatherViewModel
    private var locationRequired: Boolean = false

    override fun onResume() {
        super.onResume()
        if (locationRequired) startLocationUpdate();
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let {
            fusedLocationProviderClient?.removeLocationUpdates(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        locationCallback?.let {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 100
            )
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateDelayMillis(100)
                .build()

            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocationClient()
        initViewModel()
        setContent {
            var currentLocation by remember {
                mutableStateOf(MyLatLng(0.0,0.0))
            }

            locationCallback = object: LocationCallback(){
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations){
                        currentLocation = MyLatLng(
                            location.latitude,
                            location.longitude
                        )
                    }
                    fetchWeatherInformation(weatherViewModel, currentLocation)
                }
            }
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LocationScreen(this@MainActivity, currentLocation)
                }
            }
        }
    }

    private fun initViewModel() {
        weatherViewModel = ViewModelProvider(this@MainActivity)[WeatherViewModel::class.java]
    }

    private fun fetchWeatherInformation(weatherViewModel: WeatherViewModel, currentLocation: MyLatLng) {
        weatherViewModel.getWeatherByLocaion(currentLocation)
    }

    private fun initLocationClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    @Composable
    fun LocationScreen(context: Context, currentLocation: MyLatLng) {
        val launcherMultiplePermission = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionMap ->
            val areGranted = permissionMap.values.reduce { accepted, next ->
                accepted && next
            }

            if (areGranted) {
                locationRequired = true;
                startLocationUpdate();
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

        val systemUiController = rememberSystemUiController()

        DisposableEffect(key1 = true, effect = {
            systemUiController.isSystemBarsVisible = false
            onDispose {
                systemUiController.isSystemBarsVisible = true
            }
        })

        LaunchedEffect(key1 = currentLocation, block = {
            coroutineScope {
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    startLocationUpdate()
                } else {
                    launcherMultiplePermission.launch(permissions)
                }

            }
        })

        WeatherSection(weatherViewModel.weatherRespone)


//        BottomNavigation(weatherViewModel = weatherViewModel)

    }

    @Composable
    fun WeatherSection(weatherRespone: WeatherResponse) {
        Column {

            Text(text = weatherViewModel.toString())
            Text(text = weatherRespone.toString())
        }

    }


}



@Composable
fun BottomNavigation() {
    val navigationController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.Cloud)
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Black20,
                tonalElevation = 8.dp,
            ) {
                BottomNavigationItem(
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Cloud, contentDescription = null,
                                tint = if(selected.value == Icons.Default.Cloud) Blue20 else Grey60)
                            Spacer(modifier = Modifier.height(4.dp)) // Add space between icon and label
                            Text("Weather", fontWeight = FontWeight.Bold, color = if(selected.value == Icons.Default.Cloud) Blue20 else Grey60)
                        }
                    },
                    selected = selected.value == Icons.Default.Cloud,
                    onClick = {
                        selected.value = Icons.Default.Cloud
                        navigationController.navigate(Screen.Home.screen){
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                BottomNavigationItem(
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Newspaper, contentDescription = null,
                                tint = if(selected.value == Icons.Default.Newspaper) Blue20 else Grey60)
                            Spacer(modifier = Modifier.height(4.dp)) // Add space between icon and label
                            Text("News", fontWeight = FontWeight.Bold, color = if(selected.value == Icons.Default.Newspaper) Blue20 else Grey60)
                        }
                    },
                    selected = selected.value == Icons.Default.Newspaper,
                    onClick = {
                        selected.value = Icons.Default.Newspaper
                        navigationController.navigate(Screen.Advanced.screen){
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    ) {paddingValues ->
        NavHost(navController = navigationController,
            startDestination = Screen.Home.screen,
            modifier = Modifier.padding(paddingValues)){
            composable(Screen.Home.screen){Home()}
            composable(Screen.Advanced.screen){ Advanced() }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomNavigation() {
    WeatherAppTheme {
        BottomNavigation()
    }
}
