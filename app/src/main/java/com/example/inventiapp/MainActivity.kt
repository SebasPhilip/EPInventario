package com.example.inventiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventiapp.navigation.Screen
import com.example.inventiapp.ui.ConfiguracionesScreen
import com.example.inventiapp.ui.HomeScreen
import com.example.inventiapp.ui.LoginScreen
import com.example.inventiapp.ui.MovimientosScreen
import com.example.inventiapp.ui.theme.InventiAppTheme
import com.example.inventiapp.viewmodel.AuthViewModel
import com.example.inventiapp.viewmodel.ConfiguracionesViewModel
import com.example.inventiapp.viewmodel.InventarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventiAppTheme {
                val authViewModel: AuthViewModel = viewModel()
                val inventarioViewModel: InventarioViewModel = viewModel()
                val configuracionesViewModel: ConfiguracionesViewModel = viewModel()

                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = Screen.Login.route) {
                            LoginScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                        composable(route = Screen.Home.route) {
                            HomeScreen(
                                navController = navController,
                                authViewModel = authViewModel,
                                inventarioViewModel = inventarioViewModel
                            )
                        }
                        composable(route = Screen.Movements.route) {
                            MovimientosScreen(
                                navController = navController,
                                inventarioViewModel = inventarioViewModel
                            )
                        }
                        composable(route = Screen.Settings.route) {
                            ConfiguracionesScreen(
                                navController = navController,
                                configuracionesViewModel = configuracionesViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
