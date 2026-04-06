package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.DeviceDashboard
import com.example.myapplication.ui.DeviceDetailView
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    val navController = rememberNavController()
    
    // Manual injection of repository into ViewModel
    val viewModel: DeviceViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = DeviceRepository(RetrofitClient.service)
                return DeviceViewModel(repository) as T
            }
        }
    )

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color(0xFFF5F5F5)
            ) { innerPadding ->
                DeviceDashboard(
                    modifier = Modifier.padding(innerPadding),
                    devices = viewModel.devices,
                    isLoading = viewModel.isLoading,
                    onDeviceClick = { device ->
                        navController.navigate("detail/${device.id}")
                    }
                )
            }
        }
        composable(
            "detail/{deviceId}",
            arguments = listOf(navArgument("deviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId")
            val device = viewModel.getDeviceById(deviceId)
            if (device != null) {
                DeviceDetailView(
                    device = device,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
