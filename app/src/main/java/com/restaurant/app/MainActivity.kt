package com.restaurant.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.restaurant.app.ui.RestaurantApp
import com.restaurant.app.ui.theme.OshiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { OshiTheme { RestaurantApp() } }
    }
}
