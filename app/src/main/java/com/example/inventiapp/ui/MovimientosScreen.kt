package com.example.inventiapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.inventiapp.navigation.Screen
import com.example.inventiapp.viewmodel.InventarioViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MovimientosScreen(
        navController: NavController,
        inventarioViewModel: InventarioViewModel
    ) {
        var sku by remember { mutableStateOf("") }
        var quantity by remember { mutableStateOf("") }
        var isEntry by remember { mutableStateOf(true) }

        val formMessage by inventarioViewModel.formError.collectAsState()
        val scope = rememberCoroutineScope()

        val colorAnimado by animateColorAsState(
            targetValue = if (isEntry) Color(0xFF4CAF50) else Color(0xFFE53935),
            animationSpec = tween(durationMillis = 500),
            label = "colorBoton"
        )

        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate(Screen.Home.route) },
                        label = { Text("Inventario") },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = { },
                        label = { Text("Movimientos") },
                        icon = { Icon(Icons.Default.List, contentDescription = "Movimientos") }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Registrar Movimiento", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = sku,
                    onValueChange = { sku = it; inventarioViewModel.clearError() },
                    label = { Text("SKU del Producto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it; inventarioViewModel.clearError() },
                    label = { Text("Cantidad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = isEntry, onClick = { isEntry = true })
                    Text("Entrada")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = !isEntry, onClick = { isEntry = false })
                    Text("Salida")
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        inventarioViewModel.registrarMovimiento(sku, quantity, isEntry)
                        scope.launch {
                            delay(3000)
                            inventarioViewModel.clearError()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorAnimado)
                ) {
                    Text(if (isEntry) "Guardar Entrada" else "Guardar Salida", style = MaterialTheme.typography.titleLarge)
                }

                Spacer(modifier = Modifier.height(24.dp))
                AnimatedVisibility(visible = formMessage != null) {
                    formMessage?.let { msg ->
                        val isSuccess = msg.contains("éxito")
                        Text(
                            text = msg,
                            color = if (isSuccess) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
