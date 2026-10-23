package com.example.inventiapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventiapp.data.AppDatabase
import com.example.inventiapp.model.Productos
import com.example.inventiapp.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


    class InventarioViewModel(application: Application) : AndroidViewModel(application) {

        private val database = AppDatabase.getDatabase(application)
        private val inventarioRepository = InventarioRepository(database.inventarioDao())

        private val _productos = MutableStateFlow<List<Productos>>(emptyList())
        val producto: StateFlow<List<Productos>> = _productos.asStateFlow()

        private val _formError = MutableStateFlow<String?>(null)
        val formError: StateFlow<String?> = _formError.asStateFlow()

        init {
            viewModelScope.launch {
                inventarioRepository.insertInitialData()


                inventarioRepository.producto.collect { list ->
                    _productos.value = list
                }
            }
        }

        fun registrarMovimiento(sku: String, quantity: String, isEntry: Boolean) {
            val qty = quantity.toIntOrNull()
            if (sku.isBlank()) {
                _formError.value = "El SKU no puede estar vacío"
                return
            }
            if (qty == null || qty <= 0) {
                _formError.value = "La cantidad debe ser un número válido mayor a 0"
                return
            }

            viewModelScope.launch {
                val resultMessage = inventarioRepository.updateStock(sku, qty, isEntry)
                _formError.value = resultMessage
            }
        }

        fun clearError() {
            _formError.value = null
        }
    }
