package com.example.inventiapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.inventiapp.model.DatosLogin
import com.example.inventiapp.model.LoginErrores
import com.example.inventiapp.model.ROLES
import com.example.inventiapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _currentRole = MutableStateFlow<ROLES?>(null)
    val currentRole: StateFlow<ROLES?> = _currentRole.asStateFlow()

    private val _loginState = MutableStateFlow(DatosLogin())
    val loginState: StateFlow<DatosLogin> = _loginState.asStateFlow()

    fun onUsuarioChange(valor: String) {
        _loginState.update { it.copy(usuario = valor, errores = it.errores.copy(usuario = null)) }
    }

    fun onClaveChange(valor: String) {
        _loginState.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun validarLogin(onSuccess: () -> Unit) {
        val estadoActual = _loginState.value

        val errores = LoginErrores(
            usuario = if (!estadoActual.usuario.contains("@")) "Ingresa un correo electrónico válido" else null,
            clave = if (estadoActual.clave.length < 4) "La clave debe tener mínimo 4 caracteres" else null
        )

        val hayErrores = listOfNotNull(errores.usuario, errores.clave).isNotEmpty()
        _loginState.update { it.copy(errores = errores) }

        if (!hayErrores) {
            val rolAsignado = authRepository.authenticate(estadoActual.usuario, estadoActual.clave)

            if (rolAsignado != null) {
                _loginState.value = DatosLogin()
                _currentRole.value = rolAsignado
                onSuccess()
            } else {
                _loginState.update {
                    it.copy(errores = it.errores.copy(usuario = "Correo no registrado o clave incorrecta"))
                }
            }
        }
    }

    fun logout(onNavigate: () -> Unit) {
        _currentRole.value = null
        onNavigate()
    }
}