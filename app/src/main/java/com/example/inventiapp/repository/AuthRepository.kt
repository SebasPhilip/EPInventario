package com.example.inventiapp.repository

import com.example.inventiapp.model.ROLES
import com.example.inventiapp.model.Usuario

class AuthRepository {

    private val testUsers = listOf(
        Usuario("admin@inventiapp.cl", ROLES.ADMINISTRADOR),
        Usuario("bodega@inventiapp.cl", ROLES.BODEGA),
        Usuario("ventas@inventiapp.cl", ROLES.VENTAS)
    )

    fun authenticate(correo: String, clave: String): ROLES? {
        if (clave.length < 4) return null

        val usuario = testUsers.find { it.correo == correo.lowercase().trim() }
        return usuario?.rol
    }
}