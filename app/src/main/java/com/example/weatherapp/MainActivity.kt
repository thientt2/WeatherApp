package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.advancedscreen.Advanced
import com.example.weatherapp.ui.homescreen.Home
import com.example.weatherapp.ui.theme.GreenCL
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BottomNavigation()
                }
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
                containerColor = GreenCL,
                tonalElevation = 8.dp
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Cloud, contentDescription = null,
                        tint = if(selected.value == Icons.Default.Cloud) Color.White else Color.DarkGray ) },
                    label = { Text("Cloud", color = if(selected.value == Icons.Default.Cloud) Color.White else Color.DarkGray) },
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
                    icon = { Icon(Icons.Default.Settings, contentDescription = null,
                        tint = if(selected.value == Icons.Default.Settings) Color.White else Color.DarkGray) },
                    label = { Text("Advanced", color = if(selected.value == Icons.Default.Settings) Color.White else Color.DarkGray) },
                    selected = selected.value == Icons.Default.Settings,
                    onClick = {
                        selected.value = Icons.Default.Settings
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
