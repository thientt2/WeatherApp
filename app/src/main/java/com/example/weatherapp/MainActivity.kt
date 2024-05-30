package com.example.weatherapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.advancedscreen.Advanced
import com.example.weatherapp.ui.homescreen.Home
import com.example.weatherapp.ui.theme.Black20
import com.example.weatherapp.ui.theme.Blue20
import com.example.weatherapp.ui.theme.Grey60
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodal.LocationViewModel
import com.example.weatherapp.viewmodal.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BottomNavigation()
                }
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
                                Icon(
                                    Icons.Default.Cloud, contentDescription = null,
                                    tint = if (selected.value == Icons.Default.Cloud) Blue20 else Grey60
                                )
                                Spacer(modifier = Modifier.height(4.dp)) // Add space between icon and label
                                Text(
                                    "Thời tiết",
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected.value == Icons.Default.Cloud) Blue20 else Grey60
                                )
                            }
                        },
                        selected = selected.value == Icons.Default.Cloud,
                        onClick = {
                            selected.value = Icons.Default.Cloud
                            navigationController.navigate(Screen.Home.screen) {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    BottomNavigationItem(
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Newspaper, contentDescription = null,
                                    tint = if (selected.value == Icons.Default.Newspaper) Blue20 else Grey60
                                )
                                Spacer(modifier = Modifier.height(4.dp)) // Add space between icon and label
                                Text(
                                    "Bảng tin",
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected.value == Icons.Default.Newspaper) Blue20 else Grey60
                                )
                            }
                        },
                        selected = selected.value == Icons.Default.Newspaper,
                        onClick = {
                            selected.value = Icons.Default.Newspaper
                            navigationController.navigate(Screen.Advanced.screen) {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navigationController,
                startDestination = Screen.Home.screen,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Home.screen) { Home() }
                composable(Screen.Advanced.screen) { Advanced() }
            }
        }
    }
}


