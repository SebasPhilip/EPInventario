package com.example.inventiapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import com.example.inventiapp.model.ROLES
import com.example.inventiapp.navigation.Screen
import com.example.inventiapp.viewmodel.InventarioViewModel
import kotlinx.coroutines.launch
import com.example.inventiapp.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen (
    navController: NavController,
    authViewModel: AuthViewModel,
    inventarioViewModel: InventarioViewModel
){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val productList by inventarioViewModel.producto.collectAsState()
    val currentRole by authViewModel.currentRole.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "Menú Inventario (${currentRole?.name})", modifier = Modifier.padding(16.dp))

                if (currentRole == ROLES.BODEGA || currentRole == ROLES.ADMINISTRADOR) {
                    NavigationDrawerItem(
                        label = { Text(text = "Registrar Movimiento") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.Movements.route)
                        }
                    )
                }

                if (currentRole == ROLES.ADMINISTRADOR) {
                    NavigationDrawerItem(
                        label = { Text(text = "Configuración") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.Settings.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                NavigationDrawerItem(
                    label = { Text(text = "Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        authViewModel.logout {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Stock Actual") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(productList) { product ->
                    val isLowStock = product.stock <= product.minStock
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isLowStock) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "SKU: ${product.sku}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Stock: ${product.stock}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            if (isLowStock) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Stock Bajo",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
