package com.example.inventiapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "configuracion_admin")

class ConfiguracionesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore
    private val NOTIFICACIONES_KEY = booleanPreferencesKey("alertas_activadas")

    val notificationsEnabled: Flow<Boolean> = dataStore.data.map { preferencias ->
        preferencias[NOTIFICACIONES_KEY] ?: true
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferencias ->
                preferencias[NOTIFICACIONES_KEY] = enabled
            }
        }
    }
}